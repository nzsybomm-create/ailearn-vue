<template>
  <div class="page-container">
    <h2>作业</h2>
    <el-table :data="list" style="margin-top: 16px;">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="totalScore" label="总分" width="100" />
      <el-table-column label="截止" width="180">
        <template #default="{ row }">{{ row.deadline || '-' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="primary" link @click="submit(row)">提交</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="current" :page-size="size" :total="total" layout="prev, pager, next" @current-change="load" />
    <el-dialog v-model="dialog" title="提交作业" width="600px">
      <el-input v-model="content" type="textarea" :rows="4" placeholder="作业内容" />
      <el-input v-model="attachmentUrl" placeholder="附件URL（可选）" style="margin-top: 8px;" />
      <el-button type="success" :loading="loading" @click="doSubmit" style="margin-top: 12px;">提交</el-button>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const list = ref([])
const current = ref(1)
const size = ref(10)
const total = ref(0)
const dialog = ref(false)
const homeworkId = ref(null)
const content = ref('')
const attachmentUrl = ref('')
const loading = ref(false)

async function load() {
  const res = await request.get('/homework', { params: { current: current.value, size: size.value } })
  list.value = res.data.records
  total.value = res.data.total
}
function submit(row) {
  homeworkId.value = row.id
  content.value = ''
  attachmentUrl.value = ''
  dialog.value = true
}
async function doSubmit() {
  loading.value = true
  try {
    await request.post(`/homework/${homeworkId.value}/submit`, null, { params: { content: content.value, attachmentUrl: attachmentUrl.value } })
    ElMessage.success('已提交')
    dialog.value = false
  } finally {
    loading.value = false
  }
}
onMounted(load)
</script>
