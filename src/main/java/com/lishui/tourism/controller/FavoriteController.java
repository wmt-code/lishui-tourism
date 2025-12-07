package com.lishui.tourism.controller;

import com.lishui.tourism.common.constant.Constants;
import com.lishui.tourism.common.result.Result;
import com.lishui.tourism.config.security.RequireRole;
import com.lishui.tourism.entity.Favorite;
import com.lishui.tourism.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收藏控制器
 */
@Tag(name = "收藏管理")
@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    
    private final FavoriteService favoriteService;
    
    @Operation(summary = "添加收藏")
    @RequireRole({Constants.Role.TOURIST, Constants.Role.GUIDE, Constants.Role.ADMIN})
    @PostMapping
    public Result<Void> addFavorite(
            @RequestParam String targetType,
            @RequestParam Long targetId) {
        favoriteService.addFavorite(targetType, targetId);
        return Result.success();
    }
    
    @Operation(summary = "取消收藏")
    @RequireRole({Constants.Role.TOURIST, Constants.Role.GUIDE, Constants.Role.ADMIN})
    @DeleteMapping
    public Result<Void> removeFavorite(
            @RequestParam String targetType,
            @RequestParam Long targetId) {
        favoriteService.removeFavorite(targetType, targetId);
        return Result.success();
    }
    
    @Operation(summary = "检查是否已收藏")
    @RequireRole({Constants.Role.TOURIST, Constants.Role.GUIDE, Constants.Role.ADMIN})
    @GetMapping("/check")
    public Result<Boolean> isFavorited(
            @RequestParam String targetType,
            @RequestParam Long targetId) {
        boolean isFavorited = favoriteService.isFavorited(targetType, targetId);
        return Result.success(isFavorited);
    }
    
    @Operation(summary = "获取我的收藏列表")
    @RequireRole({Constants.Role.TOURIST, Constants.Role.GUIDE, Constants.Role.ADMIN})
    @GetMapping("/my")
    public Result<List<Favorite>> getMyFavorites(
            @RequestParam(required = false) String targetType) {
        List<Favorite> favorites = favoriteService.getUserFavorites(targetType);
        return Result.success(favorites);
    }
    
    @Operation(summary = "获取收藏数")
    @GetMapping("/count")
    public Result<Long> getFavoriteCount(
            @RequestParam String targetType,
            @RequestParam Long targetId) {
        long count = favoriteService.getFavoriteCount(targetType, targetId);
        return Result.success(count);
    }
}
