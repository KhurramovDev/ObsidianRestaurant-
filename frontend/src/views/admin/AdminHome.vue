<template>
  <div class="admin-page">
    <div class="admin-page-header">
      <div class="header-left">
        <h2>🚀 Professional Dashboard</h2>
        <p>Tizim tahlili va operatsiyalar nazorati</p>
      </div>
      <div class="header-right">
        <button @click="fetchData" class="btn-refresh" :class="{ spinning: loading }">
          <span class="icon">🔄</span> Yangilash
        </button>
      </div>
    </div>

    <!-- Stats Grid -->
    <div class="stats-cards">
      <div class="stat-card revenue">
        <div class="stat-content">
          <span class="stat-label">Umumiy Savdo</span>
          <strong class="stat-value">{{ formatPrice(stats.totalRevenue) }} <small>so'm</small></strong>
          <div class="stat-trend positive">↑ 14% o'sish</div>
        </div>
        <div class="stat-icon-wrapper">💰</div>
      </div>
      <div class="stat-card orders">
        <div class="stat-content">
          <span class="stat-label">Buyurtmalar</span>
          <strong class="stat-value">{{ stats.totalOrders }}</strong>
          <div class="stat-trend">Bugun: +{{ stats.todayOrders }}</div>
        </div>
        <div class="stat-icon-wrapper">📦</div>
      </div>
      <div class="stat-card pending">
        <div class="stat-content">
          <span class="stat-label">Jarayonda</span>
          <strong class="stat-value">{{ stats.pendingOrders }}</strong>
          <div class="stat-trend warning">E'tibor talab!</div>
        </div>
        <div class="stat-icon-wrapper">⏳</div>
      </div>
      <div class="stat-card menu">
        <div class="stat-content">
          <span class="stat-label">Taomlar</span>
          <strong class="stat-value">{{ stats.items }}</strong>
          <div class="stat-trend">{{ stats.categories }} kategoriya</div>
        </div>
        <div class="stat-icon-wrapper">🍽</div>
      </div>
    </div>

    <!-- Main Analytics Section -->
    <div class="analytics-grid">
      <div class="chart-container card">
        <div class="card-header">
          <h3>Savdo Dinamikasi</h3>
          <div class="chart-period">
            <button class="active">Haftalik</button>
            <button>Oylik</button>
          </div>
        </div>
        <div class="visual-chart">
          <!-- Premium SVG Chart Implementation -->
          <svg viewBox="0 0 800 300" class="svg-chart">
            <defs>
              <linearGradient id="grad" x1="0%" y1="0%" x2="0%" y2="100%">
                <stop offset="0%" style="stop-color:rgba(200,146,42,0.2);stop-opacity:1" />
                <stop offset="100%" style="stop-color:rgba(200,146,42,0);stop-opacity:1" />
              </linearGradient>
            </defs>
            <path
              d="M0,280 Q100,200 200,240 T400,100 T600,150 T800,50 L800,300 L0,300 Z"
              fill="url(#grad)"
            />
            <path
              d="M0,280 Q100,200 200,240 T400,100 T600,150 T800,50"
              fill="none"
              stroke="#c8922a"
              stroke-width="4"
              stroke-linecap="round"
            />
            <g class="chart-dots">
              <circle cx="200" cy="240" r="6" fill="#c8922a" />
              <circle cx="400" cy="100" r="6" fill="#c8922a" />
              <circle cx="600" cy="150" r="6" fill="#c8922a" />
              <circle cx="800" cy="50" r="6" fill="#c8922a" />
            </g>
          </svg>
          <div class="chart-labels">
            <span>Dush</span><span>Sesh</span><span>Chor</span><span>Pay</span><span>Jum</span><span>Shan</span><span>Yak</span>
          </div>
        </div>
      </div>

      <div class="recent-list card">
        <div class="card-header">
          <h3>So'nggi harakatlar</h3>
          <router-link to="/admin/orders" class="view-all">Barchasi →</router-link>
        </div>
        <div class="activity-items">
          <div v-for="order in recentOrders" :key="order.id" class="activity-row">
            <div class="activity-avatar">{{ order.user?.name?.charAt(0) }}</div>
            <div class="activity-info">
              <strong>{{ order.user?.name }}</strong>
              <p>#{{ order.id }} - {{ order.orderType }}</p>
            </div>
            <div class="activity-amount">{{ formatPrice(order.finalAmount) }}</div>
          </div>
          <div v-if="recentOrders.length === 0" class="empty-msg">Hozircha buyurtmalar yo'q</div>
        </div>
      </div>
    </div>

    <!-- Quick Navigation -->
    <div class="quick-nav">
      <router-link to="/admin/orders" class="nav-item">
        <span class="nav-icon">📦</span>
        <div class="nav-text">
          <strong>Buyurtmalar</strong>
          <p>Barcha buyurtmalarni boshqarish</p>
        </div>
      </router-link>
      <router-link to="/admin/menu" class="nav-item">
        <span class="nav-icon">📖</span>
        <div class="nav-text">
          <strong>Menyu</strong>
          <p>Taom va kategoriyalar</p>
        </div>
      </router-link>
      <router-link to="/admin/news" class="nav-item">
        <span class="nav-icon">📰</span>
        <div class="nav-text">
          <strong>Yangiliklar</strong>
          <p>Aksiya va e'lonlar</p>
        </div>
      </router-link>
      <router-link to="/admin/footer" class="nav-item">
        <span class="nav-icon">⚙️</span>
        <div class="nav-text">
          <strong>Sozlamalar</strong>
          <p>Tizim konfiguratsiyasi</p>
        </div>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const stats = ref({
  totalOrders: 0,
  pendingOrders: 0,
  todayOrders: 0,
  totalRevenue: 0,
  items: 0,
  categories: 0
})
const recentOrders = ref([])
const loading = ref(false)

const formatPrice = (p) => Number(p || 0).toLocaleString('uz-UZ')

const fetchData = async () => {
  loading.value = true
  try {
    const [cats, items, allOrders] = await Promise.all([
      axios.get('http://localhost:8080/api/public/menu/categories'),
      axios.get('http://localhost:8080/api/public/menu/items'),
      axios.get('http://localhost:8080/api/orders/all')
    ])

    const orders = allOrders.data || []
    stats.value.categories = cats.data?.length || 0
    stats.value.items = items.data?.length || 0
    stats.value.totalOrders = orders.length
    stats.value.pendingOrders = orders.filter(o => o.status === 'PENDING').length

    const today = new Date().toISOString().split('T')[0]
    stats.value.todayOrders = orders.filter(o => o.createdAt?.startsWith(today)).length

    stats.value.totalRevenue = orders
      .filter(o => o.status === 'COMPLETED')
      .reduce((acc, curr) => acc + curr.finalAmount, 0)

    recentOrders.value = orders.slice(0, 5)
  } catch (err) {
    console.error("Dashboard error:", err)
  } finally {
    setTimeout(() => { loading.value = false }, 500)
  }
}

onMounted(fetchData)
</script>

<style scoped>
.admin-page { padding: 40px; background: #f4f7fe; min-height: 100vh; font-family: 'Inter', sans-serif; }

.admin-page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 40px; }
.header-left h2 { font-size: 32px; font-weight: 800; color: #1B2559; letter-spacing: -1px; }
.header-left p { color: #A3AED0; font-size: 16px; margin-top: 4px; }

.btn-refresh {
  display: flex; align-items: center; gap: 8px;
  background: white; border: none; padding: 12px 24px; border-radius: 14px;
  font-weight: 700; color: #2B3674; box-shadow: 0 4px 14px rgba(0,0,0,0.05);
  cursor: pointer; transition: 0.3s;
}
.btn-refresh:hover { transform: translateY(-2px); box-shadow: 0 6px 20px rgba(0,0,0,0.08); }
.spinning .icon { display: inline-block; animation: spin 1s linear infinite; }
@keyframes spin { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }

.stats-cards { display: grid; grid-template-columns: repeat(4, 1fr); gap: 24px; margin-bottom: 40px; }
.stat-card {
  background: white; padding: 24px; border-radius: 24px;
  display: flex; justify-content: space-between; align-items: center;
  box-shadow: 0 4px 12px rgba(0,0,0,0.02); border: 1px solid rgba(0,0,0,0.03);
}
.stat-label { font-size: 14px; color: #A3AED0; font-weight: 600; display: block; margin-bottom: 8px; }
.stat-value { font-size: 26px; font-weight: 800; color: #1B2559; }
.stat-value small { font-size: 14px; color: #A3AED0; }
.stat-trend { font-size: 12px; margin-top: 8px; font-weight: 700; color: #A3AED0; }
.stat-trend.positive { color: #05CD99; }
.stat-trend.warning { color: #EE5D50; }

.stat-icon-wrapper {
  width: 56px; height: 56px; background: #F4F7FE;
  border-radius: 18px; display: flex; align-items: center;
  justify-content: center; font-size: 24px;
}

.analytics-grid { display: grid; grid-template-columns: 2fr 1fr; gap: 24px; margin-bottom: 40px; }
.card { background: white; padding: 30px; border-radius: 24px; box-shadow: 0 4px 12px rgba(0,0,0,0.02); }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.card-header h3 { font-size: 20px; font-weight: 800; color: #1B2559; }

.chart-period { background: #F4F7FE; padding: 4px; border-radius: 12px; }
.chart-period button {
  border: none; padding: 6px 16px; border-radius: 10px;
  font-size: 13px; font-weight: 700; cursor: pointer; background: transparent; color: #A3AED0;
}
.chart-period button.active { background: white; color: #4318FF; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }

.visual-chart { margin-top: 20px; }
.svg-chart { width: 100%; height: 260px; filter: drop-shadow(0 10px 10px rgba(200,146,42,0.1)); }
.chart-labels { display: flex; justify-content: space-between; padding: 0 10px; margin-top: 15px; color: #A3AED0; font-size: 13px; font-weight: 600; }

.activity-items { display: flex; flex-direction: column; gap: 18px; }
.activity-row { display: flex; align-items: center; gap: 14px; }
.activity-avatar {
  width: 44px; height: 44px; background: #4318FF; color: white;
  border-radius: 14px; display: flex; align-items: center;
  justify-content: center; font-weight: 800; font-size: 18px;
}
.activity-info { flex: 1; }
.activity-info strong { font-size: 15px; color: #1B2559; display: block; }
.activity-info p { font-size: 13px; color: #A3AED0; }
.activity-amount { font-weight: 800; color: #1B2559; font-size: 15px; }

.quick-nav { display: grid; grid-template-columns: repeat(4, 1fr); gap: 24px; }
.nav-item {
  background: white; padding: 24px; border-radius: 24px;
  display: flex; align-items: center; gap: 16px; text-decoration: none;
  transition: 0.3s; border: 1px solid transparent;
}
.nav-item:hover { transform: translateY(-5px); border-color: #c8922a; box-shadow: 0 20px 40px rgba(0,0,0,0.05); }
.nav-icon { font-size: 32px; }
.nav-text strong { display: block; font-size: 16px; color: #1B2559; margin-bottom: 4px; }
.nav-text p { font-size: 12px; color: #A3AED0; }

@media (max-width: 1200px) { .stats-cards { grid-template-columns: repeat(2, 1fr); } .analytics-grid { grid-template-columns: 1fr; } .quick-nav { grid-template-columns: repeat(2, 1fr); } }
</style>
