# 🚀 快速启动指南

## 第一次运行项目（必读）

### 1. 安装依赖

```bash
cd lishui-tourism-ui
npm install
```

如果网络较慢，推荐使用国内镜像：

```bash
npm install --registry=https://registry.npmmirror.com
```

或使用 pnpm（更快）：

```bash
npm install -g pnpm
pnpm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

浏览器自动打开 `http://localhost:5173`

### 3. 测试登录

**管理员账号:**
- 用户名: `admin`
- 密码: 任意（例如：`123456`）
- 登录后自动跳转到管理后台

**导游账号:**
- 用户名: `guide`
- 密码: 任意
- 可访问部分管理功能

**游客账号:**
- 用户名: 任意（例如：`user`）
- 密码: 任意
- 访问游客端功能

## ⚠️ 重要提示

### TypeScript 错误说明

在未安装依赖前，IDE 会显示大量 TypeScript 错误：
```
找不到模块"vue"或其相应的类型声明
找不到模块"vue-router"或其相应的类型声明
```

**这是正常现象！** 执行 `npm install` 后这些错误会自动消失。

### 当前数据模式

项目目前使用 **Mock 数据模式**，所有 API 调用都是模拟的（延迟 500ms）。

- ✅ 可以直接预览前端效果
- ✅ 无需启动后端服务
- ✅ 登录、数据加载等功能正常工作

### 连接真实后端

1. 确保后端服务运行在 `http://localhost:8081`

2. 修改对应文件的 API 调用（参见 README.md "API对接" 章节）

3. Axios 已配置好拦截器，会自动：
   - 携带 Token
   - 处理 401 错误
   - 显示错误提示

## 📁 核心文件说明

### 必看文件

| 文件 | 作用 | 为什么重要 |
|------|------|-----------|
| `src/router/index.ts` | 路由配置 | 了解页面结构和权限控制 |
| `src/stores/user.ts` | 用户状态管理 | 登录逻辑和 Mock 数据 |
| `src/utils/request.ts` | Axios 封装 | HTTP 请求统一处理 |
| `src/views/login/index.vue` | 登录页 | 精美的登录/注册界面 |
| `src/views/portal/AiTrip.vue` | AI行程规划 | ⭐ 核心功能展示 |
| `src/views/admin/scenic/index.vue` | 景点管理 | ⭐ CRUD 标准模板 |

### 布局文件

- `src/layout/PortalLayout.vue` - 游客端布局（顶部导航 + 内容 + 底部）
- `src/layout/AdminLayout.vue` - 管理端布局（侧边栏 + 顶部 + 内容）

## 🎨 快速体验功能

### 1. 游客端

访问路径:
- `/home` - 首页（Banner + 热门景点 + 公告）
- `/ai-trip` - AI 行程规划（⭐ 必看！）

操作步骤:
1. 点击"AI行程"菜单
2. 填写表单（天数、预算、偏好、季节）
3. 点击"生成行程"
4. 查看时间线展示的完整行程

### 2. 管理端

访问路径:
- `/admin/dashboard` - 数据看板
- `/admin/scenic` - 景点管理（⭐ 必看！）

操作步骤:
1. 使用 `admin` 登录
2. 查看数据看板的统计卡片
3. 进入"景点管理"
4. 尝试：搜索、新增、编辑、删除功能

## 🔧 常用命令

```bash
# 开发模式
npm run dev

# 构建生产版本
npm run build

# 预览生产版本
npm run preview

# TypeScript 类型检查
vue-tsc
```

## 💡 开发技巧

### 1. 热重载

Vite 支持热模块替换 (HMR)，修改代码后页面自动刷新，无需手动刷新浏览器。

### 2. Vue DevTools

推荐安装浏览器插件 "Vue.js devtools"，可以查看：
- 组件树
- Pinia 状态
- 路由信息

### 3. Element Plus 组件文档

遇到组件问题，参考官方文档：
https://element-plus.org/zh-CN/

## 🐛 遇到问题？

### 端口被占用

```bash
# 错误: Port 5173 is already in use
```

**解决**: 修改 `vite.config.ts`:

```typescript
server: {
  port: 5174, // 改成其他端口
  ...
}
```

### 依赖安装失败

```bash
# 清除缓存后重新安装
npm cache clean --force
npm install
```

### TypeScript 报错不消失

```bash
# 重启 VSCode
# 或执行
npm run dev
```

## 📖 下一步

1. ✅ 查看 `README.md` 了解完整项目架构
2. ✅ 阅读核心代码文件（上面列出的必看文件）
3. ✅ 根据 `views/admin/scenic/index.vue` 实现其他管理页面
4. ✅ 连接真实后端 API
5. ✅ 扩展功能（富文本、图表、地图等）

## 🎯 项目亮点

- ✨ **Vue 3 最佳实践**: Composition API + `<script setup>`
- 🎨 **"丽水之雾"设计语言**: 磨砂玻璃 + 大圆角卡片
- 🔐 **完善的权限系统**: 路由守卫 + 角色控制
- 📦 **状态持久化**: 刷新页面不丢失登录状态
- 🚀 **开箱即用**: Mock 数据 + 完整示例代码

---

**祝开发愉快！** 🎉

如有问题，请查看 `README.md` 或提交 Issue。
