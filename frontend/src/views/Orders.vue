<template>
  <div class="orders-page">
    <section class="page-header">
      <div class="container">
        <h1>📦 Buyurtmalarim</h1>
        <p>Barcha buyurtmalaringiz tarixi va holati</p>
      </div>
    </section>

    <section class="section" style="background:var(--bg-cream);">
      <div class="container">
        <div v-if="loading" class="center-load">
          <div class="spinner-lg"></div>
          <p>Yuklanmoqda...</p>
        </div>

        <div v-else-if="orders.length === 0" class="orders-empty">
          <span>📦</span>
          <h3>Buyurtmalar topilmadi</h3>
          <p>Siz hali buyurtma bermagansiz</p>
          <router-link to="/menu" class="btn-primary">Menyuga o'tish</router-link>
        </div>

        <div v-else class="orders-list">
          <div v-for="order in orders" :key="order.id" class="order-card card">
            <div class="order-head">
              <div class="order-meta">
                <span class="order-id">Buyurtma #{{ order.id }}</span>
                <span :class="['order-status', statusClass(order.status)]">
                  {{ statusLabel(order.status) }}
                </span>
              </div>
              <div class="order-date">{{ formatDate(order.createdAt) }}</div>
            </div>

            <div class="order-details-grid">
              <div class="detail-item">
                <span class="detail-label">Turi:</span>
                <span class="detail-value">{{ order.orderType === 'DELIVERY' ? '🚗 Dastavka' : '🍽 Restaran' }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">To'lov:</span>
                <span class="detail-value">{{ paymentLabel(order.paymentMethod) }}</span>
              </div>
              <div v-if="order.orderType === 'DELIVERY'" class="detail-item">
                <span class="detail-label">Vaqti:</span>
                <span class="detail-value">{{ timeLabel(order.deliveryTime) }}</span>
              </div>
            </div>

            <div class="order-items-list">
              <div v-for="item in order.items" :key="item.id" class="order-item-row">
                <span class="oi-name">{{ item.menuItem?.name }}</span>
                <span class="oi-qty">{{ item.quantity }} ta</span>
                <span class="oi-price">{{ formatPrice(item.priceAtTime * item.quantity) }} so'm</span>
              </div>
            </div>

            <div class="order-footer">
              <div v-if="order.discountAmount > 0" class="discount-row">
                <span>Chegirma:</span>
                <span>-{{ formatPrice(order.discountAmount) }} so'm</span>
              </div>
              <div class="total-row">
                <span>Jami to'langan:</span>
                <span class="final-price">{{ formatPrice(order.finalAmount) }} so'm</span>
              </div>
            </div>

            <div v-if="order.status === 'SHIPPING' && order.orderType === 'DELIVERY'" class="shipping-notice">
              🚚 Kuryer buyurtmangizni olib ketdi. Tez orada yetkaziladi!
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const orders = ref([])
const loading = ref(true)

const formatPrice = (p) => Number(p || 0).toLocaleString('uz-UZ')

const formatDate = (dStr) => {
  return new Date(dStr).toLocaleString('uz-UZ', {
    day: '2-digit', month: '2-digit', hour: '2-digit', minute: '2-digit'
  })
}

const statusLabel = (s) => ({
  PENDING: '⏳ Kutilmoqda',
  PREPARING: '👨‍🍳 Tayyorlanmoqda',
  SHIPPING: '🚚 Yo\'lda',
  COMPLETED: '✅ Yakunlandi',
  CANCELLED: '❌ Bekor qilindi'
}[s] || s)

const statusClass = (s) => s.toLowerCase()

const paymentLabel = (p) => ({
  CASH: 'Naqd pul',
  CARD: 'Karta orqali',
  ONLINE: 'Online to\'lov'
}[p] || p)

const timeLabel = (t) => ({
  ASAP: 'Tez orada',
  '1_HOUR': '1 soat ichida',
  EVENING: 'Kechki paytga'
}[t] || t)

onMounted(async () => {
  try {
    const r = await axios.get('http://localhost:8080/api/orders/my')
    orders.value = (r.data || []).sort((a, b) => b.id - a.id)
  } catch (err) {
    console.error("Buyurtmalarni yuklashda xatolik:", err)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.page-header { background: linear-gradient(135deg, #1a0a00, #2d1500); padding: 50px 0; text-align: center; color: white; }
.page-header h1 { font-size: 36px; margin-bottom: 8px; }

.center-load { display: flex; flex-direction: column; align-items: center; gap: 16px; padding: 60px; }
.spinner-lg { width: 40px; height: 40px; border: 3px solid #eee; border-top-color: #c8922a; border-radius: 50%; animation: spin 0.8s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }

.orders-list { display: flex; flex-direction: column; gap: 20px; max-width: 800px; margin: 0 auto; }
.order-card { padding: 24px; border-radius: 16px; background: white; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }

.order-head { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 20px; border-bottom: 1px solid #eee; padding-bottom: 15px; }
.order-id { font-size: 18px; font-weight: 700; color: #1a0a00; display: block; margin-bottom: 4px; }
.order-status { font-size: 13px; font-weight: 600; padding: 4px 12px; border-radius: 20px; }
.order-status.pending { background: #f3f4f6; color: #4b5563; }
.order-status.preparing { background: #e0f2fe; color: #0369a1; }
.order-status.shipping { background: #fef3c7; color: #92400e; }
.order-status.completed { background: #dcfce7; color: #166534; }
.order-status.cancelled { background: #fee2e2; color: #991b1b; }
.order-date { font-size: 13px; color: #888; }

.order-details-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 15px; margin-bottom: 20px; background: #f8f9fa; padding: 12px; border-radius: 10px; }
.detail-item { display: flex; flex-direction: column; }
.detail-label { font-size: 11px; color: #888; text-transform: uppercase; font-weight: 600; }
.detail-value { font-size: 13px; font-weight: 600; color: #1a0a00; }

.order-items-list { margin-bottom: 20px; }
.order-item-row { display: flex; justify-content: space-between; padding: 6px 0; font-size: 14px; border-bottom: 1px dashed #eee; }
.oi-name { font-weight: 500; }
.oi-qty { color: #888; }
.oi-price { font-weight: 600; }

.order-footer { border-top: 1px solid #eee; padding-top: 15px; }
.discount-row { display: flex; justify-content: space-between; color: #16a34a; font-size: 14px; margin-bottom: 5px; }
.total-row { display: flex; justify-content: space-between; align-items: center; }
.total-row span:first-child { font-weight: 600; }
.final-price { font-size: 20px; font-weight: 800; color: #c8922a; }

.shipping-notice { margin-top: 15px; background: #fffbeb; border: 1px solid #fef3c7; padding: 10px; border-radius: 8px; font-size: 13px; color: #92400e; text-align: center; }

@media (max-width: 600px) {
  .order-details-grid { grid-template-columns: 1fr; gap: 8px; }
}
</style>
