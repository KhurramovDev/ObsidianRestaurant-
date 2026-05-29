<template>
  <div class="saved-page">
    <section class="page-header">
      <div class="container">
        <h1>❤️ Saqlanganlar</h1>
        <p>Sizga yoqqan taomlar ro'yxati</p>
      </div>
    </section>

    <section class="section" style="background:var(--bg-cream);">
      <div class="container">
        <div v-if="savedStore.items.length === 0" class="saved-empty">
          <span>❤️</span>
          <h3>Saqlanganlar yo'q</h3>
          <p>Siz hali hech narsa saqlamadingiz</p>
          <router-link to="/menu" class="btn-primary">Menyuga o'tish</router-link>
        </div>

        <div v-else class="saved-grid">
          <div v-for="item in savedStore.items" :key="item.id" class="saved-card card">
            <div class="saved-img">
              <img v-if="item.imageUrl" :src="item.imageUrl" :alt="item.name" @error="e=>e.target.style.display='none'" />
              <span v-else>🍽</span>
              <button class="remove-fav-btn" @click="savedStore.removeSaved(item.id)">✕</button>
            </div>
            <div class="saved-info">
              <h4>{{ item.name }}</h4>
              <div class="stars">
                <span v-for="s in 5" :key="s" class="star" :class="{filled: s<=(item.rating||4)}">★</span>
              </div>
              <div class="saved-bottom">
                <span class="price">{{ formatPrice(item.price) }} so'm</span>
                <button class="add-cart-btn" @click="addToCart(item)">
                  🛒 Savatga
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { useSavedStore } from '../stores/saved'
import { useCartStore } from '../stores/cart'

const savedStore = useSavedStore()
const cartStore = useCartStore()

const formatPrice = (p) => Number(p || 0).toLocaleString('uz-UZ')

const addToCart = (item) => {
  cartStore.addItem(item)
  savedStore.removeSaved(item.id)
}
</script>

<style scoped>
.page-header { background: linear-gradient(135deg, #1a0a00, #2d1500); padding: 50px 0; text-align: center; color: white; }
.page-header h1 { font-size: 36px; margin-bottom: 8px; }
.page-header p { color: rgba(255,255,255,0.7); }

.saved-empty { display: flex; flex-direction: column; align-items: center; gap: 16px; padding: 80px; color: var(--text-gray); }
.saved-empty span { font-size: 64px; }
.saved-empty h3 { font-size: 24px; color: var(--primary); }

.saved-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); gap: 24px; }
.saved-card { overflow: hidden; display: flex; flex-direction: column; }
.saved-img { position: relative; height: 180px; background: var(--bg-cream); display: flex; align-items: center; justify-content: center; font-size: 40px; }
.saved-img img { width: 100%; height: 100%; object-fit: cover; }
.remove-fav-btn { position: absolute; top: 12px; right: 12px; background: rgba(255,255,255,0.9); border: none; width: 32px; height: 32px; border-radius: 50%; color: #ef4444; font-size: 16px; cursor: pointer; transition: all 0.2s; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
.remove-fav-btn:hover { background: #fee2e2; transform: scale(1.1); }

.saved-info { padding: 16px; flex: 1; display: flex; flex-direction: column; }
.saved-info h4 { font-size: 16px; font-weight: 600; color: var(--primary); margin-bottom: 4px; }
.stars { display: flex; gap: 1px; margin-bottom: 16px; }
.star { font-size: 12px; color: #ddd; }
.star.filled { color: #c8922a; }

.saved-bottom { display: flex; justify-content: space-between; align-items: center; margin-top: auto; }
.price { font-size: 16px; font-weight: 700; color: var(--accent-dark); }
.add-cart-btn { background: #c8922a; color: white; border: none; border-radius: 8px; padding: 6px 12px; font-size: 13px; font-weight: 600; cursor: pointer; transition: all 0.2s; }
.add-cart-btn:hover { background: #a67318; transform: scale(1.05); }
</style>
