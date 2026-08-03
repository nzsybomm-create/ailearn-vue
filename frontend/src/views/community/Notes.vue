<template>
  <div class="page-container">
    <div class="flex between center">
      <h2>我的笔记</h2>
      <el-button type="primary" @click="dialog = true">新建笔记</el-button>
    </div>
    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col v-for="n in notes" :key="n.id" :span="8" style="margin-bottom: 16px;">
        <el-card>
          <div style="font-weight: 600;">{{ n.title }}</div>
          <div class="text-muted" style="margin: 8px 0; min-height: 40px;">{{ n.content }}</div>
          <el-tag v-if="n.isPublic" size="small">公开</el-tag>
        </el-card>
      </el-col>
    </el-row>
    <el-dialog v-model="dialog" title="新建笔记" width="600px">
      <el-input v-model="title" placeholder="标题" />
      <el-input v-model="content" type="textarea" :rows="4" placeholder="内容" style="margin-top: 8px;" />
      <el-checkbox v-model="isPublic" style="margin-top: 8px;">公开</el-checkbox>
      <el-button type="success" :loading="loading" @click="create" style="margin-top: 12px;">保存</el-button>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const notes = ref([])
const dialog = ref(false)
const title = ref('')
const content = ref('')
const isPublic = ref(false)
const loading = ref(false)

async function load() {
  const res = await request.get('/notes/mine', { params: { current: 1, size: 50 } })
  notes.value = res.data.records
}
async function create() {
  loading.value = true
  try {
    await request.post('/notes', null, { params: { title: title.value, content: content.value, isPublic: isPublic.value } })
    ElMessage.success('已保存')
    dialog.value = false
    title.value = ''; content.value = ''
    await load()
  } finally {
    loading.value = false
  }
}
onMounted(load)
</script>
