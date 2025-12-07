package com.lishui.tourism.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lishui.tourism.common.constant.Constants;
import com.lishui.tourism.common.context.UserContext;
import com.lishui.tourism.common.result.Result;
import com.lishui.tourism.config.security.RequireRole;
import com.lishui.tourism.dto.user.UserLoginDTO;
import com.lishui.tourism.dto.user.UserRegisterDTO;
import com.lishui.tourism.dto.user.UserUpdateDTO;
import com.lishui.tourism.service.UserService;
import com.lishui.tourism.vo.user.LoginVO;
import com.lishui.tourism.vo.user.UserVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 用户控制器
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody UserRegisterDTO dto) {
        userService.register(dto);
        return Result.success();
    }
    
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody UserLoginDTO dto) {
        LoginVO loginVO = userService.login(dto);
        return Result.success(loginVO);
    }
    
    @Operation(summary = "退出登录")
    @RequireRole({Constants.Role.TOURIST, Constants.Role.GUIDE, Constants.Role.ADMIN})
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        userService.logout(token);
        return Result.success();
    }
    
    @Operation(summary = "获取当前用户信息")
    @RequireRole({Constants.Role.TOURIST, Constants.Role.GUIDE, Constants.Role.ADMIN})
    @GetMapping("/me")
    public Result<UserVO> getCurrentUser() {
        Long userId = UserContext.getUserId();
        UserVO userVO = userService.getCurrentUser(userId);
        return Result.success(userVO);
    }
    
    @Operation(summary = "更新用户信息")
    @RequireRole({Constants.Role.TOURIST, Constants.Role.GUIDE, Constants.Role.ADMIN})
    @PutMapping("/me")
    public Result<Void> updateUser(@Valid @RequestBody UserUpdateDTO dto) {
        Long userId = UserContext.getUserId();
        userService.updateUser(userId, dto);
        return Result.success();
    }
    
    @Operation(summary = "修改密码")
    @RequireRole({Constants.Role.TOURIST, Constants.Role.GUIDE, Constants.Role.ADMIN})
    @PostMapping("/change-password")
    public Result<Void> changePassword(@RequestParam String oldPassword, 
                                       @RequestParam String newPassword) {
        Long userId = UserContext.getUserId();
        userService.changePassword(userId, oldPassword, newPassword);
        return Result.success();
    }
    
    @Operation(summary = "用户列表（管理员）")
    @RequireRole({Constants.Role.ADMIN})
    @GetMapping("/list")
    public Result<IPage<UserVO>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role) {
        IPage<UserVO> userPage = userService.listUsers(page, size, keyword, role);
        return Result.success(userPage);
    }
    
    @Operation(summary = "启用/禁用用户（管理员）")
    @RequireRole({Constants.Role.ADMIN})
    @PutMapping("/{userId}/status")
    public Result<Void> toggleUserStatus(@PathVariable Long userId, 
                                         @RequestParam Integer status) {
        userService.toggleUserStatus(userId, status);
        return Result.success();
    }
    
    @Operation(summary = "分配角色（管理员）")
    @RequireRole({Constants.Role.ADMIN})
    @PutMapping("/{userId}/role")
    public Result<Void> assignRole(@PathVariable Long userId, 
                                   @RequestParam String role) {
        userService.assignRole(userId, role);
        return Result.success();
    }
    
    /**
     * 从请求中获取 Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(Constants.Jwt.HEADER_NAME);
        if (header != null && header.startsWith(Constants.Jwt.TOKEN_PREFIX)) {
            return header.substring(Constants.Jwt.TOKEN_PREFIX.length());
        }
        return null;
    }
}
