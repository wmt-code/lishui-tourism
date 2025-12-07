package com.lishui.tourism.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lishui.tourism.common.constant.Constants;
import com.lishui.tourism.common.context.UserContext;
import com.lishui.tourism.common.exception.BusinessException;
import com.lishui.tourism.common.result.ResultCode;
import com.lishui.tourism.entity.Comment;
import com.lishui.tourism.entity.ScenicSpot;
import com.lishui.tourism.mapper.CommentMapper;
import com.lishui.tourism.mapper.ScenicSpotMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 评论服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService extends ServiceImpl<CommentMapper, Comment> {
    
    private final ScenicSpotMapper scenicSpotMapper;
    
    /**
     * 发表评论
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createComment(Long spotId, String content, Integer rating, String images, Long parentId) {
        Long userId = UserContext.getUserId();
        
        // 检查景点是否存在
        ScenicSpot spot = scenicSpotMapper.selectById(spotId);
        if (spot == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        
        // 创建评论
        Comment comment = new Comment();
        comment.setSpotId(spotId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setRating(rating);
        comment.setImages(images);
        comment.setParentId(parentId);
        comment.setStatus(Constants.Status.ONLINE);
        
        baseMapper.insert(comment);
        
        // 如果有评分，更新景点平均评分
        if (rating != null && rating > 0) {
            updateSpotRating(spotId);
        }
        
        log.info("评论发表成功：spotId={}, userId={}", spotId, userId);
        return comment.getId();
    }
    
    /**
     * 获取景点评论列表
     */
    public IPage<Comment> getSpotComments(Long spotId, int page, int size) {
        Page<Comment> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(Comment::getSpotId, spotId)
                .eq(Comment::getStatus, Constants.Status.ONLINE)
                .isNull(Comment::getParentId)  // 只查询一级评论
                .orderByDesc(Comment::getCreatedAt);
        
        return baseMapper.selectPage(pageParam, wrapper);
    }
    
    /**
     * 获取评论的回复
     */
    public List<Comment> getCommentReplies(Long commentId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getParentId, commentId)
                .eq(Comment::getStatus, Constants.Status.ONLINE)
                .orderByAsc(Comment::getCreatedAt);
        
        return baseMapper.selectList(wrapper);
    }
    
    /**
     * 删除评论
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId) {
        Long userId = UserContext.getUserId();
        String role = UserContext.getRole();
        
        Comment comment = baseMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        
        // 只能删除自己的评论或管理员可删除任何评论
        if (!comment.getUserId().equals(userId) && !Constants.Role.ADMIN.equals(role)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        
        baseMapper.deleteById(commentId);
        
        // 如果有评分，重新计算景点评分
        if (comment.getRating() != null && comment.getRating() > 0) {
            updateSpotRating(comment.getSpotId());
        }
        
        log.info("评论删除成功：commentId={}", commentId);
    }
    
    /**
     * 更新景点平均评分
     */
    private void updateSpotRating(Long spotId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getSpotId, spotId)
                .eq(Comment::getStatus, Constants.Status.ONLINE)
                .isNotNull(Comment::getRating)
                .gt(Comment::getRating, 0);
        
        List<Comment> comments = baseMapper.selectList(wrapper);
        
        if (comments.isEmpty()) {
            return;
        }
        
        // 计算平均分
        double avgRating = comments.stream()
                .mapToInt(Comment::getRating)
                .average()
                .orElse(0.0);
        
        ScenicSpot spot = scenicSpotMapper.selectById(spotId);
        if (spot != null) {
            spot.setRating(BigDecimal.valueOf(avgRating).setScale(2, RoundingMode.HALF_UP));
            scenicSpotMapper.updateById(spot);
        }
    }
    
    /**
     * 隐藏/显示评论（管理员）
     */
    @Transactional(rollbackFor = Exception.class)
    public void toggleCommentStatus(Long commentId, Integer status) {
        Comment comment = baseMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        
        comment.setStatus(status);
        baseMapper.updateById(comment);
        
        log.info("评论状态修改成功：commentId={}, status={}", commentId, status);
    }
}
