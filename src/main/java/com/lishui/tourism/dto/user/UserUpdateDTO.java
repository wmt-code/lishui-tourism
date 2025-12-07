package com.lishui.tourism.dto.user;

import lombok.Data;

/**
 * 用户更新 DTO
 */
@Data
public class UserUpdateDTO {
    
    private String nickname;
    
    private String avatar;
    
    private String email;
    
    private String phone;
}
