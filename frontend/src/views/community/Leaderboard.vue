<template>
  <div class="page-container">
    <h2>排行榜</h2>
    <el-tabs v-model="period">
      <el-tab-pane label="总榜" name="all" />
      <el-tab-pane label="周榜" name="week" />
      <el-tab-pane label="月榜" name="month" />
    </el-tabs>
    <el-table :data="list" style="margin-top: 8px;">
      <el-table-column label="排名" width="80">
        <template #default="{ $index }">
          <el-tag :type="$index === 0 ? 'danger' : $index === 1 ? 'warning' : $index === 2 ? 'success' : 'info'">#{{ $index + 1 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="userId" label="用户ID" />
      <el-table-column prop="totalScore" label="积分" />
      <el-table-column prop="studyMinutes" label="学习分钟" />
      <el-table-column prop="completedLessons" label="完成课时" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import request from '../../utils/request'

const period = ref('all')
const list = ref([])
async function load() {
  list.value = (await request.get('/community/leaderboard', { params: { period } })).data
}
watch(period, load)
onMounted(load)
</script>
