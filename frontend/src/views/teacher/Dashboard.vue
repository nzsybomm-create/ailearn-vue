<template>
  <div class="page-container">
    <h2>教师仪表盘</h2>
    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col :span="8">
        <el-card><div class="text-muted">我的课程</div><div style="font-size: 28px; font-weight: 700;">{{ data.courseCount || 0 }}</div></el-card>
      </el-col>
      <el-col :span="8">
        <el-card><div class="text-muted">我的题目</div><div style="font-size: 28px; font-weight: 700;">{{ data.questionCount || 0 }}</div></el-card>
      </el-col>
    </el-row>
    <el-card style="margin-top: 16px;">
      <el-button type="primary" @click="go('/teacher/courses')">课程管理</el-button>
      <el-button @click="go('/teacher/questions')">题库管理</el-button>
      <el-button @click="go('/teacher/grading')">作业批改</el-button>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../../utils/request'

const router = useRouter()
const data = ref({})
function go(p) { router.push(p) }
onMounted(async () => { data.value = (await request.get('/teacher/dashboard')).data })
</script>
