package com.lishui.tourism.vo.user;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 用户信息 VO
 */
@Data
public class UserVO {
    
    private Long id;
    
    private String username;
    
    private String nickname;
    
    private String avatar;
    
    private String email;
    
    private String phone;
    
    private String role;
    
    private Integer status;
    
    private LocalDateTime createdAt;
}
