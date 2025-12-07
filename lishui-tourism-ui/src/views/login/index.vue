<template>
  <div class="login-page">
    <!-- 左侧展示区 -->
    <div class="login-banner">
      <div class="banner-content">
        <h1 class="banner-title">丽水智慧旅游</h1>
        <p class="banner-subtitle">探索山水秘境，开启智慧之旅</p>
        <div class="banner-features">
          <div class="feature-item" v-for="(item, index) in features" :key="index">
            <el-icon :size="24" color="#F9A825"><component :is="item.icon" /></el-icon>
            <span>{{ item.text }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 右侧表单区 -->
    <div class="login-form-wrapper">
      <div class="login-form-container glass-card">
        <h2 class="form-title">{{ isLogin ? '欢迎回来' : '创建账号' }}</h2>
        <p class="form-subtitle">{{ isLogin ? '登录您的账号以继续' : '填写信息以注册新账号' }}</p>

        <!-- 登录表单 -->
        <el-form
          v-if="isLogin"
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="auth-form"
          @keyup.enter="handleLogin"
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              size="large"
              clearable
            >
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              size="large"
              show-password
            >
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" size="large" class="submit-btn" :loading="loading" @click="handleLogin">
              登录
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 注册表单 -->
        <el-form
          v-else
          ref="registerFormRef"
          :model="registerForm"
          :rules="registerRules"
          class="auth-form"
        >
          <el-form-item prop="username">
            <el-input
              v-model="registerForm.username"
              placeholder="请输入用户名"
              size="large"
              clearable
            >
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="nickname">
            <el-input
              v-model="registerForm.nickname"
              placeholder="请输入昵称"
              size="large"
              clearable
            >
              <template #prefix>
                <el-icon><Avatar /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="email">
            <el-input
              v-model="registerForm.email"
              placeholder="请输入邮箱"
              size="large"
              clearable
            >
              <template #prefix>
                <el-icon><Message /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="请输入密码"
              size="large"
              show-password
            >
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="请确认密码"
              size="large"
              show-password
            >
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" size="large" class="submit-btn" :loading="loading" @click="handleRegister">
              注册
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 切换登录/注册 -->
        <div class="form-footer">
          <span v-if="isLogin">还没有账号？</span>
          <span v-else>已有账号？</span>
          <a href="javascript:;" @click="toggleMode" class="toggle-link">
            {{ isLogin ? '立即注册' : '立即登录' }}
          </a>
        </div>

        <!-- 测试提示 -->
        <el-alert
          title="测试提示"
          type="info"
          :closable="false"
          class="mt-20"
        >
          <div style="font-size: 12px;">
            <p>管理员登录：admin / 任意密码</p>
            <p>导游登录：guide / 任意密码</p>
            <p>游客登录：任意用户名 / 任意密码</p>
          </div>
        </el-alert>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'
import { ElMessage, FormInstance, FormRules } from 'element-plus'
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 特性列表
const features = [
  { icon: 'Location', text: 'AI 智能推荐景点' },
  { icon: 'Guide', text: '一键生成旅行路线' },
  { icon: 'ChatDotRound', text: '景点智能问答' }
]

// 表单模式
const isLogin = ref(true)
const loading = ref(false)

// 登录表单
const loginFormRef = ref<FormInstance>()
const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// 注册表单
const registerFormRef = ref<FormInstance>()
const registerForm = reactive({
  username: '',
  nickname: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const registerRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 登录
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userStore.login(loginForm)
        ElMessage.success('登录成功')
        
        // 跳转
        const redirect = route.query.redirect as string
        if (userStore.isAdmin || userStore.isGuide) {
          router.push(redirect || '/admin')
        } else {
          router.push(redirect || '/')
        }
      } catch (error) {
        ElMessage.error('登录失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 注册
const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userStore.register(registerForm)
        ElMessage.success('注册成功')
        router.push('/')
      } catch (error) {
        ElMessage.error('注册失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 切换登录/注册模式
const toggleMode = () => {
  isLogin.value = !isLogin.value
  // 重置表单
  loginFormRef.value?.resetFields()
  registerFormRef.value?.resetFields()
}
</script>

<style scoped lang="scss">
.login-page {
  display: flex;
  height: 100vh;
  background: linear-gradient(135deg, #00796B 0%, #009688 100%);
}

/* 左侧展示区 */
.login-banner {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px;
  color: #fff;

  .banner-content {
    max-width: 500px;
  }

  .banner-title {
    font-size: 48px;
    font-weight: 700;
    margin-bottom: 16px;
    text-shadow: 0 2px 12px rgba(0, 0, 0, 0.2);
  }

  .banner-subtitle {
    font-size: 20px;
    margin-bottom: 48px;
    opacity: 0.95;
  }

  .banner-features {
    display: flex;
    flex-direction: column;
    gap: 24px;

    .feature-item {
      display: flex;
      align-items: center;
      gap: 12px;
      font-size: 16px;
      padding: 16px 24px;
      background: rgba(255, 255, 255, 0.1);
      backdrop-filter: blur(10px);
      border-radius: 12px;
      transition: all 0.3s;

      &:hover {
        background: rgba(255, 255, 255, 0.15);
        transform: translateX(8px);
      }
    }
  }
}

/* 右侧表单区 */
.login-form-wrapper {
  width: 500px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  background: var(--color-bg);
}

.login-form-container {
  width: 100%;
  padding: 40px;

  .form-title {
    text-align: center;
    margin-bottom: 8px;
    color: var(--color-text-heading);
  }

  .form-subtitle {
    text-align: center;
    margin-bottom: 32px;
    color: var(--color-text-body);
    font-size: 14px;
  }

  .auth-form {
    .submit-btn {
      width: 100%;
      height: 44px;
      font-size: 16px;
      font-weight: 500;
    }
  }

  .form-footer {
    text-align: center;
    margin-top: 20px;
    font-size: 14px;
    color: var(--color-text-body);

    .toggle-link {
      color: var(--color-primary);
      font-weight: 500;
      margin-left: 4px;

      &:hover {
        text-decoration: underline;
      }
    }
  }
}
</style>
