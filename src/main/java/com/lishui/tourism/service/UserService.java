package com.lishui.tourism.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lishui.tourism.common.constant.Constants;
import com.lishui.tourism.common.exception.BusinessException;
import com.lishui.tourism.common.result.ResultCode;
import com.lishui.tourism.config.security.JwtUtil;
import com.lishui.tourism.dto.user.UserLoginDTO;
import com.lishui.tourism.dto.user.UserRegisterDTO;
import com.lishui.tourism.dto.user.UserUpdateDTO;
import com.lishui.tourism.entity.User;
import com.lishui.tourism.mapper.UserMapper;
import com.lishui.tourism.vo.user.LoginVO;
import com.lishui.tourism.vo.user.UserVO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> {
    
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    
    /**
     * 用户注册
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegisterDTO dto) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS);
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(BCrypt.hashpw(dto.getPassword()));
        user.setNickname(dto.getNickname() != null ? dto.getNickname() : dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(Constants.Role.TOURIST);  // 默认游客
        user.setStatus(Constants.UserStatus.ENABLED);
        
        baseMapper.insert(user);
        log.info("用户注册成功：{}", dto.getUsername());
    }
    
    /**
     * 用户登录
     */
    public LoginVO login(UserLoginDTO dto) {
        // 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User user = baseMapper.selectOne(wrapper);
        
        if (user == null) {
            throw new BusinessException(ResultCode.USERNAME_PASSWORD_ERROR);
        }
        
        // 验证密码
        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USERNAME_PASSWORD_ERROR);
        }
        
        // 检查用户状态
        if (Constants.UserStatus.DISABLED.equals(user.getStatus())) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
        
        // 生成 Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        
        // 构造返回数据
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        
        log.info("用户登录成功：{}", dto.getUsername());
        return new LoginVO(token, userVO);
    }
    
    /**
     * 退出登录（将 Token 加入黑名单）
     */
    public void logout(String token) {
        redisService.set(
                Constants.RedisKey.TOKEN_BLACKLIST + token,
                true,
                7,
                java.util.concurrent.TimeUnit.DAYS
        );
        log.info("用户退出登录");
    }
    
    /**
     * 获取当前用户信息
     */
    public UserVO getCurrentUser(Long userId) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return BeanUtil.copyProperties(user, UserVO.class);
    }
    
    /**
     * 更新用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(Long userId, UserUpdateDTO dto) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        if (dto.getNickname() != null) {
            user.setNickname(dto.getNickname());
        }
        if (dto.getAvatar() != null) {
            user.setAvatar(dto.getAvatar());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
        
        baseMapper.updateById(user);
        log.info("用户信息更新成功：{}", userId);
    }
    
    /**
     * 修改密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        // 验证旧密码
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        
        // 更新密码
        user.setPassword(BCrypt.hashpw(newPassword));
        baseMapper.updateById(user);
        log.info("用户修改密码成功：{}", userId);
    }
    
    /**
     * 用户列表（管理员）
     */
    public IPage<UserVO> listUsers(int page, int size, String keyword, String role) {
        Page<User> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                    .or().like(User::getNickname, keyword));
        }
        
        if (role != null && !role.isEmpty()) {
            wrapper.eq(User::getRole, role);
        }
        
        wrapper.orderByDesc(User::getCreatedAt);
        
        IPage<User> userPage = baseMapper.selectPage(pageParam, wrapper);
        
        return userPage.convert(user -> BeanUtil.copyProperties(user, UserVO.class));
    }
    
    /**
     * 启用/禁用用户（管理员）
     */
    @Transactional(rollbackFor = Exception.class)
    public void toggleUserStatus(Long userId, Integer status) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        user.setStatus(status);
        baseMapper.updateById(user);
        log.info("用户状态修改成功：userId={}, status={}", userId, status);
    }
    
    /**
     * 分配角色（管理员）
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignRole(Long userId, String role) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        user.setRole(role);
        baseMapper.updateById(user);
        log.info("用户角色分配成功：userId={}, role={}", userId, role);
    }
}
