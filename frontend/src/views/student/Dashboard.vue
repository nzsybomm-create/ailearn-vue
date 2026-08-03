<template>
  <div class="page-container">
    <h2>学习中心</h2>
    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col :span="8" v-for="card in cards" :key="card.title">
        <el-card shadow="hover" @click="go(card.path)" style="cursor: pointer;">
          <div style="font-size: 28px;">{{ card.icon }}</div>
          <div style="font-weight: 600; margin-top: 8px;">{{ card.title }}</div>
          <div class="text-muted" style="font-size: 12px;">{{ card.desc }}</div>
        </el-card>
      </el-col>
    </el-row>
    <el-card style="margin-top: 16px;">
      <h3>学习分析</h3>
      <el-alert type="info" :closable="false" title="提示" description="学习数据由后端 learning_analytics 表统计，可在社区模块查看错题本与分析。" />
    </el-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const cards = [
  { title: '我的课程', desc: '查看已选课程', icon: '📚', path: '/my-courses' },
  { title: '练习', desc: '巩固知识点', icon: '✏️', path: '/exercises' },
  { title: '作业', desc: '提交与查看', icon: '📝', path: '/homework' },
  { title: '讨论区', desc: '互动答疑', icon: '💬', path: '/discussions' },
  { title: 'AI 助教', desc: '智能答疑', icon: '🤖', path: '/ai-tutor' },
  { title: '排行榜', desc: '学习激励', icon: '🏆', path: '/leaderboard' }
]
function go(p) { router.push(p) }
</script>
