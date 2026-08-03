<template>
  <div class="page-container" v-if="discussion">
    <el-card>
      <h2>{{ discussion.title }}</h2>
      <div class="text-muted">浏览 {{ discussion.viewCount }} · 回复 {{ discussion.replyCount }}</div>
      <p style="margin-top: 12px;">{{ discussion.content }}</p>
    </el-card>
    <el-card style="margin-top: 16px;">
      <h3>回复 ({{ comments.length }})</h3>
      <div v-for="c in comments" :key="c.id" style="padding: 8px 0; border-bottom: 1px solid #f0f0f0;">
        {{ c.content }}
      </div>
      <div class="flex gap-8" style="margin-top: 16px;">
        <el-input v-model="reply" placeholder="写回复" style="flex: 1;" @keyup.enter="sendReply" />
        <el-button type="primary" @click="sendReply">回复</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const route = useRoute()
const id = route.params.id
const discussion = ref(null)
const comments = ref([])
const reply = ref('')

async function load() {
  discussion.value = (await request.get(`/discussions/${id}`)).data
  comments.value = (await request.get(`/discussions/${id}/replies`)).data
}
async function sendReply() {
  if (!reply.value.trim()) return
  await request.post(`/discussions/${id}/replies`, null, { params: { content: reply.value } })
  ElMessage.success('已回复')
  reply.value = ''
  await load()
}
onMounted(load)
</script>
