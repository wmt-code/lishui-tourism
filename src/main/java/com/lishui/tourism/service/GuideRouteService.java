package com.lishui.tourism.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lishui.tourism.common.constant.Constants;
import com.lishui.tourism.common.context.UserContext;
import com.lishui.tourism.common.exception.BusinessException;
import com.lishui.tourism.common.result.ResultCode;
import com.lishui.tourism.entity.GuideRoute;
import com.lishui.tourism.entity.GuideRouteSpotRel;
import com.lishui.tourism.mapper.GuideRouteMapper;
import com.lishui.tourism.mapper.GuideRouteSpotRelMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 导游线路服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GuideRouteService extends ServiceImpl<GuideRouteMapper, GuideRoute> {
    
    private final GuideRouteSpotRelMapper guideRouteSpotRelMapper;
    
    /**
     * 创建线路
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createRoute(GuideRoute route) {
        Long userId = UserContext.getUserId();
        route.setGuideId(userId);
        route.setStatus(Constants.Status.ONLINE);
        
        baseMapper.insert(route);
        log.info("线路创建成功：{}", route.getName());
        return route.getId();
    }
    
    /**
     * 更新线路
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRoute(Long routeId, GuideRoute route) {
        Long userId = UserContext.getUserId();
        String role = UserContext.getRole();
        
        GuideRoute existingRoute = baseMapper.selectById(routeId);
        if (existingRoute == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        
        // 只能修改自己的线路或管理员可修改任何线路
        if (!existingRoute.getGuideId().equals(userId) && !Constants.Role.ADMIN.equals(role)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        
        route.setId(routeId);
        baseMapper.updateById(route);
        log.info("线路更新成功：routeId={}", routeId);
    }
    
    /**
     * 删除线路
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoute(Long routeId) {
        Long userId = UserContext.getUserId();
        String role = UserContext.getRole();
        
        GuideRoute route = baseMapper.selectById(routeId);
        if (route == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        
        // 只能删除自己的线路或管理员可删除任何线路
        if (!route.getGuideId().equals(userId) && !Constants.Role.ADMIN.equals(role)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        
        baseMapper.deleteById(routeId);
        log.info("线路删除成功：routeId={}", routeId);
    }
    
    /**
     * 添加景点到线路
     */
    @Transactional(rollbackFor = Exception.class)
    public void addSpotToRoute(Long routeId, Long spotId, Integer dayNumber, 
                               Integer sortOrder, Integer duration, String description) {
        Long userId = UserContext.getUserId();
        String role = UserContext.getRole();
        
        GuideRoute route = baseMapper.selectById(routeId);
        if (route == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        
        // 只能修改自己的线路或管理员可修改任何线路
        if (!route.getGuideId().equals(userId) && !Constants.Role.ADMIN.equals(role)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        
        GuideRouteSpotRel rel = new GuideRouteSpotRel();
        rel.setRouteId(routeId);
        rel.setSpotId(spotId);
        rel.setDayNumber(dayNumber);
        rel.setSortOrder(sortOrder);
        rel.setDuration(duration);
        rel.setDescription(description);
        
        guideRouteSpotRelMapper.insert(rel);
        log.info("景点添加到线路：routeId={}, spotId={}", routeId, spotId);
    }
    
    /**
     * 从线路移除景点
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeSpotFromRoute(Long routeId, Long spotId) {
        Long userId = UserContext.getUserId();
        String role = UserContext.getRole();
        
        GuideRoute route = baseMapper.selectById(routeId);
        if (route == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        
        // 只能修改自己的线路或管理员可修改任何线路
        if (!route.getGuideId().equals(userId) && !Constants.Role.ADMIN.equals(role)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        
        LambdaQueryWrapper<GuideRouteSpotRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GuideRouteSpotRel::getRouteId, routeId)
                .eq(GuideRouteSpotRel::getSpotId, spotId);
        
        guideRouteSpotRelMapper.delete(wrapper);
        log.info("景点从线路移除：routeId={}, spotId={}", routeId, spotId);
    }
    
    /**
     * 获取线路景点列表
     */
    public List<GuideRouteSpotRel> getRouteSpots(Long routeId) {
        LambdaQueryWrapper<GuideRouteSpotRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GuideRouteSpotRel::getRouteId, routeId)
                .orderByAsc(GuideRouteSpotRel::getDayNumber)
                .orderByAsc(GuideRouteSpotRel::getSortOrder);
        
        return guideRouteSpotRelMapper.selectList(wrapper);
    }
    
    /**
     * 线路列表
     */
    public IPage<GuideRoute> listRoutes(int page, int size, Long guideId) {
        Page<GuideRoute> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<GuideRoute> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(GuideRoute::getStatus, Constants.Status.ONLINE);
        
        if (guideId != null) {
            wrapper.eq(GuideRoute::getGuideId, guideId);
        }
        
        wrapper.orderByDesc(GuideRoute::getCreatedAt);
        
        return baseMapper.selectPage(pageParam, wrapper);
    }
    
    /**
     * 我的线路列表
     */
    public List<GuideRoute> getMyRoutes() {
        Long userId = UserContext.getUserId();
        
        LambdaQueryWrapper<GuideRoute> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GuideRoute::getGuideId, userId)
                .orderByDesc(GuideRoute::getCreatedAt);
        
        return baseMapper.selectList(wrapper);
    }
    
    /**
     * 线路详情
     */
    public GuideRoute getRouteDetail(Long routeId) {
        GuideRoute route = baseMapper.selectById(routeId);
        if (route == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        return route;
    }
}
