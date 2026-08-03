<template>
  <div class="page-container" v-if="course">
    <el-card>
      <div class="flex gap-16">
        <img v-if="course.coverImage" :src="course.coverImage" style="width: 320px; height: 180px; object-fit: cover;" />
        <div style="flex: 1;">
          <h2>{{ course.title }}</h2>
          <p class="text-muted">{{ levelText(course.level) }} · {{ course.category }}</p>
          <p>{{ course.description }}</p>
          <div class="flex gap-8" style="margin-top: 16px;">
            <el-button type="primary" @click="enroll">{{ enrolled ? '取消选课' : '立即选课' }}</el-button>
            <el-button :type="faved ? 'warning' : 'default'" @click="toggleFav">
              {{ faved ? '已收藏' : '收藏' }}
            </el-button>
            <el-button v-if="enrolled" type="success" @click="go(`/courses/${course.id}/learn`)">开始学习</el-button>
          </div>
        </div>
      </div>
    </el-card>

    <el-card style="margin-top: 16px;">
      <h3>课程评价</h3>
      <div v-if="reviews.length === 0" class="text-muted">暂无评价</div>
      <div v-for="r in reviews" :key="r.id" style="padding: 8px 0; border-bottom: 1px solid #f0f0f0;">
        <el-rate :model-value="r.rating" disabled />
        <div>{{ r.comment }}</div>
      </div>
      <div class="flex gap-8" style="margin-top: 16px;">
        <el-rate v-model="rating" />
        <el-input v-model="comment" placeholder="写评价" style="flex: 1;" />
        <el-button type="primary" @click="submitReview">提交评价</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const route = useRoute()
const router = useRouter()
const course = ref(null)
const reviews = ref([])
const enrolled = ref(false)
const faved = ref(false)
const rating = ref(5)
const comment = ref('')
const id = route.params.id

function levelText(l) {
  return { BEGINNER: '入门', INTERMEDIATE: '进阶', ADVANCED: '高级' }[l] || l
}
function go(p) { router.push(p) }

async function load() {
  course.value = (await request.get(`/courses/${id}`)).data
  reviews.value = (await request.get(`/courses/${id}/reviews`)).data
}
async function enroll() {
  if (enrolled.value) {
    await request.delete(`/courses/${id}/enroll`)
    enrolled.value = false
    ElMessage.success('已取消选课')
  } else {
    await request.post(`/courses/${id}/enroll`)
    enrolled.value = true
    ElMessage.success('选课成功')
  }
}
async function toggleFav() {
  await request.post(`/courses/${id}/favorite`)
  faved.value = !faved.value
}
async function submitReview() {
  await request.post(`/courses/${id}/reviews`, null, { params: { rating: rating.value, comment: comment.value } })
  ElMessage.success('评价成功')
  comment.value = ''
  await load()
}
onMounted(load)
</script>
