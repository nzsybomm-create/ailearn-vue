<template>
  <div class="auth-wrap">
    <el-card class="auth-card">
      <h2>找回密码</h2>
      <el-steps :active="step" align-center>
        <el-step title="提交邮箱" />
        <el-step title="重置密码" />
      </el-steps>
      <div v-if="step === 1">
        <el-form :model="form" label-width="80px" style="margin-top: 24px;">
          <el-form-item label="邮箱">
            <el-input v-model="form.email" />
          </el-form-item>
          <el-button type="primary" :loading="loading" @click="send">发送重置链接</el-button>
        </el-form>
      </div>
      <div v-else>
        <el-form :model="form" label-width="80px" style="margin-top: 24px;">
          <el-form-item label="Token">
            <el-input v-model="form.token" />
          </el-form-item>
          <el-form-item label="新密码">
            <el-input v-model="form.password" type="password" show-password />
          </el-form-item>
          <el-button type="primary" :loading="loading" @click="reset">重置密码</el-button>
        </el-form>
      </div>
      <el-button text @click="go('/login')" style="margin-top: 12px;">返回登录</el-button>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const router = useRouter()
const step = ref(1)
const loading = ref(false)
const form = reactive({ email: '', token: '', password: '' })
function go(p) { router.push(p) }

async function send() {
  loading.value = true
  try {
    await request.post('/auth/forgot-password', { email: form.email })
    ElMessage.success('若邮箱存在，重置链接已发送')
    step.value = 2
  } finally {
    loading.value = false
  }
}
async function reset() {
  loading.value = true
  try {
    await request.post('/auth/reset-password', { token: form.token, password: form.password })
    ElMessage.success('密码已重置，请登录')
    router.push('/login')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-wrap { display: flex; justify-content: center; align-items: center; min-height: 100vh; background: #f5f7fa; }
.auth-card { width: 460px; }
.auth-card h2 { text-align: center; margin-bottom: 16px; }
</style>
