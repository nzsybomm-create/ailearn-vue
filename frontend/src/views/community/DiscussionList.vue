<template>
  <div class="page-container">
    <div class="flex between center">
      <h2>讨论区</h2>
      <el-button type="primary" @click="dialog = true">发起讨论</el-button>
    </div>
    <el-table :data="list" style="margin-top: 16px;">
      <el-table-column prop="title" label="标题">
        <template #default="{ row }">
          <a @click="go(`/discussions/${row.id}`)" style="cursor: pointer; color: #409eff;">{{ row.title }}</a>
        </template>
      </el-table-column>
      <el-table-column prop="replyCount" label="回复数" width="100" />
      <el-table-column prop="viewCount" label="浏览" width="100" />
    </el-table>
    <el-dialog v-model="dialog" title="发起讨论" width="600px">
      <el-input v-model="title" placeholder="标题" />
      <el-input v-model="content" type="textarea" :rows="4" placeholder="内容" style="margin-top: 8px;" />
      <el-button type="success" :loading="loading" @click="create" style="margin-top: 12px;">发布</el-button>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const router = useRouter()
const list = ref([])
const dialog = ref(false)
const title = ref('')
const content = ref('')
const loading = ref(false)
function go(p) { router.push(p) }
async function load() {
  const res = await request.get('/discussions', { params: { current: 1, size: 50 } })
  list.value = res.data.records
}
async function create() {
  loading.value = true
  try {
    await request.post('/discussions', null, { params: { title: title.value, content: content.value } })
    ElMessage.success('已发布')
    dialog.value = false
    title.value = ''; content.value = ''
    await load()
  } finally {
    loading.value = false
  }
}
onMounted(load)
</script>
