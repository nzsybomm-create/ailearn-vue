<template>
  <div class="page-container">
    <h2>作业批改</h2>
    <el-alert type="info" :closable="false" title="说明" description="输入作业 ID 查看待批改提交并评分。" style="margin-bottom: 16px;" />
    <div class="flex gap-8">
      <el-input v-model="homeworkId" placeholder="作业 ID" style="width: 200px;" />
      <el-button type="primary" @click="load">查询提交</el-button>
    </div>
    <el-table :data="subs" style="margin-top: 16px;">
      <el-table-column prop="id" label="提交ID" width="100" />
      <el-table-column prop="userId" label="学生ID" width="100" />
      <el-table-column prop="content" label="内容" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="100" />
      <el-table-column label="操作" width="240">
        <template #default="{ row }">
          <el-input v-model="row._score" placeholder="分数" style="width: 80px;" />
          <el-button link type="primary" @click="grade(row)">评分</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const homeworkId = ref('')
const subs = ref([])
async function load() {
  if (!homeworkId.value) return
  subs.value = (await request.get(`/teacher/homework/${homeworkId.value}/submissions`)).data
}
async function grade(row) {
  await request.post(`/teacher/submissions/${row.id}/grade`, null, { params: { score: row._score, feedback: '' } })
  ElMessage.success('已批改')
  await load()
}
</script>
