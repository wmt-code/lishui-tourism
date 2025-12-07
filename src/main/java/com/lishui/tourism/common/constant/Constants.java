package com.lishui.tourism.common.constant;

/**
 * 系统常量
 */
public class Constants {
    
    /**
     * 用户角色
     */
    public static class Role {
        public static final String TOURIST = "TOURIST";  // 游客
        public static final String GUIDE = "GUIDE";      // 导游
        public static final String ADMIN = "ADMIN";      // 管理员
    }
    
    /**
     * 用户状态
     */
    public static class UserStatus {
        public static final Integer DISABLED = 0;  // 禁用
        public static final Integer ENABLED = 1;   // 启用
    }
    
    /**
     * 通用状态
     */
    public static class Status {
        public static final Integer OFFLINE = 0;  // 下架/隐藏
        public static final Integer ONLINE = 1;   // 上架/显示
    }
    
    /**
     * 收藏类型
     */
    public static class FavoriteType {
        public static final String SPOT = "SPOT";              // 景点
        public static final String DESTINATION = "DESTINATION"; // 目的地
        public static final String NEWS = "NEWS";              // 资讯
    }
    
    /**
     * Redis Key 前缀
     */
    public static class RedisKey {
        public static final String TOKEN_BLACKLIST = "token:blacklist:";     // Token黑名单
        public static final String HOT_SPOTS = "hot:spots";                  // 热门景点
        public static final String HOT_DESTINATIONS = "hot:destinations";    // 热门目的地
        public static final String SPOT_INFO = "spot:info:";                 // 景点信息
    }
    
    /**
     * JWT 相关
     */
    public static class Jwt {
        public static final String HEADER_NAME = "Authorization";
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final String USER_ID = "userId";
        public static final String USERNAME = "username";
        public static final String ROLE = "role";
    }
    
    /**
     * 分页默认值
     */
    public static class Page {
        public static final Integer DEFAULT_PAGE = 1;
        public static final Integer DEFAULT_SIZE = 10;
        public static final Integer MAX_SIZE = 100;
    }
}
