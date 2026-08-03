import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  { path: '/login', name: 'Login', component: () => import('../views/auth/Login.vue'), meta: { public: true } },
  { path: '/register', name: 'Register', component: () => import('../views/auth/Register.vue'), meta: { public: true } },
  { path: '/forgot', name: 'Forgot', component: () => import('../views/auth/Forgot.vue'), meta: { public: true } },
  {
    path: '/',
    component: () => import('../layouts/MainLayout.vue'),
    children: [
      { path: '', redirect: '/courses' },
      { path: 'courses', name: 'CourseList', component: () => import('../views/course/CourseList.vue') },
      { path: 'courses/:id', name: 'CourseDetail', component: () => import('../views/course/CourseDetail.vue') },
      { path: 'courses/:id/learn', name: 'Learn', component: () => import('../views/course/Learn.vue') },
      { path: 'my-courses', name: 'MyCourses', component: () => import('../views/course/MyCourses.vue') },
      { path: 'exercises', name: 'ExerciseList', component: () => import('../views/learn/ExerciseList.vue') },
      { path: 'exercises/:id', name: 'ExerciseDo', component: () => import('../views/learn/ExerciseDo.vue') },
      { path: 'quizzes', name: 'QuizList', component: () => import('../views/learn/QuizList.vue') },
      { path: 'exams', name: 'ExamList', component: () => import('../views/learn/ExamList.vue') },
      { path: 'homework', name: 'HomeworkList', component: () => import('../views/learn/HomeworkList.vue') },
      { path: 'ai-tutor', name: 'AiTutor', component: () => import('../views/learn/AiTutor.vue') },
      { path: 'discussions', name: 'DiscussionList', component: () => import('../views/community/DiscussionList.vue') },
      { path: 'discussions/:id', name: 'DiscussionDetail', component: () => import('../views/community/DiscussionDetail.vue') },
      { path: 'notes', name: 'Notes', component: () => import('../views/community/Notes.vue') },
      { path: 'leaderboard', name: 'Leaderboard', component: () => import('../views/community/Leaderboard.vue') },
      { path: 'dashboard', name: 'Dashboard', component: () => import('../views/student/Dashboard.vue') },
      { path: 'profile', name: 'Profile', component: () => import('../views/auth/Profile.vue') }
    ]
  },
  {
    path: '/teacher',
    component: () => import('../layouts/TeacherLayout.vue'),
    meta: { teacher: true },
    children: [
      { path: '', redirect: '/teacher/dashboard' },
      { path: 'dashboard', name: 'TeacherDashboard', component: () => import('../views/teacher/Dashboard.vue') },
      { path: 'courses', name: 'TeacherCourses', component: () => import('../views/teacher/Courses.vue') },
      { path: 'questions', name: 'TeacherQuestions', component: () => import('../views/teacher/Questions.vue') },
      { path: 'grading', name: 'TeacherGrading', component: () => import('../views/teacher/Grading.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  const userStore = useUserStore()
  if (!to.meta.public && !userStore.isLogin) {
    return { name: 'Login', query: { redirect: to.fullPath } }
  }
  if (to.meta.teacher && userStore.role !== 'TEACHER') {
    return { name: 'Dashboard' }
  }
})

export default router
