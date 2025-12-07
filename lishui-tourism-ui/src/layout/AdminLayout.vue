<template>
  <div class="admin-layout">
    <!-- 左侧侧边栏 -->
    <aside class="admin-sidebar">
      <div class="sidebar-logo">
        <el-icon :size="32" color="#00796B"><Setting /></el-icon>
        <span>管理后台</span>
      </div>

      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        router
        unique-opened
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据看板</span>
        </el-menu-item>

        <el-menu-item index="/admin/scenic" v-if="userStore.isAdmin">
          <el-icon><Location /></el-icon>
          <span>景点管理</span>
        </el-menu-item>

        <el-menu-item index="/admin/route">
          <el-icon><Guide /></el-icon>
          <span>路线管理</span>
        </el-menu-item>

        <el-menu-item index="/admin/user" v-if="userStore.isAdmin">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>

        <el-menu-item index="/admin/comment" v-if="userStore.isAdmin">
          <el-icon><ChatDotRound /></el-icon>
          <span>评论管理</span>
        </el-menu-item>
      </el-menu>
    </aside>

    <!-- 右侧主体内容 -->
    <div class="admin-main">
      <!-- 顶部栏 -->
      <header class="admin-header">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
        </el-breadcrumb>

        <div class="header-actions">
          <el-button text @click="backToPortal">
            <el-icon><HomeFilled /></el-icon>
            返回前台
          </el-button>

          <el-dropdown>
            <div class="user-info">
              <el-avatar :size="32" :src="userStore.userInfo?.avatar" />
              <span class="user-name">{{ userStore.userName }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <!-- 内容区 -->
      <main class="admin-content">
        <router-view v-slot="{ Component }">
          <transition name="slide-fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 当前激活的菜单
const activeMenu = computed(() => route.path)

// 当前页面标题
const currentTitle = computed(() => route.meta.title || '管理后台')

// 返回前台
const backToPortal = () => {
  router.push('/')
}

// 退出登录
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logout()
    router.push('/login')
  }).catch(() => {})
}
</script>

<style scoped lang="scss">
.admin-layout {
  display: flex;
  height: 100vh;
  background: var(--color-bg);
}

/* 左侧侧边栏 */
.admin-sidebar {
  width: 240px;
  background: #fff;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;

  .sidebar-logo {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    height: 64px;
    border-bottom: 1px solid var(--color-border);
    font-size: 18px;
    font-weight: 600;
    color: var(--color-primary);
  }

  .sidebar-menu {
    flex: 1;
    border: none;
    padding: 16px 0;

    .el-menu-item {
      height: 48px;
      line-height: 48px;
      margin: 4px 12px;
      border-radius: 8px;

      &:hover {
        background: rgba(0, 121, 107, 0.05);
      }

      &.is-active {
        background: rgba(0, 121, 107, 0.1);
        color: var(--color-primary);
      }
    }
  }
}

/* 右侧主体 */
.admin-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* 顶部栏 */
.admin-header {
  height: 64px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;

  .header-actions {
    display: flex;
    align-items: center;
    gap: 24px;

    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;
      padding: 4px 12px;
      border-radius: 20px;
      transition: background 0.3s;

      &:hover {
        background: rgba(0, 121, 107, 0.05);
      }

      .user-name {
        font-size: 14px;
        color: var(--color-text-heading);
      }
    }
  }
}

/* 内容区 */
.admin-content {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
}
</style>
