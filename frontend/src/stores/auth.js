import { defineStore } from 'pinia'
import axios from 'axios'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || null,
    role: localStorage.getItem('role') || null,
    name: localStorage.getItem('name') || null,
  }),
  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.role === 'ADMIN'
  },
  actions: {
    async login(email, password) {
      try {
        const res = await axios.post('http://localhost:8080/api/auth/login', { email, password })
        this.token = res.data.accessToken
        this.role = res.data.role
        this.name = res.data.name
        
        localStorage.setItem('token', this.token)
        localStorage.setItem('role', this.role)
        localStorage.setItem('name', this.name)
        
        axios.defaults.headers.common['Authorization'] = `Bearer ${this.token}`
        return true
      } catch (error) {
        return false
      }
    },
    async register(userData) {
      try {
        await axios.post('http://localhost:8080/api/auth/register', userData)
        return { ok: true }
      } catch (error) {
        const serverMsg = error.response?.data
        if (error.response?.status === 400 && serverMsg) {
          return { ok: false, msg: "Bu email allaqachon ro'yxatdan o'tgan!" }
        }
        return { ok: false, msg: "Server bilan bog'lanib bo'lmadi. Backend ishlaganini tekshiring." }
      }
    },
    logout() {
      this.token = null
      this.role = null
      this.name = null
      localStorage.removeItem('token')
      localStorage.removeItem('role')
      localStorage.removeItem('name')
      delete axios.defaults.headers.common['Authorization']
    }
  }
})
