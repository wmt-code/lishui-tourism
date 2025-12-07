<template>
  <div class="dashboard-page">
    <h2 class="page-title">数据看板</h2>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6" v-for="(stat, index) in stats" :key="index">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" :style="{ background: stat.color }">
              <el-icon :size="32"><component :is="stat.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <p class="stat-label">{{ stat.label }}</p>
              <h3 class="stat-value">{{ stat.value }}</h3>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 其他图表等待实现 -->
    <el-card class="mt-20">
      <h3>数据统计图表</h3>
      <p>待实现 - 可集成 ECharts 等图表库</p>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const stats = ref([
  { label: '景点总数', value: '42', icon: 'Location', color: '#00796B' },
  { label: '用户总数', value: '1,234', icon: 'User', color: '#F9A825' },
  { label: '今日访问', value: '567', icon: 'View', color: '#67C23A' },
  { label: '评论总数', value: '890', icon: 'ChatDotRound', color: '#409EFF' }
])
</script>

<style scoped lang="scss">
.dashboard-page {
  .page-title {
    font-size: 28px;
    margin-bottom: 24px;
  }

  .stats-row {
    margin-bottom: 20px;
  }

  .stat-card {
    .stat-content {
      display: flex;
      align-items: center;
      gap: 16px;

      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;
      }

      .stat-info {
        flex: 1;

        .stat-label {
          font-size: 14px;
          color: var(--color-text-body);
          margin-bottom: 8px;
        }

        .stat-value {
          font-size: 28px;
          font-weight: 600;
          color: var(--color-text-heading);
        }
      }
    }
  }
}
</style>
