package com.lishui.tourism.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    
    SUCCESS(0, "操作成功"),
    ERROR(500, "操作失败"),
    
    // 参数相关 1xxx
    PARAM_ERROR(1001, "参数错误"),
    PARAM_MISSING(1002, "参数缺失"),
    
    // 认证相关 2xxx
    UNAUTHORIZED(2001, "未认证"),
    TOKEN_INVALID(2002, "Token无效"),
    TOKEN_EXPIRED(2003, "Token已过期"),
    FORBIDDEN(2004, "无权限"),
    
    // 用户相关 3xxx
    USER_NOT_FOUND(3001, "用户不存在"),
    USER_ALREADY_EXISTS(3002, "用户已存在"),
    USERNAME_PASSWORD_ERROR(3003, "用户名或密码错误"),
    USER_DISABLED(3004, "用户已被禁用"),
    
    // 业务相关 4xxx
    DATA_NOT_FOUND(4001, "数据不存在"),
    DATA_ALREADY_EXISTS(4002, "数据已存在"),
    OPERATION_FAILED(4003, "操作失败"),
    
    // AI 相关 5xxx
    AI_SERVICE_ERROR(5001, "AI服务异常"),
    AI_MOCK_MODE(5002, "AI Mock模式");
    
    private final Integer code;
    private final String message;
}
