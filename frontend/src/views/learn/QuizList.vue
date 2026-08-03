<template>
  <div class="page-container">
    <h2>测验</h2>
    <el-table :data="list" style="margin-top: 16px;">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="totalScore" label="总分" width="100" />
      <el-table-column prop="passScore" label="及格" width="100" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="primary" link @click="start(row)">开始</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="current" :page-size="size" :total="total" layout="prev, pager, next" @current-change="load" />
    <el-dialog v-model="dialog" title="答题" width="700px">
      <div v-if="quiz">
        <div v-for="(q, i) in quiz.questions" :key="q.id" style="margin-bottom: 16px;">
          <div><b>第 {{ i + 1 }} 题：</b>{{ q.content }}</div>
          <el-radio-group v-model="answers[q.id]" style="display: block; margin-top: 6px;">
            <div v-for="(opt, idx) in parseOptions(q.options)" :key="idx" style="margin: 4px 0;">
              <el-radio :value="opt">{{ String.fromCharCode(65 + idx) }}. {{ opt }}</el-radio>
            </div>
          </el-radio-group>
        </div>
        <el-button type="success" :loading="loading" @click="submit">提交</el-button>
        <el-alert v-if="result" type="success" :title="`得分: ${result.score} (正确 ${result.correctCount}/错误 ${result.wrongCount})`" :closable="false" style="margin-top: 12px;" />
      </div>
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
const quiz = ref(null)
const answers = ref({})
const loading = ref(false)
const result = ref(null)
const attemptId = ref(null)

function parseOptions(str) {
  if (!str) return []
  try { return JSON.parse(str) } catch { return str.split('\n').filter(Boolean) }
}
async function load() {
  const res = await request.get('/quizzes', { params: { current: current.value, size: size.value } })
  list.value = res.data.records
  total.value = res.data.total
}
async function start(row) {
  quiz.value = (await request.get(`/quizzes/${row.id}`)).data
  answers.value = {}
  result.value = null
  const res = await request.post(`/quizzes/${row.id}/start`)
  attemptId.value = res.data.id
  dialog.value = true
}
async function submit() {
  loading.value = true
  try {
    const res = await request.post(`/quizzes/attempts/${attemptId.value}/submit`, answers.value)
    result.value = res.data
    ElMessage.success('已提交')
  } finally {
    loading.value = false
  }
}
onMounted(load)
</script>
