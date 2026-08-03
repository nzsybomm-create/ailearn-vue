<template>
  <div class="page-container" v-if="course">
    <div class="flex gap-16">
      <el-card style="width: 280px;">
        <h3>章节</h3>
        <el-tree :data="treeData" :props="{ label: 'title' }" @node-click="onNode" default-expand-all />
      </el-card>
      <el-card style="flex: 1;" v-if="lesson">
        <h2>{{ lesson.title }}</h2>
        <div v-html="lesson.content"></div>
        <h3>课件</h3>
        <ul>
          <li v-for="m in materials" :key="m.id">
            <span v-if="m.type === 'VIDEO'">🎬</span>
            <a :href="m.url" target="_blank">{{ m.title }}</a>
          </li>
        </ul>
        <el-button type="success" @click="finish">标记完成 (100%)</el-button>
      </el-card>
      <el-empty v-else description="请选择课时" style="flex: 1;" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const route = useRoute()
const id = route.params.id
const course = ref(null)
const units = ref([])
const lessons = ref([])
const lesson = ref(null)
const materials = ref([])

const treeData = computed(() => units.value.map(u => ({
  title: u.title,
  children: lessons.value.filter(l => l.unitId === u.id).map(l => ({ ...l, title: l.title }))
})))

async function load() {
  course.value = (await request.get(`/courses/${id}`)).data
  units.value = (await request.get(`/courses/${id}/units`)).data
  lessons.value = (await request.get(`/courses/${id}/lessons`)).data
  if (lessons.value.length) await open(lessons.value[0])
}
async function onNode(node) {
  if (node.id) await open(node)
}
async function open(l) {
  lesson.value = (await request.get(`/courses/lessons/${l.id}`)).data
  materials.value = (await request.get(`/courses/lessons/${l.id}/materials`)).data
}
async function finish() {
  await request.post(`/courses/lessons/${lesson.value.id}/progress`, null, { params: { percent: 100 } })
  ElMessage.success('已记录进度')
}
onMounted(load)
</script>
