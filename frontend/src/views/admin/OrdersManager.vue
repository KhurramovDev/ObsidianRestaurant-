<template>
  <div class="admin-orders-page">
    <div class="page-header">
      <div class="header-content">
        <h1>📦 Buyurtmalar Nazorati</h1>
        <p>Barcha buyurtmalarni real vaqt rejimida boshqaring</p>
      </div>
      <div class="header-actions">
        <div class="search-box">
          <input v-model="searchQuery" type="text" placeholder="ID yoki ism bo'yicha qidirish..." />
        </div>
      </div>
    </div>

    <div class="filters-bar">
      <button
        v-for="status in statusFilters"
        :key="status.id"
        :class="['filter-btn', { active: activeFilter === status.id }]"
        @click="activeFilter = status.id"
      >
        {{ status.label }}
        <span class="count" v-if="getCount(status.id) > 0">{{ getCount(status.id) }}</span>
      </button>
    </div>

    <div class="orders-container">
      <div v-if="loading" class="loading-state">
        <div class="spinner"></div>
        <p>Ma'lumotlar yuklanmoqda...</p>
      </div>

      <div v-else-if="filteredOrders.length === 0" class="empty-state">
        <div class="empty-icon">📂</div>
        <h3>Buyurtmalar topilmadi</h3>
        <p>Tanlangan filtr bo'yicha hech qanday ma'lumot yo'q.</p>
      </div>

      <div v-else class="orders-grid">
        <div v-for="order in filteredOrders" :key="order.id" class="order-card-premium">
          <div class="order-card-header">
            <div class="order-id-tag">#{{ order.id }}</div>
            <div :class="['status-pill', order.status.toLowerCase()]">{{ getStatusLabel(order.status) }}</div>
          </div>

          <div class="order-card-body">
            <div class="customer-info">
              <div class="avatar">{{ order.user?.name?.charAt(0) }}</div>
              <div class="details">
                <strong>{{ order.user?.name }}</strong>
                <span>{{ order.user?.phoneNumber || order.user?.email }}</span>
              </div>
            </div>

            <div class="order-type-info">
              <span v-if="order.orderType === 'DELIVERY'" class="type-badge delivery">🚗 Dastavka</span>
              <span v-else class="type-badge dining">🍽 Restaran</span>
              <span class="payment-method">💳 {{ order.paymentMethod }}</span>
            </div>

            <div class="items-preview">
              <p><strong>Taomlar:</strong> {{ order.items?.length }} xil</p>
              <div class="items-dots">
                <span v-for="n in Math.min(order.items?.length || 0, 5)" :key="n" class="dot"></span>
              </div>
            </div>
          </div>

          <div class="order-card-footer">
            <div class="price-section">
              <span class="label">Jami:</span>
              <span class="amount">{{ formatPrice(order.finalAmount) }} <small>so'm</small></span>
            </div>
            <div class="action-buttons">
              <button class="btn-icon view" @click="openDetails(order)" title="Batafsil">👁</button>
              <div class="status-change-dropdown">
                <select :value="order.status" @change="updateStatus(order.id, $event.target.value)">
                  <option value="PENDING">Kutilmoqda</option>
                  <option value="PREPARING">Tayyorlash</option>
                  <option value="SHIPPING">Yo'lda</option>
                  <option value="COMPLETED">Yakunlash</option>
                  <option value="CANCELLED">Bekor qilish</option>
                </select>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Order Details Modal -->
    <div v-if="selectedOrder" class="modal-overlay" @click.self="selectedOrder = null">
      <div class="order-modal">
        <div class="modal-header">
          <h3>Buyurtma Tafsilotlari #{{ selectedOrder.id }}</h3>
          <button class="close-btn" @click="selectedOrder = null">×</button>
        </div>
        <div class="modal-body">
          <div class="modal-grid">
            <div class="modal-section info">
              <h4>📋 Umumiy ma'lumot</h4>
              <p><strong>Mijoz:</strong> {{ selectedOrder.user?.fullname }}</p>
              <p><strong>Tel:</strong> {{ selectedOrder.user?.phoneNumber }}</p>
              <p><strong>Email:</strong> {{ selectedOrder.user?.email }}</p>
              <p><strong>Vaqti:</strong> {{ formatDate(selectedOrder.createdAt) }}</p>
              <p><strong>Yetkazish vaqti:</strong> {{ selectedOrder.deliveryTime }}</p>
            </div>
            <div class="modal-section address" v-if="selectedOrder.orderType === 'DELIVERY'">
              <h4>📍 Manzil</h4>
              <p>{{ selectedOrder.deliveryAddress }}</p>
              <a v-if="selectedOrder.latitude" :href="`https://www.google.com/maps?q=${selectedOrder.latitude},${selectedOrder.longitude}`" target="_blank" class="map-link">
                Xaritada ko'rish →
              </a>
            </div>
          </div>

          <div class="modal-section items">
            <h4>🍽 Taomlar ro'yxati</h4>
            <div class="items-list">
              <div v-for="item in selectedOrder.items" :key="item.id" class="item-row">
                <span>{{ item.menuItem?.name }}</span>
                <span>{{ item.quantity }} x {{ formatPrice(item.priceAtTime) }}</span>
                <strong>{{ formatPrice(item.quantity * item.priceAtTime) }}</strong>
              </div>
            </div>
            <div class="modal-footer-totals">
              <div v-if="selectedOrder.discountAmount > 0" class="total-row discount">
                <span>Chegirma:</span>
                <span>-{{ formatPrice(selectedOrder.discountAmount) }} so'm</span>
              </div>
              <div class="total-row final">
                <span>Jami To'lanadi:</span>
                <span>{{ formatPrice(selectedOrder.finalAmount) }} so'm</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const orders = ref([])
const loading = ref(true)
const searchQuery = ref('')
const activeFilter = ref('ALL')
const selectedOrder = ref(null)

const statusFilters = [
  { id: 'ALL', label: 'Barchasi' },
  { id: 'PENDING', label: 'Kutilmoqda' },
  { id: 'PREPARING', label: 'Jarayonda' },
  { id: 'SHIPPING', label: 'Yo\'lda' },
  { id: 'COMPLETED', label: 'Bajarildi' },
  { id: 'CANCELLED', label: 'Bekor qilindi' }
]

const formatPrice = (p) => Number(p || 0).toLocaleString('uz-UZ')
const formatDate = (d) => new Date(d).toLocaleString('uz-UZ')

const getStatusLabel = (s) => {
  const found = statusFilters.find(f => f.id === s)
  return found ? found.label : s
}

const getCount = (statusId) => {
  if (statusId === 'ALL') return orders.value.length
  return orders.value.filter(o => o.status === statusId).length
}

const filteredOrders = computed(() => {
  return orders.value.filter(o => {
    const matchesSearch = o.id.toString().includes(searchQuery.value) ||
                         o.user?.name?.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesFilter = activeFilter.value === 'ALL' || o.status === activeFilter.value
    return matchesSearch && matchesFilter
  }).sort((a, b) => b.id - a.id)
})

const fetchOrders = async () => {
  loading.value = true
  try {
    const res = await axios.get('http://localhost:8080/api/orders/all')
    orders.value = res.data
  } catch (err) {
    console.error("Xatolik:", err)
  } finally {
    loading.value = false
  }
}

const updateStatus = async (orderId, newStatus) => {
  try {
    await axios.patch(`http://localhost:8080/api/orders/${orderId}/status`, { status: newStatus })
    const order = orders.value.find(o => o.id === orderId)
    if (order) order.status = newStatus
  } catch (err) {
    alert('Statusni yangilashda xatolik!')
  }
}

const openDetails = (order) => {
  selectedOrder.value = order
}

onMounted(fetchOrders)
</script>

<style scoped>
.admin-orders-page { padding: 40px; background: #f4f7fe; min-height: 100vh; font-family: 'Inter', sans-serif; }

.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }
.header-content h1 { font-size: 32px; font-weight: 800; color: #1B2559; letter-spacing: -1px; }
.header-content p { color: #A3AED0; margin-top: 5px; }

.search-box input {
  background: white; border: none; padding: 12px 20px; border-radius: 14px;
  width: 300px; box-shadow: 0 4px 14px rgba(0,0,0,0.05); font-weight: 600; outline: none;
}

.filters-bar { display: flex; gap: 12px; margin-bottom: 30px; overflow-x: auto; padding-bottom: 10px; }
.filter-btn {
  background: white; border: none; padding: 10px 20px; border-radius: 12px;
  font-weight: 700; color: #A3AED0; cursor: pointer; transition: 0.3s;
  display: flex; align-items: center; gap: 8px; white-space: nowrap;
}
.filter-btn.active { background: #4318FF; color: white; box-shadow: 0 10px 20px rgba(67, 24, 255, 0.2); }
.filter-btn .count {
  background: rgba(163, 174, 208, 0.2); padding: 2px 8px; border-radius: 8px; font-size: 11px;
}
.filter-btn.active .count { background: rgba(255, 255, 255, 0.2); }

.orders-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 24px; }
.order-card-premium {
  background: white; border-radius: 24px; padding: 24px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.02); transition: 0.3s;
  border: 1px solid rgba(0,0,0,0.03);
}
.order-card-premium:hover { transform: translateY(-5px); box-shadow: 0 20px 40px rgba(0,0,0,0.05); }

.order-card-header { display: flex; justify-content: space-between; margin-bottom: 20px; }
.order-id-tag { background: #F4F7FE; padding: 4px 12px; border-radius: 8px; font-weight: 800; color: #4318FF; font-size: 14px; }
.status-pill { padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 700; text-transform: uppercase; }
.status-pill.pending { background: #F4F7FE; color: #A3AED0; }
.status-pill.preparing { background: #E0F2FE; color: #0369A1; }
.status-pill.shipping { background: #FFF9E6; color: #D97706; }
.status-pill.completed { background: #E6FFFA; color: #05CD99; }
.status-pill.cancelled { background: #FFF5F5; color: #EE5D50; }

.customer-info { display: flex; align-items: center; gap: 12px; margin-bottom: 15px; }
.customer-info .avatar { width: 40px; height: 40px; background: #4318FF; color: white; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-weight: 800; }
.customer-info .details strong { display: block; font-size: 15px; color: #1B2559; }
.customer-info .details span { font-size: 12px; color: #A3AED0; }

.type-badge { font-size: 12px; font-weight: 700; padding: 4px 8px; border-radius: 6px; margin-right: 8px; }
.type-badge.delivery { background: #E0F2FE; color: #0369A1; }
.type-badge.dining { background: #F3E8FF; color: #7E22CE; }
.payment-method { font-size: 12px; color: #A3AED0; font-weight: 600; }

.items-preview { margin: 15px 0; padding: 12px; background: #F4F7FE; border-radius: 12px; }
.items-preview p { font-size: 13px; color: #1B2559; }
.items-dots { display: flex; gap: 4px; margin-top: 6px; }
.dot { width: 6px; height: 6px; background: #A3AED0; border-radius: 50%; opacity: 0.5; }

.order-card-footer { display: flex; justify-content: space-between; align-items: center; border-top: 1px solid #F4F7FE; padding-top: 15px; margin-top: 15px; }
.price-section .label { font-size: 11px; color: #A3AED0; text-transform: uppercase; font-weight: 700; display: block; }
.price-section .amount { font-size: 18px; font-weight: 800; color: #1B2559; }

.action-buttons { display: flex; gap: 10px; align-items: center; }
.btn-icon { background: #F4F7FE; border: none; width: 36px; height: 36px; border-radius: 10px; cursor: pointer; transition: 0.3s; font-size: 16px; }
.btn-icon:hover { background: #4318FF; color: white; }
.status-change-dropdown select { border: 1px solid #F4F7FE; background: #F4F7FE; padding: 6px 10px; border-radius: 8px; font-size: 12px; font-weight: 700; color: #1B2559; outline: none; cursor: pointer; }

.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.5); backdrop-filter: blur(8px); display: flex; align-items: center; justify-content: center; z-index: 2000; padding: 20px; }
.order-modal { background: white; border-radius: 30px; width: 100%; max-width: 700px; max-height: 90vh; overflow-y: auto; box-shadow: 0 40px 100px rgba(0,0,0,0.2); }
.modal-header { padding: 30px; border-bottom: 1px solid #F4F7FE; display: flex; justify-content: space-between; align-items: center; }
.modal-header h3 { font-size: 22px; font-weight: 800; color: #1B2559; }
.close-btn { background: none; border: none; font-size: 30px; color: #A3AED0; cursor: pointer; }

.modal-body { padding: 30px; }
.modal-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-bottom: 30px; }
.modal-section h4 { font-size: 14px; text-transform: uppercase; color: #A3AED0; letter-spacing: 1px; margin-bottom: 12px; }
.modal-section p { font-size: 15px; color: #1B2559; margin-bottom: 6px; }
.map-link { color: #4318FF; font-weight: 700; font-size: 14px; text-decoration: none; display: inline-block; margin-top: 8px; }

.items-list { background: #F4F7FE; border-radius: 20px; padding: 20px; }
.item-row { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px dashed rgba(163, 174, 208, 0.3); font-size: 14px; }
.item-row:last-child { border-bottom: none; }
.modal-footer-totals { margin-top: 20px; padding: 20px; background: #1B2559; border-radius: 20px; color: white; }
.total-row { display: flex; justify-content: space-between; margin-bottom: 8px; }
.total-row.discount { color: #05CD99; font-size: 14px; font-weight: 700; }
.total-row.final { font-size: 20px; font-weight: 800; margin-top: 10px; border-top: 1px solid rgba(255,255,255,0.1); padding-top: 10px; }

.loading-state, .empty-state { padding: 60px; text-align: center; color: #A3AED0; }
.spinner { width: 40px; height: 40px; border: 4px solid #F4F7FE; border-top-color: #4318FF; border-radius: 50%; animation: spin 1s linear infinite; margin: 0 auto 20px; }
@keyframes spin { to { transform: rotate(360deg); } }
.empty-icon { font-size: 60px; margin-bottom: 20px; opacity: 0.3; }

@media (max-width: 600px) { .modal-grid { grid-template-columns: 1fr; } .page-header { flex-direction: column; align-items: flex-start; gap: 20px; } .search-box input { width: 100%; } }
</style>
