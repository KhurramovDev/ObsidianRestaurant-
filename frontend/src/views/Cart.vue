<template>
  <div class="cart-page">
    <section class="page-header">
      <div class="container">
        <h1>🛒 {{ t('cart.title') }}</h1>
        <p>{{ t('nav.cart') }}</p>
      </div>
    </section>

    <section class="section" style="background:var(--bg-cream);">
      <div class="container">
        <div v-if="cartStore.items.length === 0" class="cart-empty">
          <span>🛒</span>
          <h3>{{ t('cart.empty') }}</h3>
          <p>{{ t('hero.stat_dishes') }}</p>
          <router-link to="/menu" class="btn-primary">{{ t('hero.btn_menu') }}</router-link>
        </div>

        <div v-else class="cart-layout">
          <div class="cart-main">
            <div class="cart-items">
              <div v-for="item in cartStore.items" :key="item.id" class="cart-row card">
                <div class="cart-img">
                  <img v-if="item.imageUrl" :src="item.imageUrl" :alt="item.name" @error="e=>e.target.style.display='none'" />
                  <span v-else>🍽</span>
                </div>
                <div class="cart-item-info">
                  <h4>{{ item.name }}</h4>
                  <p class="cart-item-price">{{ formatPrice(item.price) }} so'm</p>
                </div>
                <div class="cart-qty">
                  <button @click="cartStore.decreaseQty(item.id)" class="qty-btn">−</button>
                  <span>{{ item.quantity }}</span>
                  <button @click="cartStore.increaseQty(item.id)" class="qty-btn">+</button>
                </div>
                <div class="cart-subtotal">{{ formatPrice(item.price * item.quantity) }} so'm</div>
                <button @click="cartStore.removeItem(item.id)" class="remove-btn">✕</button>
              </div>
            </div>

            <!-- AI Recommendation Section -->
            <div v-if="aiRecommendations.length > 0" class="ai-recommendations mt-32">
              <div class="ai-header">
                <h3>✨ {{ t('cart.ai_title') }}</h3>
                <p>{{ t('hero.sub') }}</p>
              </div>
              <div class="ai-grid">
                <div v-for="rec in aiRecommendations" :key="rec.id" class="ai-card card">
                  <div class="ai-card-img">
                    <img v-if="rec.imageUrl" :src="rec.imageUrl" :alt="rec.name" />
                    <span v-else>🍲</span>
                  </div>
                  <div class="ai-card-body">
                    <h5>{{ rec.name }}</h5>
                    <p>{{ formatPrice(rec.price) }} so'm</p>
                    <button class="btn-add-mini" @click="addToCart(rec)">+ {{ t('nav.register') }}</button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="cart-summary card">
            <h3>{{ t('cart.checkout') }}</h3>

            <div class="order-type-selector">
              <button
                :class="{ active: orderType === 'DINING' }"
                @click="orderType = 'DINING'"
              >🍽 {{ t('cart.dining') }}</button>
              <button
                :class="{ active: orderType === 'DELIVERY' }"
                @click="orderType = 'DELIVERY'"
              >🚗 {{ t('cart.delivery') }}</button>
            </div>

            <div class="summary-section">
              <label class="section-label">💳 {{ t('cart.payment') }}</label>
              <select v-model="paymentMethod" class="form-select">
                <option value="CASH">{{ t('cart.cash') }}</option>
                <option value="CARD">{{ t('cart.card') }}</option>
                <option value="ONLINE">Online (Click/Payme)</option>
              </select>
            </div>

            <div class="summary-section">
              <label class="section-label">⏰ {{ t('cart.time') }}</label>
              <select v-model="deliveryTime" class="form-select">
                <option value="ASAP">{{ t('cart.asap') }}</option>
                <option value="1_HOUR">1 {{ t('hero.stat_experience') }}</option>
              </select>
            </div>

            <div class="summary-divider"></div>

            <div class="summary-row">
              <span>{{ t('hero.stat_dishes') }}:</span>
              <strong>{{ formatPrice(cartStore.totalAmount) }} so'm</strong>
            </div>

            <div v-if="cartStore.totalAmount > 500000" class="summary-row discount">
              <span>Loyalty:</span>
              <strong>-{{ formatPrice(cartStore.totalAmount * 0.05) }} so'm</strong>
            </div>

            <div class="summary-row total">
              <span>{{ t('cart.total') }}:</span>
              <strong>{{ formatPrice(finalTotal) }} so'm</strong>
            </div>

            <!-- Delivery section -->
            <div v-if="orderType === 'DELIVERY'" class="delivery-section">
              <div v-if="selectedAddress" class="address-display">
                <div class="address-icon">📍</div>
                <div class="address-text">
                  <span class="address-label">{{ t('cart.address') }}:</span>
                  <p>{{ selectedAddress }}</p>
                </div>
                <button class="address-change-btn" @click="openMapModal">✏</button>
              </div>

              <button
                v-if="!selectedAddress"
                class="btn-map w-full"
                @click="openMapModal"
              >
                📍 {{ t('cart.address') }}
              </button>
            </div>

            <button
              class="btn-primary w-full"
              @click="placeOrder"
              :disabled="ordering || (orderType === 'DELIVERY' && !selectedAddress)"
            >
              {{ ordering ? '⏳...' : t('cart.checkout') }}
            </button>

            <button class="btn-ghost w-full mt-8" @click="cartStore.clearCart">🗑 {{ t('cart.empty') }}</button>
            <div v-if="orderMsg" class="order-msg" :class="orderOk ? 'success' : 'error'">{{ orderMsg }}</div>
            <div v-if="!authStore.isAuthenticated" class="auth-warn">
              ⚠ <router-link to="/login">{{ t('nav.login') }}</router-link>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Map Modal -->
    <div class="map-modal-overlay" v-if="showMapModal" @click.self="showMapModal = false">
      <div class="map-modal-box">
        <div class="map-modal-header">
          <div>
            <h3 class="map-modal-title">📍 {{ t('cart.address') }}</h3>
          </div>
          <button class="map-modal-close" @click="showMapModal = false">×</button>
        </div>
        <div id="leaflet-map" class="map-container"></div>
        <div class="map-modal-footer">
          <button class="btn-primary" @click="confirmAddress" :disabled="!pendingAddress">{{ t('cart.checkout') }}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, nextTick, onMounted, onUnmounted } from 'vue'
import { useCartStore } from '../stores/cart'
import { useAuthStore } from '../stores/auth'
import axios from 'axios'
import 'leaflet/dist/leaflet.css'
import L from 'leaflet'
import { useI18n } from '../i18n'

const { t } = useI18n()
const cartStore = useCartStore()
const authStore = useAuthStore()
const ordering = ref(false)
const orderMsg = ref('')
const orderOk = ref(false)
const orderType = ref('DINING')
const paymentMethod = ref('CASH')
const deliveryTime = ref('ASAP')
const aiRecommendations = ref([])

const finalTotal = computed(() => {
  const total = cartStore.totalAmount
  return total > 500000 ? total * 0.95 : total
})

const formatPrice = (p) => Number(p || 0).toLocaleString('uz-UZ')

const fetchRecommendations = async () => {
  if (cartStore.items.length === 0) { aiRecommendations.value = []; return }
  try {
    const ids = cartStore.items.map(i => i.id).join(',')
    const res = await axios.get(`http://localhost:8080/api/public/ai/recommend?cartItemIds=${ids}`)
    aiRecommendations.value = res.data
  } catch (err) { console.error(err) }
}

const addToCart = (item) => { cartStore.addToCart(item); fetchRecommendations() }
watch(() => cartStore.items.length, fetchRecommendations)
onMounted(fetchRecommendations)

const showMapModal = ref(false)
const selectedAddress = ref('')
const selectedLat = ref(null)
const selectedLng = ref(null)
const pendingAddress = ref('')
const pendingLat = ref(null)
const pendingLng = ref(null)
let mapInstance = null
let markerInstance = null

const initMap = () => {
  if (mapInstance) mapInstance.remove()
  mapInstance = L.map('leaflet-map').setView([41.2995, 69.2401], 12)
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(mapInstance)
  mapInstance.on('click', async (e) => {
    const { lat, lng } = e.latlng
    if (markerInstance) markerInstance.setLatLng([lat, lng])
    else markerInstance = L.marker([lat, lng]).addTo(mapInstance)
    pendingLat.value = lat; pendingLng.value = lng
    try {
      const res = await fetch(`https://nominatim.openstreetmap.org/reverse?lat=${lat}&lon=${lng}&format=json&accept-language=uz`)
      const data = await res.json()
      pendingAddress.value = data.display_name
    } catch { pendingAddress.value = `${lat}, ${lng}` }
  })
}

const openMapModal = async () => { showMapModal.value = true; await nextTick(); initMap() }
const confirmAddress = () => {
  selectedAddress.value = pendingAddress.value
  selectedLat.value = pendingLat.value
  selectedLng.value = pendingLng.value
  showMapModal.value = false
}

const placeOrder = async () => {
  if (!authStore.isAuthenticated) { orderMsg.value = t('nav.login'); return }
  ordering.value = true
  try {
    const items = cartStore.items.map(i => ({ menuItemId: i.id, quantity: i.quantity }))
    await axios.post('http://localhost:8080/api/orders', {
      items, orderType: orderType.value, paymentMethod: paymentMethod.value,
      deliveryTime: deliveryTime.value, deliveryAddress: orderType.value === 'DELIVERY' ? selectedAddress.value : 'Restaurant Dining',
      latitude: selectedLat.value, longitude: selectedLng.value,
    })
    orderOk.value = true; orderMsg.value = '✅'; cartStore.clearCart()
  } catch { orderOk.value = false; orderMsg.value = '❌' }
  finally { ordering.value = false }
}
onUnmounted(() => { if (mapInstance) mapInstance.remove() })
</script>

<style scoped>
.page-header { background: linear-gradient(135deg, #1a0a00, #2d1500); padding: 50px 0; text-align: center; color: white; }
.cart-layout { display: grid; grid-template-columns: 1fr 340px; gap: 28px; margin-top: 30px; }
.card { background: white; padding: 24px; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.08); }
.cart-row { display: flex; align-items: center; gap: 16px; margin-bottom: 12px; }
.cart-img { width: 64px; height: 64px; border-radius: 12px; background: #f5f5f5; display: flex; align-items: center; justify-content: center; overflow: hidden; }
.cart-item-info { flex: 1; }
.order-type-selector { display: flex; background: #f0f0f0; border-radius: 10px; padding: 4px; margin-bottom: 20px; }
.order-type-selector button { flex: 1; border: none; padding: 10px; border-radius: 8px; cursor: pointer; font-weight: 600; background: transparent; }
.order-type-selector button.active { background: white; color: #c8922a; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
.summary-row { display: flex; justify-content: space-between; margin-bottom: 10px; }
.summary-row.total { font-size: 20px; font-weight: 700; color: #a67318; }
.btn-primary { background: #c8922a; color: white; border: none; padding: 14px; border-radius: 12px; font-weight: 700; cursor: pointer; width: 100%; }
.map-modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.6); display: flex; align-items: center; justify-content: center; z-index: 2000; }
.map-modal-box { background: white; width: 90%; max-width: 600px; border-radius: 20px; overflow: hidden; }
.map-container { height: 350px; width: 100%; }
</style>
