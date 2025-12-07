<template>
  <div class="portal-layout">
    <!-- 顶部导航栏 - 磨砂玻璃效果 -->
    <header class="portal-header glass-effect">
      <div class="container header-content">
        <!-- Logo -->
        <div class="logo">
          <el-icon :size="28" color="#00796B"><Location /></el-icon>
          <span class="logo-text">丽水智慧旅游</span>
        </div>

        <!-- 导航菜单 -->
        <nav class="nav-menu">
          <router-link to="/home" class="nav-item" active-class="active">首页</router-link>
          <router-link to="/scenic" class="nav-item" active-class="active">景点</router-link>
          <router-link to="/ai-trip" class="nav-item" active-class="active">AI行程</router-link>
        </nav>

        <!-- 用户操作区 -->
        <div class="user-actions">
          <template v-if="userStore.isLogin">
            <el-dropdown>
              <div class="user-info">
                <el-avatar :size="36" :src="userStore.userInfo?.avatar" />
                <span class="user-name">{{ userStore.userName }}</span>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleUserCenter">
                    <el-icon><User /></el-icon>
                    个人中心
                  </el-dropdown-item>
                  <el-dropdown-item v-if="userStore.isAdmin || userStore.isGuide" @click="handleAdmin">
                    <el-icon><Setting /></el-icon>
                    管理后台
                  </el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">
                    <el-icon><SwitchButton /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" @click="handleLogin">登录/注册</el-button>
          </template>
        </div>
      </div>
    </header>

    <!-- 主体内容区 -->
    <main class="portal-main">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <!-- 底部 Footer -->
    <footer class="portal-footer">
      <div class="container footer-content">
        <div class="footer-info">
          <h3>丽水智慧旅游</h3>
          <p>探索丽水山水，体验智慧旅行</p>
          <p class="copyright">© 2025 丽水智慧旅游管理系统. All rights reserved.</p>
        </div>
        <div class="footer-links">
          <div class="link-group">
            <h4>关于我们</h4>
            <a href="#">公司简介</a>
            <a href="#">联系方式</a>
            <a href="#">加入我们</a>
          </div>
          <div class="link-group">
            <h4>帮助中心</h4>
            <a href="#">常见问题</a>
            <a href="#">用户协议</a>
            <a href="#">隐私政策</a>
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()
const userStore = useUserStore()

const handleLogin = () => {
  router.push('/login')
}

const handleUserCenter = () => {
  router.push('/user-center')
}

const handleAdmin = () => {
  router.push('/admin')
}

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logout()
    router.push('/')
  }).catch(() => {})
}
</script>

<style scoped lang="scss">
.portal-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* 顶部导航栏 */
.portal-header {
  position: sticky;
  top: 0;
  z-index: 999;
  height: 64px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);

  .header-content {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 100%;
  }

  .logo {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 20px;
    font-weight: 600;
    color: var(--color-primary);
    cursor: pointer;

    .logo-text {
      background: linear-gradient(135deg, #00796B, #009688);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }
  }

  .nav-menu {
    display: flex;
    gap: 32px;

    .nav-item {
      position: relative;
      padding: 20px 0;
      color: var(--color-text-body);
      font-size: 16px;
      font-weight: 500;
      transition: color 0.3s;

      &:hover {
        color: var(--color-primary);
      }

      &.active {
        color: var(--color-primary);

        &::after {
          content: '';
          position: absolute;
          bottom: 0;
          left: 0;
          right: 0;
          height: 3px;
          background: var(--color-primary);
          border-radius: 2px;
        }
      }
    }
  }

  .user-actions {
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

/* 主体内容区 */
.portal-main {
  flex: 1;
  background: var(--color-bg);
}

/* 底部 Footer */
.portal-footer {
  background: #2C3E50;
  color: #ecf0f1;
  padding: 40px 0 20px;

  .footer-content {
    display: flex;
    justify-content: space-between;
  }

  .footer-info {
    h3 {
      color: #fff;
      margin-bottom: 12px;
    }

    p {
      margin: 8px 0;
      color: #bdc3c7;
      font-size: 14px;
    }

    .copyright {
      margin-top: 20px;
      font-size: 12px;
    }
  }

  .footer-links {
    display: flex;
    gap: 80px;

    .link-group {
      h4 {
        color: #fff;
        margin-bottom: 16px;
        font-size: 16px;
      }

      a {
        display: block;
        margin: 8px 0;
        color: #bdc3c7;
        font-size: 14px;
        transition: color 0.3s;

        &:hover {
          color: var(--color-secondary);
        }
      }
    }
  }
}
</style>
