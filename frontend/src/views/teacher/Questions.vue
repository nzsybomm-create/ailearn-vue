<template>
  <div class="page-container">
    <div class="flex between center">
      <h2>题库管理</h2>
      <el-button type="primary" @click="dialog = true">新增题目</el-button>
    </div>
    <el-table :data="list" style="margin-top: 16px;">
      <el-table-column prop="content" label="题干" show-overflow-tooltip />
      <el-table-column prop="type" label="类型" width="140" />
      <el-table-column prop="difficulty" label="难度" width="100" />
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialog" title="新增题目" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="题干"><el-input v-model="form.content" type="textarea" /></el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.type">
            <el-option label="单选" value="SINGLE_CHOICE" />
            <el-option label="多选" value="MULTIPLE_CHOICE" />
            <el-option label="判断" value="TRUE_FALSE" />
            <el-option label="填空" value="FILL_BLANK" />
          </el-select>
        </el-form-item>
        <el-form-item label="难度">
          <el-select v-model="form.difficulty">
            <el-option label="简单" value="EASY" />
            <el-option label="中等" value="MEDIUM" />
            <el-option label="困难" value="HARD" />
          </el-select>
        </el-form-item>
        <el-form-item label="选项(JSON)"><el-input v-model="form.options" placeholder='["A","B"]' /></el-form-item>
        <el-form-item label="正确答案"><el-input v-model="form.correctAnswer" /></el-form-item>
        <el-form-item label="解析"><el-input v-model="form.explanation" type="textarea" /></el-form-item>
      </el-form>
      <el-button type="success" :loading="loading" @click="create">保存</el-button>
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
const form = ref({ content: '', type: 'SINGLE_CHOICE', difficulty: 'EASY', options: '', correctAnswer: '', explanation: '' })

async function load() {
  const res = await request.get('/teacher/questions', { params: { current: 1, size: 50 } })
  list.value = res.data.records
}
async function create() {
  loading.value = true
  try {
    await request.post('/teacher/questions', form.value)
    ElMessage.success('已保存')
    dialog.value = false
    await load()
  } finally { loading.value = false }
}
async function remove(row) {
  await ElMessageBox.confirm('确认删除该题目？', '提示', { type: 'warning' })
  await request.delete(`/teacher/questions/${row.id}`)
  ElMessage.success('已删除')
  await load()
}
onMounted(load)
</script>
