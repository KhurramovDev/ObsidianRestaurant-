import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const routes = [
  { path: '/', name: 'Home', component: () => import('../views/Home.vue') },
  { path: '/about', name: 'About', component: () => import('../views/About.vue') },
  { path: '/menu', name: 'Menu', component: () => import('../views/Menu.vue') },
  { path: '/news', name: 'News', component: () => import('../views/News.vue') },
  { path: '/contact', name: 'Contact', component: () => import('../views/Contact.vue') },
  { path: '/cart', name: 'Cart', component: () => import('../views/Cart.vue') },
  { path: '/saved', name: 'Saved', component: () => import('../views/Saved.vue') },
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('../views/Register.vue') },
  { path: '/orders', name: 'Orders', component: () => import('../views/Orders.vue'), meta: { requiresAuth: true } },
  { 
    path: '/admin', 
    component: () => import('../views/admin/Dashboard.vue'), 
    meta: { requiresAdmin: true },
    children: [
      { path: '', name: 'Admin', component: () => import('../views/admin/AdminHome.vue') },
      { path: 'orders', name: 'AdminOrders', component: () => import('../views/admin/OrdersManager.vue') },
      { path: 'content', name: 'AdminContent', component: () => import('../views/admin/ContentManager.vue') },
      { path: 'menu', name: 'AdminMenu', component: () => import('../views/admin/MenuManager.vue') },
      { path: 'news', name: 'AdminNews', component: () => import('../views/admin/NewsManager.vue') },
      { path: 'slider', name: 'AdminSlider', component: () => import('../views/admin/SliderManager.vue') },
      { path: 'footer', name: 'AdminFooter', component: () => import('../views/admin/ContentManager.vue') }
    ]
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
  } else if (to.meta.requiresAdmin && (!authStore.isAuthenticated || authStore.role !== 'ADMIN')) {
    next('/')
  } else {
    next()
  }
})

export default router
