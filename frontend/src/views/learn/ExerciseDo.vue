<template>
  <div class="page-container" v-if="exercise">
    <h2>{{ exercise.title }}</h2>
    <p class="text-muted">共 {{ exercise.questions?.length || 0 }} 题</p>
    <el-card v-for="(q, i) in exercise.questions" :key="q.id" style="margin-bottom: 16px;">
      <div><b>第 {{ i + 1 }} 题：</b>{{ q.content }}</div>
      <el-radio-group v-model="answers[q.id]" style="margin-top: 8px; display: block;">
        <div v-for="(opt, idx) in parseOptions(q.options)" :key="idx" style="margin: 4px 0;">
          <el-radio :value="opt">{{ String.fromCharCode(65 + idx) }}. {{ opt }}</el-radio>
        </div>
      </el-radio-group>
    </el-card>
    <el-button type="success" :loading="loading" @click="submit">提交</el-button>
    <el-alert v-if="result" type="success" :title="`得分: ${result.score} (正确 ${result.correctCount}/${result.totalCount})`" :closable="false" style="margin-top: 16px;" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const route = useRoute()
const id = route.params.id
const exercise = ref(null)
const answers = ref({})
const loading = ref(false)
const result = ref(null)
const attemptId = ref(null)

function parseOptions(str) {
  if (!str) return []
  try { return JSON.parse(str) } catch { return str.split('\n').filter(Boolean) }
}
async function load() {
  exercise.value = (await request.get(`/exercises/${id}`)).data
  const res = await request.post(`/exercises/${id}/start`)
  attemptId.value = res.data.id
}
async function submit() {
  loading.value = true
  try {
    const res = await request.post(`/exercises/attempts/${attemptId.value}/submit`, answers.value)
    result.value = res.data
    ElMessage.success('已提交')
  } finally {
    loading.value = false
  }
}
onMounted(load)
</script>
