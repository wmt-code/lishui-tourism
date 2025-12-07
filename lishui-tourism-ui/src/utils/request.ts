import router from "@/router";
import { useUserStore } from "@/stores/user";
import axios, {
  AxiosError,
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
} from "axios";
import { ElMessage } from "element-plus";

// 响应数据接口
export interface ApiResponse<T = any> {
  code: number;
  message: string;
  data: T;
}

// 创建 Axios 实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "/api",
  timeout: 15000,
  headers: {
    "Content-Type": "application/json;charset=utf-8",
  },
});

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    const userStore = useUserStore();

    // 自动携带 Token
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`;
    }

    return config;
  },
  (error: AxiosError) => {
    console.error("Request Error:", error);
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    const res = response.data;

    // code 为 0 表示成功
    if (res.code === 0) {
      return res;
    }

    // 其他错误码统一处理
    ElMessage.error(res.message || "请求失败");
    return Promise.reject(new Error(res.message || "Error"));
  },
  (error: AxiosError<ApiResponse>) => {
    console.error("Response Error:", error);

    // 处理 HTTP 状态码错误
    if (error.response) {
      const { status, data } = error.response;

      switch (status) {
        case 401:
          // Token 过期或未授权
          ElMessage.error("登录已过期，请重新登录");
          const userStore = useUserStore();
          userStore.logout();
          router.push("/login");
          break;
        case 403:
          ElMessage.error("没有权限访问该资源");
          break;
        case 404:
          ElMessage.error("请求的资源不存在");
          break;
        case 500:
          ElMessage.error(data?.message || "服务器内部错误");
          break;
        default:
          ElMessage.error(data?.message || `请求失败(${status})`);
      }
    } else if (error.request) {
      // 请求已发送但没有收到响应
      ElMessage.error("网络连接失败，请检查网络");
    } else {
      // 其他错误
      ElMessage.error(error.message || "请求配置错误");
    }

    return Promise.reject(error);
  }
);

// 封装请求方法
export const request = {
  get<T = any>(
    url: string,
    config?: AxiosRequestConfig
  ): Promise<ApiResponse<T>> {
    return service.get(url, config);
  },

  post<T = any>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig
  ): Promise<ApiResponse<T>> {
    return service.post(url, data, config);
  },

  put<T = any>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig
  ): Promise<ApiResponse<T>> {
    return service.put(url, data, config);
  },

  delete<T = any>(
    url: string,
    config?: AxiosRequestConfig
  ): Promise<ApiResponse<T>> {
    return service.delete(url, config);
  },
};

export default service;
