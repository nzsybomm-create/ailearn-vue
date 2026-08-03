<template>
  <div class="page-container">
    <h2>练习</h2>
    <el-table :data="list" style="margin-top: 16px;">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="difficulty" label="难度" />
      <el-table-column prop="totalQuestions" label="题量" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="primary" link @click="go(`/exercises/${row.id}`)">开始</el-button>
        </template>
      </el-table-column>
    </el-table>
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
const current = ref(1)
const size = ref(10)
const total = ref(0)
function go(p) { router.push(p) }
async function load() {
  const res = await request.get('/exercises', { params: { current: current.value, size: size.value } })
  list.value = res.data.records
  total.value = res.data.total
}
onMounted(load)
</script>
