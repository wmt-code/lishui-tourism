<template>
  <div class="scenic-detail-page">
    <div class="container">
      <h1 class="page-title">景点详情</h1>
      <p>景点ID: {{ route.params.id }}</p>
      <p>详情页面 - 待实现（包含图集、介绍、评论列表）</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router';

const route = useRoute()
</script>

<style scoped lang="scss">
.scenic-detail-page {
  min-height: calc(100vh - 64px);
  padding: 40px 0;

  .page-title {
    font-size: 32px;
    margin-bottom: 20px;
  }
}
</style>
