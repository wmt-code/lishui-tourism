import type { ApiResponse } from "@/utils/request";
import { defineStore } from "pinia";
import { computed, ref } from "vue";

// 用户信息接口
export interface UserInfo {
  id: number;
  username: string;
  nickname: string;
  avatar?: string;
  role: "ADMIN" | "GUIDE" | "TOURIST";
  email?: string;
  phone?: string;
}

// 登录请求参数
export interface LoginParams {
  username: string;
  password: string;
}

// 登录响应
export interface LoginResponse {
  token: string;
  user: UserInfo;
}

export const useUserStore = defineStore(
  "user",
  () => {
    // State
    const token = ref<string>("");
    const userInfo = ref<UserInfo | null>(null);

    // Getters
    const isLogin = computed(() => !!token.value);
    const isAdmin = computed(() => userInfo.value?.role === "ADMIN");
    const isGuide = computed(() => userInfo.value?.role === "GUIDE");
    const isTourist = computed(() => userInfo.value?.role === "TOURIST");
    const userName = computed(
      () => userInfo.value?.nickname || userInfo.value?.username || "游客"
    );

    // Actions
    /**
     * 登录
     */
    const login = async (params: LoginParams): Promise<void> => {
      try {
        // 实际项目中调用后端 API
        // const res = await request.post<LoginResponse>('/user/login', params)

        // Mock 数据模拟登录（500ms 延迟）
        const res = await new Promise<ApiResponse<LoginResponse>>((resolve) => {
          setTimeout(() => {
            // 根据用户名模拟不同角色
            let mockRole: "ADMIN" | "GUIDE" | "TOURIST" = "TOURIST";
            if (params.username === "admin") {
              mockRole = "ADMIN";
            } else if (params.username === "guide") {
              mockRole = "GUIDE";
            }

            resolve({
              code: 0,
              message: "success",
              data: {
                token: "mock-jwt-token-" + Date.now(),
                user: {
                  id: 1,
                  username: params.username,
                  nickname:
                    params.username === "admin"
                      ? "管理员"
                      : params.username === "guide"
                      ? "导游小李"
                      : "游客",
                  avatar:
                    "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
                  role: mockRole,
                  email: `${params.username}@example.com`,
                  phone: "13800138000",
                },
              },
            });
          }, 500);
        });

        if (res.code === 0 && res.data) {
          token.value = res.data.token;
          userInfo.value = res.data.user;
        }
      } catch (error) {
        console.error("Login failed:", error);
        throw error;
      }
    };

    /**
     * 注册
     */
    const register = async (params: any): Promise<void> => {
      try {
        // Mock 注册
        const res = await new Promise<ApiResponse<LoginResponse>>((resolve) => {
          setTimeout(() => {
            resolve({
              code: 0,
              message: "success",
              data: {
                token: "mock-jwt-token-" + Date.now(),
                user: {
                  id: Math.floor(Math.random() * 10000),
                  username: params.username,
                  nickname: params.nickname || params.username,
                  avatar:
                    "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
                  role: "TOURIST",
                  email: params.email,
                  phone: params.phone,
                },
              },
            });
          }, 500);
        });

        if (res.code === 0 && res.data) {
          token.value = res.data.token;
          userInfo.value = res.data.user;
        }
      } catch (error) {
        console.error("Register failed:", error);
        throw error;
      }
    };

    /**
     * 获取用户信息
     */
    const getUserInfo = async (): Promise<void> => {
      try {
        // const res = await request.get<UserInfo>('/user/info')

        // Mock 数据
        const res = await new Promise<ApiResponse<UserInfo>>((resolve) => {
          setTimeout(() => {
            resolve({
              code: 0,
              message: "success",
              data: userInfo.value || {
                id: 1,
                username: "user",
                nickname: "游客",
                role: "TOURIST",
              },
            });
          }, 300);
        });

        if (res.code === 0 && res.data) {
          userInfo.value = res.data;
        }
      } catch (error) {
        console.error("Get user info failed:", error);
      }
    };

    /**
     * 退出登录
     */
    const logout = (): void => {
      token.value = "";
      userInfo.value = null;
      // 清除 localStorage
      localStorage.removeItem("user");
    };

    return {
      // State
      token,
      userInfo,
      // Getters
      isLogin,
      isAdmin,
      isGuide,
      isTourist,
      userName,
      // Actions
      login,
      register,
      getUserInfo,
      logout,
    };
  },
  {
    // 持久化配置
    persist: {
      key: "user",
      storage: localStorage,
      paths: ["token", "userInfo"],
    },
  }
);
