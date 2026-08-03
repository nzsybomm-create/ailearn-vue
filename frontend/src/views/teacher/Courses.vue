<template>
  <div class="page-container">
    <div class="flex between center">
      <h2>课程管理</h2>
      <el-button type="primary" @click="openCreate">新建课程</el-button>
    </div>
    <el-table :data="list" style="margin-top: 16px;">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="category" label="分类" width="120" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.isPublished ? 'success' : 'info'">{{ row.isPublished ? '已发布' : '草稿' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220">
        <template #default="{ row }">
          <el-button link type="primary" @click="togglePublish(row)">{{ row.isPublished ? '下架' : '发布' }}</el-button>
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialog" title="新建课程" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="分类"><el-input v-model="form.category" /></el-form-item>
        <el-form-item label="难度">
          <el-select v-model="form.level">
            <el-option label="入门" value="BEGINNER" />
            <el-option label="进阶" value="INTERMEDIATE" />
            <el-option label="高级" value="ADVANCED" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
      </el-form>
      <el-button type="success" :loading="loading" @click="create">创建</el-button>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const list = ref([])
const dialog = ref(false)
const loading = ref(false)
const form = ref({ title: '', category: '', level: 'BEGINNER', description: '' })

async function load() {
  const res = await request.get('/teacher/courses', { params: { current: 1, size: 50 } })
  list.value = res.data.records
}
function openCreate() { dialog.value = true }
async function create() {
  loading.value = true
  try {
    await request.post('/teacher/courses', form.value)
    ElMessage.success('已创建')
    dialog.value = false
    await load()
  } finally { loading.value = false }
}
async function togglePublish(row) {
  await request.post(`/teacher/courses/${row.id}/publish`, null, { params: { published: !row.isPublished } })
  ElMessage.success('已更新')
  await load()
}
async function remove(row) {
  await ElMessageBox.confirm('确认删除该课程？', '提示', { type: 'warning' })
  await request.delete(`/teacher/courses/${row.id}`)
  ElMessage.success('已删除')
  await load()
}
onMounted(load)
</script>
