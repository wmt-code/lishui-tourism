package com.lishui.tourism.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lishui.tourism.common.context.UserContext;
import com.lishui.tourism.common.exception.BusinessException;
import com.lishui.tourism.entity.Favorite;
import com.lishui.tourism.mapper.FavoriteMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 收藏服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService extends ServiceImpl<FavoriteMapper, Favorite> {
    
    /**
     * 添加收藏
     */
    @Transactional(rollbackFor = Exception.class)
    public void addFavorite(String targetType, Long targetId) {
        Long userId = UserContext.getUserId();
        
        // 检查是否已收藏
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, targetType)
                .eq(Favorite::getTargetId, targetId);
        
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("已经收藏过了");
        }
        
        // 创建收藏
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setTargetType(targetType);
        favorite.setTargetId(targetId);
        
        baseMapper.insert(favorite);
        log.info("添加收藏成功：userId={}, targetType={}, targetId={}", userId, targetType, targetId);
    }
    
    /**
     * 取消收藏
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeFavorite(String targetType, Long targetId) {
        Long userId = UserContext.getUserId();
        
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, targetType)
                .eq(Favorite::getTargetId, targetId);
        
        baseMapper.delete(wrapper);
        log.info("取消收藏成功：userId={}, targetType={}, targetId={}", userId, targetType, targetId);
    }
    
    /**
     * 检查是否已收藏
     */
    public boolean isFavorited(String targetType, Long targetId) {
        Long userId = UserContext.getUserId();
        
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, targetType)
                .eq(Favorite::getTargetId, targetId);
        
        return baseMapper.selectCount(wrapper) > 0;
    }
    
    /**
     * 获取用户的收藏列表
     */
    public List<Favorite> getUserFavorites(String targetType) {
        Long userId = UserContext.getUserId();
        
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        
        if (targetType != null && !targetType.isEmpty()) {
            wrapper.eq(Favorite::getTargetType, targetType);
        }
        
        wrapper.orderByDesc(Favorite::getCreatedAt);
        
        return baseMapper.selectList(wrapper);
    }
    
    /**
     * 获取目标的收藏数
     */
    public long getFavoriteCount(String targetType, Long targetId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getTargetType, targetType)
                .eq(Favorite::getTargetId, targetId);
        
        return baseMapper.selectCount(wrapper);
    }
}
