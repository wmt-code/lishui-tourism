package com.lishui.tourism.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户注册 DTO
 */
@Data
public class UserRegisterDTO {
    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    private String password;
    
    private String nickname;
    
    private String email;
    
    private String phone;
}
