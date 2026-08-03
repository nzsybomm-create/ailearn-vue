<template>
  <div class="page-container">
    <el-card>
      <h2>个人资料</h2>
      <el-form :model="form" label-width="80px" style="max-width: 480px;">
        <el-form-item label="姓名">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="头像">
          <el-input v-model="form.avatar" placeholder="头像 URL" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="form.bio" type="textarea" />
        </el-form-item>
        <el-form-item label="手机">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="专业">
          <el-input v-model="form.major" />
        </el-form-item>
        <el-button type="primary" :loading="loading" @click="save">保存</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../stores/user'
import request from '../../utils/request'

const userStore = useUserStore()
const loading = ref(false)
const form = reactive({ name: '', avatar: '', bio: '', phone: '', major: '' })

onMounted(async () => {
  const user = await userStore.fetchProfile()
  Object.assign(form, {
    name: user.name, avatar: user.avatar, bio: user.bio, phone: user.phone, major: user.major
  })
})

async function save() {
  loading.value = true
  try {
    await userStore.updateProfile(form)
    ElMessage.success('已保存')
  } finally {
    loading.value = false
  }
}
</script>
