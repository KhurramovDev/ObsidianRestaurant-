<template>
  <div class="menu-page">
    <!-- Header -->
    <section class="menu-header">
      <div class="container">
        <span class="badge badge-accent">📖 {{ t('nav.menu') }}</span>
        <h1>{{ t('menu.title').split(' ')[0] }} <span class="accent-text">{{ t('menu.title').split(' ').slice(1).join(' ') }}</span></h1>
        <p>{{ t('menu.sub') }}</p>
      </div>
    </section>

    <!-- Book -->
    <section class="book-section">
      <div class="book-container">
        <!-- Book Wrapper -->
        <div class="book" :class="{ open: bookOpen }">
          <!-- COVER -->
          <div class="book-cover" @click="openBook" v-if="!bookOpen">
            <div class="cover-front">
              <div class="cover-content">
                <div class="cover-logo">🍽</div>
                <h2>Obsidian</h2>
                <p>{{ t('menu.title') }}</p>
                <div class="cover-divider"></div>
                <span class="cover-hint">{{ t('menu.open_hint') }}</span>
              </div>
              <div class="cover-pattern"></div>
            </div>
          </div>

          <!-- OPEN BOOK PAGES -->
          <div class="book-open" v-else>
            <!-- LEFT PAGE: category list -->
            <div class="book-left">
              <div class="book-spine-left"></div>
              <div class="book-page-content">
                <h3 class="page-title">📋 {{ t('menu.sections') }}</h3>
                <ul class="category-list">
                  <li
                    v-for="cat in categories"
                    :key="cat.id"
                    class="category-item"
                    :class="{ active: selectedCat && selectedCat.id === cat.id }"
                    @click="selectCategory(cat)"
                  >
                    <span class="cat-num">{{ cat.pageNumber }}</span>
                    <span class="cat-title">{{ cat.title }}</span>
                    <span class="cat-arrow">›</span>
                  </li>
                </ul>
                <div class="page-footer">
                  <button class="btn-outline close-btn" @click="closeBook">✕ {{ t('menu.close') }}</button>
                </div>
              </div>
            </div>

            <!-- RIGHT PAGE: items -->
            <div class="book-right">
              <div class="book-spine-right"></div>
              <div class="book-page-content" v-if="selectedCat">
                <h3 class="page-title">{{ selectedCat.title }}</h3>
                <div v-if="loadingItems" class="loading-items">
                  <div class="spinner"></div>
                  <p>Yuklanmoqda...</p>
                </div>
                <div v-else class="menu-items-list">
                  <div
                    v-for="item in menuItems"
                    :key="item.id"
                    class="menu-item-row"
                  >
                    <div class="item-img-wrap">
                      <img
                        v-if="item.imageUrl"
                        :src="item.imageUrl"
                        :alt="item.name"
                        @error="e => e.target.style.display='none'"
                      />
                      <div v-else class="item-img-placeholder">🍽</div>
                    </div>
                    <div class="item-details">
                      <div class="item-name">{{ item.name }}</div>
                      <div class="item-stars">
                        <span v-for="s in 5" :key="s" class="star" :class="{ filled: s <= (item.rating||4) }">★</span>
                      </div>
                    </div>
                    <div class="item-right">
                      <div class="item-price">{{ formatPrice(item.price) }} so'm</div>
                      <div style="display: flex; gap: 6px;">
                        <button class="save-btn" @click="savedStore.toggleSaved(item)" :class="{ active: savedStore.isSaved(item.id) }">
                          {{ savedStore.isSaved(item.id) ? '❤️' : '🤍' }}
                        </button>
                        <button class="add-cart-btn" @click="addToCart(item)" :class="{ added: justAdded[item.id] }">
                          {{ justAdded[item.id] ? '✓ ' + t('menu.added') : '🛒 ' + t('menu.add') }}
                        </button>
                      </div>
                    </div>
                  </div>
                  <div v-if="menuItems.length === 0" class="empty-items">
                    <p>{{ t('menu.empty') }}</p>
                  </div>
                </div>
                <div class="page-number-tag">{{ selectedCat.pageNumber }}-{{ t('menu.page') }}</div>
              </div>
              <div class="book-page-content empty-page" v-else>
                <div class="empty-right">
                  <span>👈</span>
                  <p>{{ t('menu.select_cat') }}</p>
                </div>
              </div>
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
import { useCartStore } from '../stores/cart'
import { useSavedStore } from '../stores/saved'
import { useI18n } from '../i18n'

const { t } = useI18n()
const cartStore = useCartStore()
const savedStore = useSavedStore()
const categories = ref([])
const selectedCat = ref(null)
const menuItems = ref([])
const loadingItems = ref(false)
const bookOpen = ref(false)
const justAdded = ref({})

const openBook = () => { bookOpen.value = true }
const closeBook = () => { bookOpen.value = false; selectedCat.value = null }

const selectCategory = async (cat) => {
  selectedCat.value = cat
  loadingItems.value = true
  try {
    const r = await axios.get(`http://localhost:8080/api/public/menu/items/${cat.id}`)
    menuItems.value = r.data || []
  } catch { menuItems.value = [] }
  loadingItems.value = false
}

const addToCart = (item) => {
  cartStore.addItem(item)
  justAdded.value[item.id] = true
  setTimeout(() => { justAdded.value[item.id] = false }, 1500)
}

const formatPrice = (p) => {
  if (!p) return '0'
  return Number(p).toLocaleString('uz-UZ')
}

onMounted(async () => {
  try {
    const r = await axios.get('http://localhost:8080/api/public/menu/categories')
    categories.value = r.data || []
  } catch {}
})
</script>

<style scoped>
.menu-page { background: var(--bg-cream); min-height: 100vh; }
.menu-header { background: linear-gradient(135deg, #1a0a00, #2d1500); padding: 60px 0; text-align: center; color: white; }
.menu-header .badge { margin-bottom: 16px; display: inline-block; }
.menu-header h1 { font-size: clamp(28px, 4vw, 48px); margin: 12px 0; }
.accent-text { color: #c8922a; }
.menu-header p { color: rgba(255,255,255,0.7); font-size: 16px; }
.book-section { padding: 60px 24px 80px; display: flex; justify-content: center; }
.book-container { width: 100%; max-width: 900px; }
.book { perspective: 1500px; }
.book-cover { cursor: pointer; width: 320px; margin: 0 auto; border-radius: 4px 16px 16px 4px; box-shadow: -8px 8px 32px rgba(0,0,0,0.35), 4px 0 12px rgba(0,0,0,0.2); position: relative; overflow: hidden; transition: 0.3s; }
.cover-front { background: linear-gradient(160deg, #1a0a00 0%, #3d1f00 50%, #1a0a00 100%); border-left: 10px solid #c8922a; min-height: 460px; display: flex; align-items: center; justify-content: center; }
.cover-content { text-align: center; padding: 40px 32px; }
.cover-logo { font-size: 56px; margin-bottom: 12px; }
.cover-content h2 { color: #c8922a; font-size: 28px; }
.cover-pattern { position: absolute; inset: 0; background-image: repeating-linear-gradient(45deg, rgba(200,146,42,0.04) 0px, rgba(200,146,42,0.04) 1px, transparent 1px, transparent 12px); }
.book-open { display: grid; grid-template-columns: 1fr 1fr; background: white; border-radius: 4px 16px 16px 4px; box-shadow: -8px 8px 40px rgba(0,0,0,0.3); min-height: 600px; animation: openBook 0.4s ease; }
@keyframes openBook { from { opacity: 0; transform: rotateY(-10deg); } to { opacity: 1; transform: rotateY(0); } }
.book-left { border-right: 2px solid rgba(200,146,42,0.2); background: linear-gradient(to right, #fdf8f0, #fff9f2); }
.book-spine-left { position: absolute; left: 0; top: 0; bottom: 0; width: 8px; background: linear-gradient(to right, #2d1500, #c8922a); border-radius: 4px 0 0 4px; }
.book-page-content { padding: 32px 24px 24px 36px; }
.page-title { font-size: 20px; color: var(--primary); border-bottom: 2px solid rgba(200,146,42,0.3); padding-bottom: 12px; margin-bottom: 20px; }
.category-list { list-style: none; display: flex; flex-direction: column; gap: 4px; }
.category-item { display: flex; align-items: center; gap: 12px; padding: 10px 14px; border-radius: 10px; cursor: pointer; transition: 0.2s; }
.category-item.active { background: rgba(200,146,42,0.12); color: #a67318; }
.cat-num { width: 28px; height: 28px; border-radius: 50%; background: rgba(200,146,42,0.15); color: #c8922a; font-size: 12px; font-weight: 700; display: flex; align-items: center; justify-content: center; }
.menu-items-list { display: flex; flex-direction: column; gap: 8px; max-height: 480px; overflow-y: auto; }
.menu-item-row { display: flex; align-items: center; gap: 12px; padding: 10px 12px; border-radius: 10px; border: 1px solid var(--border); background: white; }
.item-img-wrap { width: 52px; height: 52px; border-radius: 10px; overflow: hidden; background: var(--bg-cream); display: flex; align-items: center; justify-content: center; }
.item-img-wrap img { width: 100%; height: 100%; object-fit: cover; }
.item-right { display: flex; flex-direction: column; align-items: flex-end; gap: 6px; }
.item-price { font-size: 14px; font-weight: 700; color: #a67318; }
.add-cart-btn { background: #c8922a; color: white; border: none; border-radius: 8px; padding: 5px 12px; font-size: 12px; font-weight: 600; cursor: pointer; }
.save-btn { background: #fef2f2; border: 1px solid #fca5a5; border-radius: 8px; padding: 5px 10px; font-size: 14px; cursor: pointer; }
.page-number-tag { position: absolute; bottom: 16px; right: 20px; font-size: 12px; color: var(--text-light); }
@media (max-width: 700px) { .book-open { grid-template-columns: 1fr; } }
</style>
