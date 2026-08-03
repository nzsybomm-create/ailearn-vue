<template>
  <div class="page-container">
    <div class="flex between center" style="margin-bottom: 16px;">
      <h2>课程广场</h2>
      <div class="flex gap-8">
        <el-input v-model="keyword" placeholder="搜索课程" clearable @keyup.enter="load" style="width: 200px;" />
        <el-select v-model="level" placeholder="难度" clearable style="width: 120px;">
          <el-option label="入门" value="BEGINNER" />
          <el-option label="进阶" value="INTERMEDIATE" />
          <el-option label="高级" value="ADVANCED" />
        </el-select>
        <el-button type="primary" @click="load">查询</el-button>
      </div>
    </div>
    <el-row :gutter="16">
      <el-col v-for="c in list" :key="c.id" :span="6" style="margin-bottom: 16px;">
        <el-card shadow="hover" @click="go(`/courses/${c.id}`)" style="cursor: pointer;">
          <img v-if="c.coverImage" :src="c.coverImage" style="width: 100%; height: 140px; object-fit: cover;" />
          <div style="height: 12px;"></div>
          <div style="font-weight: 600;">{{ c.title }}</div>
          <div class="text-muted" style="font-size: 12px; margin-top: 4px;">{{ levelText(c.level) }} · {{ c.category }}</div>
        </el-card>
      </el-col>
    </el-row>
    <el-pagination v-model:current-page="current" :page-size="size" :total="total"
                   layout="prev, pager, next" @current-change="load" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../../utils/request'

const router = useRouter()
const list = ref([])
const keyword = ref('')
const level = ref('')
const current = ref(1)
const size = ref(12)
const total = ref(0)

function levelText(l) {
  return { BEGINNER: '入门', INTERMEDIATE: '进阶', ADVANCED: '高级' }[l] || l
}
function go(p) { router.push(p) }

async function load() {
  const res = await request.get('/courses', { params: { current: current.value, size: size.value, keyword: keyword.value, level: level.value } })
  list.value = res.data.records
  total.value = res.data.total
}
onMounted(load)
</script>
