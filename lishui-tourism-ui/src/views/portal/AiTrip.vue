<template>
  <div class="ai-trip-page">
    <div class="container">
      <div class="page-header">
        <h1 class="page-title">AI 智能行程规划</h1>
        <p class="page-subtitle">告诉我们您的需求，让AI为您定制专属旅行路线</p>
      </div>

      <div class="trip-planner">
        <!-- 左侧表单 -->
        <div class="planner-form glass-card">
          <h3 class="form-title">
            <el-icon><MagicStick /></el-icon>
            填写您的需求
          </h3>

          <el-form :model="planForm" :rules="planRules" ref="planFormRef" label-position="top">
            <el-form-item label="旅行天数" prop="days">
              <el-input-number v-model="planForm.days" :min="1" :max="7" style="width: 100%" />
            </el-form-item>

            <el-form-item label="预算（元）" prop="budget">
              <el-input-number v-model="planForm.budget" :min="100" :step="100" style="width: 100%" />
            </el-form-item>

            <el-form-item label="旅行偏好" prop="preference">
              <el-select v-model="planForm.preference" placeholder="请选择" style="width: 100%">
                <el-option label="自然风光" value="nature" />
                <el-option label="人文历史" value="culture" />
                <el-option label="休闲度假" value="leisure" />
                <el-option label="户外探险" value="adventure" />
              </el-select>
            </el-form-item>

            <el-form-item label="出行季节" prop="season">
              <el-select v-model="planForm.season" placeholder="请选择" style="width: 100%">
                <el-option label="春季" value="spring" />
                <el-option label="夏季" value="summer" />
                <el-option label="秋季" value="autumn" />
                <el-option label="冬季" value="winter" />
              </el-select>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" size="large" style="width: 100%" :loading="loading" @click="generatePlan">
                <el-icon><MagicStick /></el-icon>
                生成行程
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- 右侧结果 -->
        <div class="planner-result">
          <div v-if="!hasResult && !loading" class="empty-state">
            <el-icon :size="80" color="#DCDFE6"><Calendar /></el-icon>
            <p>填写左侧表单，开始生成您的专属行程</p>
          </div>

          <div v-else-if="loading" class="loading-state">
            <el-icon :size="60" class="is-loading"><Loading /></el-icon>
            <p>AI正在为您规划行程...</p>
          </div>

          <div v-else class="result-content glass-card">
            <div class="result-header">
              <h3>您的专属行程</h3>
              <el-button type="primary" @click="savePlan">
                <el-icon><Check /></el-icon>
                保存行程
              </el-button>
            </div>

            <!-- 行程时间线 -->
            <el-timeline class="trip-timeline">
              <el-timeline-item
                v-for="(day, index) in planResult"
                :key="index"
                :timestamp="`第 ${day.day} 天`"
                placement="top"
                color="#00796B"
                size="large"
              >
                <el-card>
                  <h4 class="day-title">{{ day.title }}</h4>
                  <div class="day-activities">
                    <div v-for="(activity, idx) in day.activities" :key="idx" class="activity-item">
                      <div class="activity-time">
                        <el-tag type="info" size="small">{{ activity.time }}</el-tag>
                      </div>
                      <div class="activity-content">
                        <h5>{{ activity.spot }}</h5>
                        <p>{{ activity.description }}</p>
                        <div class="activity-meta">
                          <span>
                            <el-icon><Clock /></el-icon>
                            {{ activity.duration }}
                          </span>
                          <span>
                            <el-icon><Money /></el-icon>
                            约 ¥{{ activity.cost }}
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="day-summary">
                    <el-tag type="warning">
                      <el-icon><Wallet /></el-icon>
                      当日预算: ¥{{ day.budget }}
                    </el-tag>
                  </div>
                </el-card>
              </el-timeline-item>
            </el-timeline>

            <!-- 总结 -->
            <div class="trip-summary">
              <h4>行程总结</h4>
              <div class="summary-stats">
                <div class="stat-item">
                  <el-icon :size="32" color="#00796B"><Calendar /></el-icon>
                  <div class="stat-info">
                    <span class="stat-label">总天数</span>
                    <span class="stat-value">{{ planForm.days }} 天</span>
                  </div>
                </div>
                <div class="stat-item">
                  <el-icon :size="32" color="#F9A825"><Wallet /></el-icon>
                  <div class="stat-info">
                    <span class="stat-label">预计花费</span>
                    <span class="stat-value">¥{{ totalCost }}</span>
                  </div>
                </div>
                <div class="stat-item">
                  <el-icon :size="32" color="#00796B"><Location /></el-icon>
                  <div class="stat-info">
                    <span class="stat-label">打卡景点</span>
                    <span class="stat-value">{{ totalSpots }} 个</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessage, FormInstance, FormRules } from 'element-plus'
import { computed, reactive, ref } from 'vue'

// 表单数据
const planFormRef = ref<FormInstance>()
const planForm = reactive({
  days: 3,
  budget: 1000,
  preference: 'nature',
  season: 'spring'
})

const planRules: FormRules = {
  days: [{ required: true, message: '请输入旅行天数', trigger: 'blur' }],
  budget: [{ required: true, message: '请输入预算', trigger: 'blur' }],
  preference: [{ required: true, message: '请选择旅行偏好', trigger: 'change' }],
  season: [{ required: true, message: '请选择出行季节', trigger: 'change' }]
}

// 加载状态
const loading = ref(false)
const hasResult = ref(false)

// 行程结果
const planResult = ref<any[]>([])

// 计算总花费
const totalCost = computed(() => {
  return planResult.value.reduce((sum, day) => sum + (day.budget || 0), 0)
})

// 计算总景点数
const totalSpots = computed(() => {
  return planResult.value.reduce((sum, day) => sum + (day.activities?.length || 0), 0)
})

// 生成行程
const generatePlan = async () => {
  if (!planFormRef.value) return

  await planFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      hasResult.value = false

      try {
        // Mock AI生成行程（实际项目中调用后端API）
        await new Promise(resolve => setTimeout(resolve, 2000))

        // 模拟生成结果
        const mockDays = []
        for (let i = 1; i <= planForm.days; i++) {
          mockDays.push({
            day: i,
            title: `第${i}天: 探索丽水之美`,
            budget: Math.floor(planForm.budget / planForm.days),
            activities: [
              {
                time: '09:00',
                spot: i === 1 ? '南明山' : i === 2 ? '仙都景区' : '古堰画乡',
                description: '欣赏秀美的自然风光，感受大自然的魅力',
                duration: '2小时',
                cost: 50
              },
              {
                time: '12:00',
                spot: '当地特色餐厅',
                description: '品尝丽水特色美食，体验地道风味',
                duration: '1.5小时',
                cost: 80
              },
              {
                time: '14:30',
                spot: i === 1 ? '古街漫步' : i === 2 ? '瓯江游船' : '云和梯田',
                description: '深度体验当地文化，拍摄美丽照片',
                duration: '3小时',
                cost: 100
              }
            ]
          })
        }

        planResult.value = mockDays
        hasResult.value = true
        ElMessage.success('行程生成成功！')
      } catch (error) {
        ElMessage.error('生成失败，请重试')
      } finally {
        loading.value = false
      }
    }
  })
}

// 保存行程
const savePlan = () => {
  ElMessage.success('行程已保存到个人中心')
  // 实际项目中调用API保存
}
</script>

<style scoped lang="scss">
.ai-trip-page {
  min-height: calc(100vh - 64px);
  padding: 40px 0;
  background: var(--color-bg);
}

.page-header {
  text-align: center;
  margin-bottom: 40px;

  .page-title {
    font-size: 40px;
    margin-bottom: 12px;
    background: linear-gradient(135deg, #00796B, #009688);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }

  .page-subtitle {
    font-size: 18px;
    color: var(--color-text-body);
  }
}

.trip-planner {
  display: grid;
  grid-template-columns: 400px 1fr;
  gap: 24px;
  align-items: start;
}

/* 左侧表单 */
.planner-form {
  padding: 32px;
  position: sticky;
  top: 84px;

  .form-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 20px;
    margin-bottom: 24px;
    color: var(--color-text-heading);
  }
}

/* 右侧结果 */
.planner-result {
  min-height: 600px;

  .empty-state,
  .loading-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 600px;
    text-align: center;

    p {
      margin-top: 16px;
      font-size: 16px;
      color: var(--color-text-body);
    }
  }

  .loading-state {
    .is-loading {
      animation: rotating 2s linear infinite;
    }
  }
}

@keyframes rotating {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.result-content {
  padding: 32px;

  .result-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 32px;
    padding-bottom: 16px;
    border-bottom: 2px solid var(--color-border);

    h3 {
      font-size: 24px;
      color: var(--color-text-heading);
    }
  }
}

/* 行程时间线 */
.trip-timeline {
  margin-bottom: 32px;

  .day-title {
    font-size: 18px;
    margin-bottom: 16px;
    color: var(--color-text-heading);
  }

  .day-activities {
    margin-bottom: 16px;

    .activity-item {
      display: flex;
      gap: 16px;
      margin-bottom: 16px;
      padding-bottom: 16px;
      border-bottom: 1px dashed var(--color-border);

      &:last-child {
        border-bottom: none;
      }

      .activity-time {
        flex-shrink: 0;
      }

      .activity-content {
        flex: 1;

        h5 {
          font-size: 16px;
          margin-bottom: 8px;
          color: var(--color-primary);
        }

        p {
          font-size: 14px;
          color: var(--color-text-body);
          margin-bottom: 8px;
        }

        .activity-meta {
          display: flex;
          gap: 16px;
          font-size: 13px;
          color: var(--color-text-body);

          span {
            display: flex;
            align-items: center;
            gap: 4px;
          }
        }
      }
    }
  }

  .day-summary {
    padding-top: 12px;
    border-top: 1px solid var(--color-border);
  }
}

/* 总结 */
.trip-summary {
  padding: 24px;
  background: rgba(0, 121, 107, 0.03);
  border-radius: 12px;

  h4 {
    font-size: 18px;
    margin-bottom: 20px;
    color: var(--color-text-heading);
  }

  .summary-stats {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 24px;

    .stat-item {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 16px;
      background: #fff;
      border-radius: 8px;

      .stat-info {
        display: flex;
        flex-direction: column;

        .stat-label {
          font-size: 12px;
          color: var(--color-text-body);
          margin-bottom: 4px;
        }

        .stat-value {
          font-size: 20px;
          font-weight: 600;
          color: var(--color-text-heading);
        }
      }
    }
  }
}
</style>
