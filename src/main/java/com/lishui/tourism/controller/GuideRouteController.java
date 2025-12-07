package com.lishui.tourism.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lishui.tourism.common.constant.Constants;
import com.lishui.tourism.common.result.Result;
import com.lishui.tourism.config.security.RequireRole;
import com.lishui.tourism.entity.GuideRoute;
import com.lishui.tourism.entity.GuideRouteSpotRel;
import com.lishui.tourism.service.GuideRouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 导游线路控制器
 */
@Tag(name = "导游线路管理")
@RestController
@RequestMapping("/guide-route")
@RequiredArgsConstructor
public class GuideRouteController {
    
    private final GuideRouteService guideRouteService;
    
    @Operation(summary = "创建线路（导游）")
    @RequireRole({Constants.Role.GUIDE, Constants.Role.ADMIN})
    @PostMapping
    public Result<Long> createRoute(@Valid @RequestBody RouteRequest request) {
        GuideRoute route = new GuideRoute();
        route.setName(request.getName());
        route.setDescription(request.getDescription());
        route.setDays(request.getDays());
        route.setPrice(request.getPrice());
        route.setMaxPeople(request.getMaxPeople());
        
        Long id = guideRouteService.createRoute(route);
        return Result.success(id);
    }
    
    @Operation(summary = "更新线路（导游）")
    @RequireRole({Constants.Role.GUIDE, Constants.Role.ADMIN})
    @PutMapping("/{routeId}")
    public Result<Void> updateRoute(
            @PathVariable Long routeId,
            @Valid @RequestBody RouteRequest request) {
        GuideRoute route = new GuideRoute();
        route.setName(request.getName());
        route.setDescription(request.getDescription());
        route.setDays(request.getDays());
        route.setPrice(request.getPrice());
        route.setMaxPeople(request.getMaxPeople());
        
        guideRouteService.updateRoute(routeId, route);
        return Result.success();
    }
    
    @Operation(summary = "删除线路（导游）")
    @RequireRole({Constants.Role.GUIDE, Constants.Role.ADMIN})
    @DeleteMapping("/{routeId}")
    public Result<Void> deleteRoute(@PathVariable Long routeId) {
        guideRouteService.deleteRoute(routeId);
        return Result.success();
    }
    
    @Operation(summary = "添加景点到线路（导游）")
    @RequireRole({Constants.Role.GUIDE, Constants.Role.ADMIN})
    @PostMapping("/{routeId}/spots")
    public Result<Void> addSpotToRoute(
            @PathVariable Long routeId,
            @Valid @RequestBody RouteSpotRequest request) {
        guideRouteService.addSpotToRoute(
                routeId,
                request.getSpotId(),
                request.getDayNumber(),
                request.getSortOrder(),
                request.getDuration(),
                request.getDescription()
        );
        return Result.success();
    }
    
    @Operation(summary = "从线路移除景点（导游）")
    @RequireRole({Constants.Role.GUIDE, Constants.Role.ADMIN})
    @DeleteMapping("/{routeId}/spots/{spotId}")
    public Result<Void> removeSpotFromRoute(
            @PathVariable Long routeId,
            @PathVariable Long spotId) {
        guideRouteService.removeSpotFromRoute(routeId, spotId);
        return Result.success();
    }
    
    @Operation(summary = "获取线路景点列表")
    @GetMapping("/{routeId}/spots")
    public Result<List<GuideRouteSpotRel>> getRouteSpots(@PathVariable Long routeId) {
        List<GuideRouteSpotRel> spots = guideRouteService.getRouteSpots(routeId);
        return Result.success(spots);
    }
    
    @Operation(summary = "线路列表")
    @GetMapping("/list")
    public Result<IPage<GuideRoute>> listRoutes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long guideId) {
        IPage<GuideRoute> routes = guideRouteService.listRoutes(page, size, guideId);
        return Result.success(routes);
    }
    
    @Operation(summary = "我的线路列表（导游）")
    @RequireRole({Constants.Role.GUIDE, Constants.Role.ADMIN})
    @GetMapping("/my")
    public Result<List<GuideRoute>> getMyRoutes() {
        List<GuideRoute> routes = guideRouteService.getMyRoutes();
        return Result.success(routes);
    }
    
    @Operation(summary = "线路详情")
    @GetMapping("/{routeId}")
    public Result<GuideRoute> getRouteDetail(@PathVariable Long routeId) {
        GuideRoute route = guideRouteService.getRouteDetail(routeId);
        return Result.success(route);
    }
    
    @Data
    public static class RouteRequest {
        @NotBlank(message = "线路名称不能为空")
        private String name;
        
        private String description;
        
        @NotNull(message = "天数不能为空")
        private Integer days;
        
        private BigDecimal price;
        
        private Integer maxPeople;
    }
    
    @Data
    public static class RouteSpotRequest {
        @NotNull(message = "景点ID不能为空")
        private Long spotId;
        
        @NotNull(message = "天数不能为空")
        private Integer dayNumber;
        
        private Integer sortOrder;
        
        private Integer duration;
        
        private String description;
    }
}
