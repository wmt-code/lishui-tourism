# 丽水智慧旅游管理系统 - 前端项目

> 基于 Vue 3 + TypeScript + Element Plus 构建的现代化旅游管理系统前端

## 📦 技术栈

- **核心框架**: Vue 3.4+ (Composition API + `<script setup>` 语法糖)
- **开发语言**: TypeScript 5.x
- **构建工具**: Vite 5.x
- **UI 组件库**: Element Plus 2.6+
- **状态管理**: Pinia 2.1+ (持久化支持)
- **路由管理**: Vue Router 4.x
- **HTTP 客户端**: Axios 1.6+
- **CSS 预处理**: SCSS/SASS
- **图标库**: Element Plus Icons

## 🎨 设计语言 - "丽水之雾"

### 配色方案
- **主色**: `#00796B` (瓯江黛绿) - 导航栏、按钮、激活状态
- **次色**: `#F9A825` (梯田金) - 推荐、收藏、高亮标签
- **背景**: `#F5F7FA` (冷灰白) - 整体背景
- **文字**: `#2C3E50` (标题), `#606266` (正文)

### 视觉特性
- ✨ **磨砂玻璃效果**: 导航栏和 AI 对话框使用 `backdrop-filter: blur(10px)`
- 🎴 **大圆角卡片**: `border-radius: 12px`
- 🌊 **柔和阴影**: `box-shadow: 0 4px 12px rgba(0, 121, 107, 0.1)`

## 🏗️ 项目架构

### 双布局策略 (SPA 单页应用)

#### 1. PortalLayout (游客端)
```
顶部导航栏 (Logo + 菜单 + 登录/个人中心)
    ↓
  中间内容区 (流式布局)
    ↓
  底部 Footer
```

#### 2. AdminLayout (管理端)
```
左侧侧边栏 (el-menu)  |  顶部面包屑/用户信息栏
                       |        ↓
                       |  中间内容区 (el-main)
```

## 📁 目录结构

```
lishui-tourism-ui/
├── src/
│   ├── layout/               # 布局组件
│   │   ├── PortalLayout.vue  # 游客端布局
│   │   └── AdminLayout.vue   # 管理端布局
│   ├── views/                # 页面视图
│   │   ├── login/            # 登录/注册
│   │   ├── portal/           # 游客端页面
│   │   │   ├── Home.vue      # 首页
│   │   │   ├── AiTrip.vue    # AI行程规划 ⭐
│   │   │   └── ...
│   │   ├── admin/            # 管理端页面
│   │   │   ├── Dashboard.vue # 数据看板
│   │   │   ├── scenic/       # 景点管理 ⭐
│   │   │   └── ...
│   │   └── error/            # 错误页面
│   ├── stores/               # Pinia状态管理
│   │   └── user.ts           # 用户状态 (登录/注销/权限)
│   ├── router/               # 路由配置
│   │   └── index.ts          # 路由守卫 + 权限控制
│   ├── utils/                # 工具函数
│   │   └── request.ts        # Axios封装 (拦截器)
│   └── style/                # 全局样式
│       ├── main.scss         # 主样式文件
│       └── element-var.scss  # Element Plus 主题定制
├── .env                      # 环境变量
├── vite.config.ts            # Vite 配置
├── tsconfig.json             # TypeScript 配置
└── package.json
```

## 🚀 快速开始

### 1. 安装依赖

```bash
cd lishui-tourism-ui
npm install
# 或使用 pnpm (推荐)
pnpm install
```

### 2. 启动开发服务器

```bash
npm run dev
# 或
pnpm dev
```

访问: `http://localhost:5173`

### 3. 构建生产版本

```bash
npm run build
# 或
pnpm build
```

## 🔑 功能模块

### 核心页面

#### ✅ 已实现（完整代码）

| 页面 | 路径 | 描述 | 亮点 |
|------|------|------|------|
| **登录/注册** | `/login` | 左右分栏设计 | 支持测试账号：admin/guide/任意用户名 |
| **游客首页** | `/home` | Banner轮播 + 热门景点 + 公告 | 卡片式布局 + Mock数据 |
| **AI行程规划** | `/ai-trip` | 左侧表单 + 右侧时间线结果 | ⭐ 核心功能，完整UI实现 |
| **景点管理** | `/admin/scenic` | 完整 CRUD 示例 | ⭐ 管理后台标准模板 |
| **数据看板** | `/admin/dashboard` | 统计卡片 | 可扩展图表集成 |

#### 🔨 待扩展（占位页面）

- `/scenic` - 景点列表
- `/scenic/:id` - 景点详情
- `/user-center` - 个人中心
- `/admin/user` - 用户管理
- `/admin/route` - 路线管理
- `/admin/comment` - 评论管理

> **提示**: 参照 `views/admin/scenic/index.vue` 实现其他 CRUD 页面

### API 对接

#### 当前状态: Mock 数据模式

所有 API 调用已使用 `Promise.resolve` + 500ms 延迟模拟：

```typescript
// 示例：登录 (src/stores/user.ts)
const res = await new Promise<ApiResponse<LoginResponse>>((resolve) => {
  setTimeout(() => {
    resolve({
      code: 0,
      message: 'success',
      data: { token: '...', user: {...} }
    })
  }, 500)
})
```

#### 切换到真实后端

1. 确保后端服务运行在 `http://localhost:8081/api`
2. 修改对应文件，取消注释真实 API 调用：

```typescript
// 取消注释这行
const res = await request.post<LoginResponse>('/user/login', params)

// 注释或删除 Mock 代码
// const res = await new Promise(...)
```

3. Axios 已自动配置拦截器，支持：
   - ✅ 自动携带 Token (`Authorization: Bearer <token>`)
   - ✅ 401 自动跳转登录
   - ✅ 统一错误提示

## 🔐 权限控制

### 路由守卫 (router/index.ts)

```typescript
router.beforeEach((to, from, next) => {
  // 1. 检查是否需要登录
  if (to.meta.requiresAuth && !userStore.isLogin) {
    next('/login')
  }
  
  // 2. 检查角色权限
  if (to.meta.roles && !to.meta.roles.includes(userRole)) {
    next('/')
  }
  
  next()
})
```

### 角色类型

- `ADMIN` - 管理员 (全部权限)
- `GUIDE` - 导游 (部分管理权限)
- `TOURIST` - 游客 (基础浏览权限)

### 测试账号

```
用户名: admin   角色: ADMIN
用户名: guide   角色: GUIDE
其他任意用户名  角色: TOURIST
密码: 任意
```

## 📦 Pinia 状态持久化

使用 `pinia-plugin-persistedstate` 实现状态持久化：

```typescript
// stores/user.ts
export const useUserStore = defineStore('user', () => {
  // ...
}, {
  persist: {
    key: 'user',
    storage: localStorage,
    paths: ['token', 'userInfo']
  }
})
```

刷新页面后自动恢复登录状态。

## 🎯 核心代码示例

### 1. Axios 请求封装 (utils/request.ts)

```typescript
import axios from 'axios'
import { useUserStore } from '@/stores/user'
import router from '@/router'

const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 15000
})

// 请求拦截器 - 自动携带 Token
service.interceptors.request.use(config => {
  const userStore = useUserStore()
  if (userStore.token) {
    config.headers.Authorization = `Bearer ${userStore.token}`
  }
  return config
})

// 响应拦截器 - 统一错误处理
service.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response?.status === 401) {
      userStore.logout()
      router.push('/login')
    }
    return Promise.reject(error)
  }
)
```

### 2. Composition API 最佳实践

```vue
<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

// 响应式状态
const loading = ref(false)
const form = reactive({ name: '', email: '' })

// 计算属性
const isValid = computed(() => form.name && form.email)

// 方法
const handleSubmit = async () => {
  loading.value = true
  try {
    // API 调用
    await request.post('/api/submit', form)
  } finally {
    loading.value = false
  }
}

// 生命周期
onMounted(() => {
  // 初始化逻辑
})
</script>
```

## 🔧 环境变量

`.env` 文件配置:

```env
VITE_API_BASE_URL=http://localhost:8081/api
```

代码中访问:

```typescript
import.meta.env.VITE_API_BASE_URL
```

## 🎨 样式定制

### 全局CSS变量 (style/main.scss)

```scss
:root {
  --color-primary: #00796B;
  --color-secondary: #F9A825;
  --color-bg: #F5F7FA;
  --color-text-heading: #2C3E50;
  --color-text-body: #606266;
  --border-radius-card: 12px;
  --shadow-card: 0 4px 12px rgba(0, 121, 107, 0.1);
}
```

### Element Plus 主题定制 (style/element-var.scss)

```scss
@forward 'element-plus/theme-chalk/src/common/var.scss' with (
  $colors: (
    'primary': ('base': #00796B)
  ),
  $button-radius: 8px
);
```

## 📝 开发规范

### 1. 组件命名

- **PascalCase**: 组件文件名和组件名
- **kebab-case**: CSS 类名

### 2. 代码风格

- 使用 `<script setup>` 语法糖
- 优先使用 Composition API
- TypeScript 严格模式

### 3. Git 提交

```bash
git commit -m "feat: 添加AI行程规划功能"
git commit -m "fix: 修复登录状态持久化问题"
git commit -m "style: 优化卡片阴影效果"
```

## 🐛 常见问题

### 1. TypeScript 报错 "找不到模块"

**原因**: npm 依赖未安装

**解决**: `npm install` 或 `pnpm install`

### 2. Vite 代理不生效

**检查**: `vite.config.ts` 中的 `proxy` 配置

```typescript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8081',
      changeOrigin: true
    }
  }
}
```

### 3. Element Plus 主题未生效

**检查**: `vite.config.ts` 中的 SCSS 配置

```typescript
css: {
  preprocessorOptions: {
    scss: {
      additionalData: `@use "@/style/element-var.scss" as *;`
    }
  }
}
```

## 📚 扩展建议

### 1. 集成富文本编辑器

推荐: `@wangeditor/editor-for-vue`

### 2. 集成图表库

推荐: `echarts` + `vue-echarts`

### 3. 集成地图

推荐: 高德地图 Web API

### 4. 图片上传

使用 `el-upload` + OSS 存储

## 🤝 贡献指南

1. Fork 本项目
2. 创建功能分支: `git checkout -b feature/新功能`
3. 提交更改: `git commit -m 'feat: 添加某功能'`
4. 推送分支: `git push origin feature/新功能`
5. 提交 Pull Request

## 📄 License

MIT License

---

**开发者**: 资深前端架构师  
**最后更新**: 2025-12-07  
**项目状态**: ✅ 核心功能已实现，可直接运行
