package com.lishui.tourism.service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lishui.tourism.common.constant.Constants;
import com.lishui.tourism.common.exception.BusinessException;
import com.lishui.tourism.common.result.ResultCode;
import com.lishui.tourism.entity.Comment;
import com.lishui.tourism.entity.ScenicSpot;
import com.lishui.tourism.entity.ScenicTagRel;
import com.lishui.tourism.entity.Tag;
import com.lishui.tourism.mapper.CommentMapper;
import com.lishui.tourism.mapper.ScenicSpotMapper;
import com.lishui.tourism.mapper.ScenicTagRelMapper;
import com.lishui.tourism.mapper.TagMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 景点服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScenicSpotService extends ServiceImpl<ScenicSpotMapper, ScenicSpot> {
    
    private final ScenicTagRelMapper scenicTagRelMapper;
    private final TagMapper tagMapper;
    private final CommentMapper commentMapper;
    private final RedisService redisService;
    
    /**
     * 景点列表（分页）
     */
    public IPage<ScenicSpot> listSpots(int page, int size, String keyword, Long destinationId) {
        Page<ScenicSpot> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ScenicSpot> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(ScenicSpot::getStatus, Constants.Status.ONLINE);
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(ScenicSpot::getName, keyword)
                    .or().like(ScenicSpot::getDescription, keyword);
        }
        
        if (destinationId != null) {
            wrapper.eq(ScenicSpot::getDestinationId, destinationId);
        }
        
        wrapper.orderByDesc(ScenicSpot::getHotScore);
        
        return baseMapper.selectPage(pageParam, wrapper);
    }
    
    /**
     * 获取景点详情
     */
    public ScenicSpot getSpotDetail(Long id) {
        // 先从缓存获取
        String cacheKey = Constants.RedisKey.SPOT_INFO + id;
        ScenicSpot spot = redisService.get(cacheKey, ScenicSpot.class);
        
        if (spot == null) {
            spot = baseMapper.selectById(id);
            if (spot == null) {
                throw new BusinessException(ResultCode.DATA_NOT_FOUND);
            }
            // 缓存1小时
            redisService.set(cacheKey, spot, 1, TimeUnit.HOURS);
        }
        
        return spot;
    }
    
    /**
     * 创建景点（管理员）
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createSpot(ScenicSpot spot) {
        spot.setStatus(Constants.Status.ONLINE);
        spot.setHotScore(0);
        spot.setRating(java.math.BigDecimal.ZERO);
        baseMapper.insert(spot);
        log.info("景点创建成功：{}", spot.getName());
        return spot.getId();
    }
    
    /**
     * 更新景点（管理员）
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSpot(Long id, ScenicSpot spot) {
        ScenicSpot existing = baseMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        
        spot.setId(id);
        baseMapper.updateById(spot);
        
        // 清除缓存
        redisService.delete(Constants.RedisKey.SPOT_INFO + id);
        
        log.info("景点更新成功：{}", id);
    }
    
    /**
     * 删除景点（管理员）
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteSpot(Long id) {
        baseMapper.deleteById(id);
        redisService.delete(Constants.RedisKey.SPOT_INFO + id);
        log.info("景点删除成功：{}", id);
    }
    
    /**
     * 获取热门景点
     */
    public List<ScenicSpot> getHotSpots(int limit) {
        // 先从缓存获取
        @SuppressWarnings("unchecked")
        List<ScenicSpot> hotSpots = (List<ScenicSpot>) redisService.get(Constants.RedisKey.HOT_SPOTS);
        
        if (hotSpots == null || hotSpots.isEmpty()) {
            LambdaQueryWrapper<ScenicSpot> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ScenicSpot::getStatus, Constants.Status.ONLINE)
                    .orderByDesc(ScenicSpot::getHotScore)
                    .last("LIMIT " + limit);
            
            hotSpots = baseMapper.selectList(wrapper);
            
            // 缓存30分钟
            redisService.set(Constants.RedisKey.HOT_SPOTS, hotSpots, 30, TimeUnit.MINUTES);
        }
        
        return hotSpots;
    }
    
    /**
     * 为景点添加标签
     */
    @Transactional(rollbackFor = Exception.class)
    public void addTags(Long spotId, List<Long> tagIds) {
        // 先删除旧标签
        LambdaQueryWrapper<ScenicTagRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScenicTagRel::getSpotId, spotId);
        scenicTagRelMapper.delete(wrapper);
        
        // 添加新标签
        for (Long tagId : tagIds) {
            ScenicTagRel rel = new ScenicTagRel();
            rel.setSpotId(spotId);
            rel.setTagId(tagId);
            scenicTagRelMapper.insert(rel);
        }
        
        log.info("景点标签更新成功：spotId={}", spotId);
    }
    
    /**
     * 获取景点标签
     */
    public List<Tag> getSpotTags(Long spotId) {
        LambdaQueryWrapper<ScenicTagRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScenicTagRel::getSpotId, spotId);
        List<ScenicTagRel> rels = scenicTagRelMapper.selectList(wrapper);
        
        if (rels.isEmpty()) {
            return List.of();
        }
        
        List<Long> tagIds = rels.stream()
                .map(ScenicTagRel::getTagId)
                .collect(Collectors.toList());
        
        return tagMapper.selectBatchIds(tagIds);
    }
    
    /**
     * 获取景点评论摘要（用于AI上下文）
     */
    public String getCommentSummary(Long spotId, int limit) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getSpotId, spotId)
                .eq(Comment::getStatus, Constants.Status.ONLINE)
                .orderByDesc(Comment::getCreatedAt)
                .last("LIMIT " + limit);
        
        List<Comment> comments = commentMapper.selectList(wrapper);
        
        if (comments.isEmpty()) {
            return "暂无用户评论";
        }
        
        return comments.stream()
                .map(Comment::getContent)
                .collect(Collectors.joining("；"));
    }
}
