import { useUserStore } from "@/stores/user";
import { ElMessage } from "element-plus";
import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

// 路由配置
const routes: RouteRecordRaw[] = [
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/login/index.vue"),
    meta: { title: "登录" },
  },
  // Portal Layout - 游客端
  {
    path: "/",
    component: () => import("@/layout/PortalLayout.vue"),
    redirect: "/home",
    children: [
      {
        path: "home",
        name: "Home",
        component: () => import("@/views/portal/Home.vue"),
        meta: { title: "首页" },
      },
      {
        path: "scenic",
        name: "ScenicList",
        component: () => import("@/views/portal/ScenicList.vue"),
        meta: { title: "景点列表" },
      },
      {
        path: "scenic/:id",
        name: "ScenicDetail",
        component: () => import("@/views/portal/ScenicDetail.vue"),
        meta: { title: "景点详情" },
      },
      {
        path: "ai-trip",
        name: "AiTrip",
        component: () => import("@/views/portal/AiTrip.vue"),
        meta: { title: "AI行程规划" },
      },
      {
        path: "user-center",
        name: "UserCenter",
        component: () => import("@/views/portal/UserCenter.vue"),
        meta: { title: "个人中心", requiresAuth: true },
      },
    ],
  },
  // Admin Layout - 管理端
  {
    path: "/admin",
    component: () => import("@/layout/AdminLayout.vue"),
    redirect: "/admin/dashboard",
    meta: { requiresAuth: true, roles: ["ADMIN", "GUIDE"] },
    children: [
      {
        path: "dashboard",
        name: "Dashboard",
        component: () => import("@/views/admin/Dashboard.vue"),
        meta: { title: "数据看板" },
      },
      {
        path: "scenic",
        name: "AdminScenic",
        component: () => import("@/views/admin/scenic/index.vue"),
        meta: { title: "景点管理", roles: ["ADMIN"] },
      },
      {
        path: "user",
        name: "AdminUser",
        component: () => import("@/views/admin/user/index.vue"),
        meta: { title: "用户管理", roles: ["ADMIN"] },
      },
      {
        path: "route",
        name: "AdminRoute",
        component: () => import("@/views/admin/route/index.vue"),
        meta: { title: "路线管理", roles: ["ADMIN", "GUIDE"] },
      },
      {
        path: "comment",
        name: "AdminComment",
        component: () => import("@/views/admin/comment/index.vue"),
        meta: { title: "评论管理", roles: ["ADMIN"] },
      },
    ],
  },
  // 404
  {
    path: "/:pathMatch(.*)*",
    name: "NotFound",
    component: () => import("@/views/error/404.vue"),
    meta: { title: "页面不存在" },
  },
];

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 };
  },
});

// 全局前置守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore();

  // 设置页面标题
  document.title = `${to.meta.title || "丽水智慧旅游"} - 丽水智慧旅游管理系统`;

  // 检查是否需要登录
  if (to.meta.requiresAuth && !userStore.isLogin) {
    ElMessage.warning("请先登录");
    next({ path: "/login", query: { redirect: to.fullPath } });
    return;
  }

  // 检查角色权限
  if (to.meta.roles && Array.isArray(to.meta.roles)) {
    const userRole = userStore.userInfo?.role;
    if (!userRole || !to.meta.roles.includes(userRole)) {
      ElMessage.error("没有权限访问该页面");
      next({ path: "/" });
      return;
    }
  }

  // 已登录用户访问登录页，重定向到首页
  if (to.path === "/login" && userStore.isLogin) {
    next({ path: "/" });
    return;
  }

  next();
});

// 扩展路由元信息类型
declare module "vue-router" {
  interface RouteMeta {
    title?: string;
    requiresAuth?: boolean;
    roles?: string[];
  }
}

export default router;
