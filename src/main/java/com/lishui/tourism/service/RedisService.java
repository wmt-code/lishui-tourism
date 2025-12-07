package com.lishui.tourism.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Redis 服务
 */
@Service
@RequiredArgsConstructor
public class RedisService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 设置缓存
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    
    /**
     * 设置缓存并设置过期时间
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }
    
    /**
     * 获取缓存
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    
    /**
     * 获取缓存（泛型）
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Object value = get(key);
        return value == null ? null : (T) value;
    }
    
    /**
     * 删除缓存
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }
    
    /**
     * 批量删除缓存
     */
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }
    
    /**
     * 判断 key 是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
    
    /**
     * 设置过期时间
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }
    
    /**
     * 获取过期时间
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }
    
    /**
     * 列表添加（右侧）
     */
    public Long rightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }
    
    /**
     * 获取列表
     */
    public List<Object> range(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }
    
    /**
     * 自增
     */
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }
    
    /**
     * 自增指定值
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }
}
