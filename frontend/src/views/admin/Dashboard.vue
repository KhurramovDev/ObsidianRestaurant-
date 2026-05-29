<template>
  <div class="admin-layout">
    <!-- Sidebar -->
    <aside class="admin-sidebar">
      <div class="sidebar-brand">
        <span>🍽</span>
        <span>Admin Panel</span>
      </div>
      <nav class="sidebar-nav">
        <router-link to="/admin" exact class="sidebar-link">📊 Dashboard</router-link>
        <router-link to="/admin/orders" class="sidebar-link">📦 Buyurtmalar</router-link>
        <router-link to="/admin/slider" class="sidebar-link">🖼 Slayd rasmlar</router-link>
        <router-link to="/admin/menu" class="sidebar-link">📖 Menyu</router-link>
        <router-link to="/admin/news" class="sidebar-link">📰 Yangiliklar</router-link>
        <router-link to="/admin/footer" class="sidebar-link">🔧 Footer</router-link>
      </nav>
      <div class="sidebar-bottom">
        <router-link to="/" class="sidebar-link">🏠 Saytga qaytish</router-link>
        <button @click="logout" class="sidebar-link sidebar-logout">🚪 Chiqish</button>
      </div>
    </aside>

    <!-- Content -->
    <main class="admin-main">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { useAuthStore } from '../../stores/auth'
import { useRouter } from 'vue-router'
const authStore = useAuthStore()
const router = useRouter()
const logout = () => { authStore.logout(); router.push('/login') }
</script>

<style scoped>
.admin-layout { display: flex; min-height: 100vh; background: #f4f6fb; }

.admin-sidebar {
  width: 240px; flex-shrink: 0;
  background: #1a0a00;
  display: flex; flex-direction: column;
  position: sticky; top: 0; height: 100vh;
}
.sidebar-brand {
  display: flex; align-items: center; gap: 10px;
  padding: 24px 20px; font-size: 17px; font-weight: 700;
  color: #c8922a; font-family: 'Playfair Display', serif;
  border-bottom: 1px solid rgba(200,146,42,0.15);
}
.sidebar-brand span:first-child { font-size: 24px; }

.sidebar-nav { flex: 1; padding: 16px 12px; display: flex; flex-direction: column; gap: 4px; }
.sidebar-link {
  display: flex; align-items: center; gap: 10px;
  padding: 11px 14px; border-radius: 10px;
  color: rgba(255,255,255,0.65); font-size: 14px; font-weight: 500;
  transition: all 0.2s; text-decoration: none; border: none;
  background: none; cursor: pointer; width: 100%; text-align: left;
}
.sidebar-link:hover { background: rgba(200,146,42,0.12); color: #c8922a; }
.sidebar-link.router-link-active { background: rgba(200,146,42,0.18); color: #c8922a; font-weight: 600; }
.sidebar-bottom { padding: 16px 12px; border-top: 1px solid rgba(255,255,255,0.06); display: flex; flex-direction: column; gap: 4px; }
.sidebar-logout { color: rgba(255,100,100,0.7) !important; }
.sidebar-logout:hover { background: rgba(239,68,68,0.1) !important; color: #ef4444 !important; }

.admin-main { flex: 1; overflow: auto; }

@media (max-width: 768px) {
  .admin-sidebar { width: 60px; }
  .sidebar-brand span:last-child, .sidebar-link span:last-child { display: none; }
}
</style>
