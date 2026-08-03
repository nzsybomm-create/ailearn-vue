<template>
  <div class="page-container">
    <h2>AI 助教</h2>
    <div class="flex gap-8">
      <el-button type="primary" @click="newSession">新建对话</el-button>
    </div>
    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col :span="6">
        <el-card>
          <h4>会话列表</h4>
          <div v-for="s in sessions" :key="s.id" @click="openSession(s)"
               :class="['session-item', s.id === activeId ? 'active' : '']">
            {{ s.title }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="18">
        <el-card style="height: 520px; display: flex; flex-direction: column;">
          <div class="messages" style="flex: 1; overflow-y: auto;">
            <div v-for="(m, i) in messages" :key="i" :class="['msg', m.role]">
              <b>{{ m.role === 'user' ? '我' : 'AI' }}：</b>{{ m.content }}
            </div>
          </div>
          <div class="flex gap-8" style="margin-top: 8px;">
            <el-input v-model="input" placeholder="输入你的问题" @keyup.enter="send" />
            <el-button type="primary" @click="send">发送</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const sessions = ref([])
const activeId = ref(null)
const messages = ref([])
const input = ref('')

async function loadSessions() {
  sessions.value = (await request.get('/ai-tutor/sessions')).data
  if (sessions.value.length && !activeId.value) await openSession(sessions.value[0])
}
async function newSession() {
  const res = await request.post('/ai-tutor/sessions', null, { params: { title: '新对话' } })
  sessions.value.unshift(res.data)
  await openSession(res.data)
}
async function openSession(s) {
  activeId.value = s.id
  messages.value = (await request.get(`/ai-tutor/sessions/${s.id}/messages`)).data
}
async function send() {
  if (!activeId.value) { ElMessage.warning('请先新建会话'); return }
  if (!input.value.trim()) return
  const content = input.value
  messages.value.push({ role: 'user', content })
  input.value = ''
  const res = await request.post(`/ai-tutor/sessions/${activeId.value}/messages`, null, { params: { content } })
  messages.value.push(res.data)
}
onMounted(loadSessions)
</script>

<style scoped>
.session-item { padding: 8px; cursor: pointer; border-radius: 4px; }
.session-item.active { background: #ecf5ff; color: #409eff; }
.msg { padding: 6px 0; }
.msg.assistant { color: #67c23a; }
</style>
