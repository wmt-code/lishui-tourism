<template>
  <div class="home-page">
    <!-- Banner 轮播 -->
    <section class="hero-banner">
      <el-carousel height="500px" indicator-position="none">
        <el-carousel-item v-for="(banner, index) in banners" :key="index">
          <div class="banner-item" :style="{ backgroundImage: `url(${banner.image})` }">
            <div class="container banner-content">
              <h1 class="banner-title">{{ banner.title }}</h1>
              <p class="banner-subtitle">{{ banner.subtitle }}</p>
              <el-button type="primary" size="large" @click="router.push(banner.link)">
                {{ banner.buttonText }}
              </el-button>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </section>

    <!-- 热门景点 -->
    <section class="section-container">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">热门景点</h2>
          <el-button text type="primary" @click="router.push('/scenic')">
            查看全部
            <el-icon class="ml-5"><ArrowRight /></el-icon>
          </el-button>
        </div>

        <div class="scenic-grid">
          <div v-for="scenic in hotScenicList" :key="scenic.id" class="scenic-card glass-card" @click="goToDetail(scenic.id)">
            <div class="scenic-image">
              <img :src="scenic.coverImage" :alt="scenic.name" />
              <el-tag v-if="scenic.hot" type="warning" class="hot-tag">热门</el-tag>
            </div>
            <div class="scenic-info">
              <h3 class="scenic-name">{{ scenic.name }}</h3>
              <p class="scenic-desc">{{ scenic.description }}</p>
              <div class="scenic-meta">
                <div class="rating">
                  <el-rate v-model="scenic.rating" disabled show-score text-color="#ff9900" />
                </div>
                <div class="stats">
                  <el-icon><View /></el-icon>
                  <span>{{ scenic.viewCount }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- AI 行程推荐 -->
    <section class="section-container ai-section">
      <div class="container">
        <div class="ai-banner glass-card">
          <div class="ai-content">
            <el-icon :size="64" color="#F9A825"><MagicStick /></el-icon>
            <h2>AI 智能行程规划</h2>
            <p>告诉我们您的需求，让AI为您定制专属旅行路线</p>
            <el-button type="primary" size="large" @click="router.push('/ai-trip')">
              <el-icon><MagicStick /></el-icon>
              立即体验
            </el-button>
          </div>
          <div class="ai-illustration">
            <img src="https://via.placeholder.com/400x300/00796B/FFFFFF?text=AI+Travel+Assistant" alt="AI助手" />
          </div>
        </div>
      </div>
    </section>

    <!-- 最新公告 -->
    <section class="section-container">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">最新公告</h2>
        </div>

        <el-timeline class="notice-timeline">
          <el-timeline-item
            v-for="(notice, index) in noticeList"
            :key="index"
            :timestamp="notice.date"
            placement="top"
          >
            <el-card>
              <h4>{{ notice.title }}</h4>
              <p class="notice-content">{{ notice.content }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// Banner 数据
const banners = ref([
  {
    image: 'https://via.placeholder.com/1920x500/00796B/FFFFFF?text=Lishui+Tourism+Banner+1',
    title: '探索丽水秘境',
    subtitle: '体验山水之间的诗意生活',
    buttonText: '开始探索',
    link: '/scenic'
  },
  {
    image: 'https://via.placeholder.com/1920x500/009688/FFFFFF?text=Lishui+Tourism+Banner+2',
    title: 'AI 智能导游',
    subtitle: '让科技为您的旅行增添色彩',
    buttonText: '体验AI',
    link: '/ai-trip'
  },
  {
    image: 'https://via.placeholder.com/1920x500/00796B/FFFFFF?text=Lishui+Tourism+Banner+3',
    title: '定制专属路线',
    subtitle: '根据您的喜好打造独特行程',
    buttonText: '立即定制',
    link: '/ai-trip'
  }
])

// 热门景点列表
const hotScenicList = ref([
  {
    id: 1,
    name: '南明山',
    description: '丽水市区最高峰，登高望远，俯瞰全城美景',
    coverImage: 'https://via.placeholder.com/400x300/00796B/FFFFFF?text=NanMing+Mountain',
    rating: 4.5,
    viewCount: '12.3k',
    hot: true
  },
  {
    id: 2,
    name: '仙都景区',
    description: '国家级风景名胜区，拥有奇峰异石、碧水丹崖',
    coverImage: 'https://via.placeholder.com/400x300/009688/FFFFFF?text=Xiandu+Scenic+Area',
    rating: 4.8,
    viewCount: '18.6k',
    hot: true
  },
  {
    id: 3,
    name: '古堰画乡',
    description: '千年古村落，诗画江南的典型代表',
    coverImage: 'https://via.placeholder.com/400x300/00796B/FFFFFF?text=Ancient+Village',
    rating: 4.6,
    viewCount: '15.2k',
    hot: false
  },
  {
    id: 4,
    name: '云和梯田',
    description: '中国最美梯田之一，四季皆有不同景致',
    coverImage: 'https://via.placeholder.com/400x300/009688/FFFFFF?text=Yunhe+Terraces',
    rating: 4.9,
    viewCount: '22.1k',
    hot: true
  }
])

// 公告列表
const noticeList = ref([
  {
    title: '春节假期景区开放通知',
    content: '各景区将在春节期间正常开放，欢迎游客朋友们前来游玩。',
    date: '2025-01-20 10:00'
  },
  {
    title: 'AI行程规划功能上线',
    content: '全新AI智能行程规划功能已上线，快来体验个性化的旅行定制服务！',
    date: '2025-01-15 14:30'
  },
  {
    title: '景区优惠活动开启',
    content: '即日起至2月底，部分景区门票享受8折优惠，详情请查看各景区页面。',
    date: '2025-01-10 09:00'
  }
])

// 跳转到景点详情
const goToDetail = (id: number) => {
  router.push(`/scenic/${id}`)
}

// 页面加载
onMounted(() => {
  // 这里可以调用API获取数据
  // loadHotScenic()
  // loadNotices()
})
</script>

<style scoped lang="scss">
.home-page {
  min-height: 100vh;
}

/* Hero Banner */
.hero-banner {
  .banner-item {
    height: 500px;
    background-size: cover;
    background-position: center;
    position: relative;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(0, 0, 0, 0.3);
    }
  }

  .banner-content {
    position: relative;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    color: #fff;
    z-index: 1;

    .banner-title {
      font-size: 56px;
      font-weight: 700;
      margin-bottom: 16px;
      text-shadow: 0 2px 12px rgba(0, 0, 0, 0.3);
    }

    .banner-subtitle {
      font-size: 24px;
      margin-bottom: 32px;
      opacity: 0.95;
    }
  }
}

/* Section 通用样式 */
.section-container {
  padding: 60px 0;

  &.ai-section {
    background: linear-gradient(135deg, rgba(0, 121, 107, 0.03) 0%, rgba(0, 150, 136, 0.03) 100%);
  }
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;

  .section-title {
    font-size: 32px;
    font-weight: 600;
    position: relative;
    padding-bottom: 12px;

    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 0;
      width: 60px;
      height: 4px;
      background: var(--color-primary);
      border-radius: 2px;
    }
  }
}

/* 景点网格 */
.scenic-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}

.scenic-card {
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s;

  &:hover {
    transform: translateY(-8px);
    
    .scenic-image img {
      transform: scale(1.1);
    }
  }

  .scenic-image {
    position: relative;
    height: 200px;
    overflow: hidden;
    border-radius: 12px 12px 0 0;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s;
    }

    .hot-tag {
      position: absolute;
      top: 12px;
      right: 12px;
    }
  }

  .scenic-info {
    padding: 16px;

    .scenic-name {
      font-size: 18px;
      font-weight: 600;
      margin-bottom: 8px;
      color: var(--color-text-heading);
    }

    .scenic-desc {
      font-size: 14px;
      color: var(--color-text-body);
      margin-bottom: 12px;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }

    .scenic-meta {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .stats {
        display: flex;
        align-items: center;
        gap: 4px;
        color: var(--color-text-body);
        font-size: 14px;
      }
    }
  }
}

/* AI Banner */
.ai-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 48px;
  background: linear-gradient(135deg, rgba(0, 121, 107, 0.05) 0%, rgba(249, 168, 37, 0.05) 100%);

  .ai-content {
    flex: 1;
    text-align: center;

    h2 {
      font-size: 32px;
      margin: 16px 0 12px;
      color: var(--color-text-heading);
    }

    p {
      font-size: 16px;
      color: var(--color-text-body);
      margin-bottom: 24px;
    }
  }

  .ai-illustration {
    width: 400px;
    
    img {
      width: 100%;
      border-radius: 12px;
    }
  }
}

/* 公告时间线 */
.notice-timeline {
  padding: 20px 0;

  .notice-content {
    margin-top: 8px;
    color: var(--color-text-body);
    font-size: 14px;
  }
}
</style>
