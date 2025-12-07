package com.lishui.tourism.config.security;

import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lishui.tourism.common.constant.Constants;
import com.lishui.tourism.common.context.UserContext;
import com.lishui.tourism.common.exception.BusinessException;
import com.lishui.tourism.common.result.ResultCode;
import com.lishui.tourism.service.RedisService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT 认证拦截器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 非方法不拦截
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        
        // 获取 Token
        String token = getTokenFromRequest(request);
        
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
        
        // 如果方法没有 @RequireRole 注解，不需要认证
        if (requireRole == null) {
            requireRole = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
        }
        
        // 没有注解，放行
        if (requireRole == null) {
            return true;
        }
        
        // 需要认证但没有 Token
        if (token == null || token.isEmpty()) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        
        // 验证 Token
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }
        
        // 检查 Token 是否在黑名单中
        if (redisService.hasKey(Constants.RedisKey.TOKEN_BLACKLIST + token)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }
        
        // 获取用户信息
        Long userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getUsernameFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);
        
        // 检查角色权限
        String[] requiredRoles = requireRole.value();
        if (!Arrays.asList(requiredRoles).contains(role)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        
        // 设置用户上下文
        UserContext.setUserId(userId);
        UserContext.setUsername(username);
        UserContext.setRole(role);
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理用户上下文
        UserContext.clear();
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
