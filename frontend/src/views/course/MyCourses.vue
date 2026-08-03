<template>
  <div class="page-container">
    <h2>我的课程</h2>
    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col v-for="c in list" :key="c.id" :span="8" style="margin-bottom: 16px;">
        <el-card shadow="hover" @click="go(`/courses/${c.id}/learn`)" style="cursor: pointer;">
          <div style="font-weight: 600;">{{ c.title }}</div>
          <div class="text-muted" style="font-size: 12px;">{{ c.category }}</div>
          <el-button type="primary" link style="margin-top: 8px;">继续学习 →</el-button>
        </el-card>
      </el-col>
    </el-row>
    <el-empty v-if="list.length === 0" description="还没有选课" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../../utils/request'

const router = useRouter()
const list = ref([])
function go(p) { router.push(p) }
async function load() {
  const res = await request.get('/courses/my', { params: { current: 1, size: 50 } })
  list.value = res.data.records
}
onMounted(load)
</script>
