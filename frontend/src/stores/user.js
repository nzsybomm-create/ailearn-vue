import { defineStore } from 'pinia'
import request from '../utils/request'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user') || 'null')
  }),
  getters: {
    isLogin: (state) => !!state.token,
    role: (state) => state.user?.role
  },
  actions: {
    async login(email, password) {
      const res = await request.post('/auth/login', { email, password })
      this.token = res.data.token
      this.user = res.data.user
      localStorage.setItem('token', this.token)
      localStorage.setItem('user', JSON.stringify(this.user))
      return res.data
    },
    async register(email, name, password, role) {
      return request.post('/auth/register', { email, name, password, role })
    },
    async fetchProfile() {
      const res = await request.get('/auth/me')
      this.user = res.data
      localStorage.setItem('user', JSON.stringify(this.user))
      return res.data
    },
    async updateProfile(payload) {
      const res = await request.put('/auth/me', payload)
      this.user = res.data
      localStorage.setItem('user', JSON.stringify(this.user))
      return res.data
    },
    logout() {
      this.token = ''
      this.user = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
})
