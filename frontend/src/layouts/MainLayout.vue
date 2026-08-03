<template>
  <el-container class="layout">
    <el-header class="header">
      <div class="logo" @click="go('/courses')">AI Learn</div>
      <el-menu mode="horizontal" :default-active="activeMenu" router class="nav">
        <el-menu-item index="/courses">课程广场</el-menu-item>
        <el-menu-item index="/my-courses">我的课程</el-menu-item>
        <el-menu-item index="/exercises">练习</el-menu-item>
        <el-menu-item index="/quizzes">测验</el-menu-item>
        <el-menu-item index="/exams">考试</el-menu-item>
        <el-menu-item index="/homework">作业</el-menu-item>
        <el-menu-item index="/ai-tutor">AI 助教</el-menu-item>
        <el-menu-item index="/discussions">讨论区</el-menu-item>
        <el-menu-item index="/leaderboard">排行榜</el-menu-item>
        <el-menu-item index="/dashboard">学习中心</el-menu-item>
      </el-menu>
      <div class="user-area">
        <el-dropdown v-if="userStore.isLogin" @command="handleCommand">
          <span class="user-trigger">
            {{ userStore.user?.name || '用户' }}
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人资料</el-dropdown-item>
              <el-dropdown-item v-if="userStore.role === 'TEACHER'" command="teacher">教师后台</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <el-button v-else type="primary" @click="go('/login')">登录</el-button>
      </div>
    </el-header>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => '/' + (route.path.split('/')[1] || 'courses'))

function go(path) {
  router.push(path)
}
function handleCommand(cmd) {
  if (cmd === 'logout') {
    userStore.logout()
    router.push('/login')
  } else if (cmd === 'teacher') {
    router.push('/teacher/dashboard')
  } else {
    router.push('/' + cmd)
  }
}
</script>

<style scoped>
.layout { height: 100%; }
.header {
  display: flex;
  align-items: center;
  gap: 16px;
  border-bottom: 1px solid #ebeef5;
  background: #fff;
}
.logo { font-size: 20px; font-weight: 700; color: #409eff; cursor: pointer; }
.nav { flex: 1; border-bottom: none; }
.user-area { margin-left: auto; }
.user-trigger { cursor: pointer; display: flex; align-items: center; gap: 4px; }
</style>
