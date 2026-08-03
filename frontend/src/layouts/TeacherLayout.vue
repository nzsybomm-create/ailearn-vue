<template>
  <el-container class="layout">
    <el-aside width="200px" class="aside">
      <div class="logo" @click="go('/courses')">AI Learn</div>
      <el-menu :default-active="activeMenu" router>
        <el-menu-item index="/teacher/dashboard">仪表盘</el-menu-item>
        <el-menu-item index="/teacher/courses">课程管理</el-menu-item>
        <el-menu-item index="/teacher/questions">题库管理</el-menu-item>
        <el-menu-item index="/teacher/grading">作业批改</el-menu-item>
        <el-menu-item index="/dashboard">返回学生端</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span>教师后台</span>
        <el-button text @click="go('/dashboard')">{{ userStore.user?.name }}</el-button>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const activeMenu = computed(() => route.path)
function go(path) {
  router.push(path)
}
</script>

<style scoped>
.layout { height: 100%; }
.aside { border-right: 1px solid #ebeef5; background: #fff; }
.logo { font-size: 20px; font-weight: 700; color: #409eff; padding: 16px; cursor: pointer; }
.header { display: flex; align-items: center; justify-content: space-between; border-bottom: 1px solid #ebeef5; }
</style>
