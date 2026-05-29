<template>
  <div class="app-layout">
    <!-- NAVBAR -->
    <header class="navbar" :class="{ scrolled: isScrolled }">
      <div class="container">
        <div class="navbar-inner">
          <router-link to="/" class="navbar-brand">
            <span class="brand-icon">🍽</span>
            <span class="brand-name">Obsidian</span>
          </router-link>

          <nav class="navbar-nav" :class="{ open: menuOpen }">
            <router-link to="/" @click="menuOpen=false">{{ t('nav.home') }}</router-link>
            <router-link to="/about" @click="menuOpen=false">{{ t('nav.about') }}</router-link>
            <router-link to="/menu" @click="menuOpen=false">{{ t('nav.menu') }}</router-link>
            <router-link to="/news" @click="menuOpen=false">{{ t('nav.news') }}</router-link>
            <router-link to="/contact" @click="menuOpen=false">{{ t('nav.contact') }}</router-link>
          </nav>

          <div class="navbar-actions">
            <!-- Language Switcher -->
            <div class="lang-switcher">
              <button class="lang-btn" @click="showLangMenu = !showLangMenu">
                {{ currentLang.toUpperCase() }} ▾
              </button>
              <div v-if="showLangMenu" class="lang-dropdown">
                <button @click="changeLang('uz')">UZ</button>
                <button @click="changeLang('ru')">RU</button>
                <button @click="changeLang('en')">EN</button>
                <button @click="changeLang('de')">DE</button>
              </div>
            </div>

            <router-link to="/saved" class="cart-btn" title="Saqlanganlar">
              <span class="cart-icon">❤️</span>
              <span v-if="savedStore.totalItems > 0" class="cart-badge">{{ savedStore.totalItems }}</span>
            </router-link>
            <router-link to="/cart" class="cart-btn" :title="t('nav.cart')">
              <span class="cart-icon">🛒</span>
              <span v-if="cartStore.totalItems > 0" class="cart-badge">{{ cartStore.totalItems }}</span>
            </router-link>

            <template v-if="authStore.isAuthenticated">
              <router-link to="/orders" class="nav-link-btn">{{ t('nav.orders') }}</router-link>
              <router-link v-if="authStore.isAdmin" to="/admin" class="btn-primary btn-sm">Admin</router-link>
              <button @click="logout" class="btn-ghost btn-sm">{{ t('auth.logout') }}</button>
            </template>
            <template v-else>
              <router-link to="/login" class="btn-outline btn-sm">{{ t('nav.login') }}</router-link>
              <router-link to="/register" class="btn-primary btn-sm">{{ t('nav.register') }}</router-link>
            </template>

            <button class="hamburger" @click="menuOpen = !menuOpen" :class="{ active: menuOpen }">
              <span></span><span></span><span></span>
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- MAIN -->
    <main class="main-content">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <!-- FOOTER -->
    <footer class="site-footer">
      <div class="container">
        <div class="footer-grid">
          <div class="footer-brand">
            <h3>{{ footer.restaurantName || 'Obsidian' }}</h3>
            <p>{{ footer.description || t('hero.sub') }}</p>
            <div class="footer-socials">
              <!-- Social Icons could be added here -->
            </div>
          </div>
          <div class="footer-links">
            <h4>{{ t('footer.pages') }}</h4>
            <ul>
              <li><router-link to="/">{{ t('nav.home') }}</router-link></li>
              <li><router-link to="/about">{{ t('nav.about') }}</router-link></li>
              <li><router-link to="/menu">{{ t('nav.menu') }}</router-link></li>
              <li><router-link to="/news">{{ t('nav.news') }}</router-link></li>
              <li><router-link to="/contact">{{ t('nav.contact') }}</router-link></li>
            </ul>
          </div>
          <div class="footer-contact">
            <h4>{{ t('footer.contact') }}</h4>
            <p>📍 {{ t('footer.address') }}: {{ footer.address || 'Toshkent shahar' }}</p>
            <p v-if="footer.phone1">📞 {{ footer.phone1 }}</p>
            <p v-if="footer.phone2">📞 {{ footer.phone2 }}</p>
          </div>
        </div>
        <div class="footer-bottom">
          <p>© {{ new Date().getFullYear() }} {{ footer.restaurantName || 'Obsidian' }}. {{ t('footer.rights') }}</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useAuthStore } from './stores/auth'
import { useCartStore } from './stores/cart'
import { useSavedStore } from './stores/saved'
import { useRouter } from 'vue-router'
import { useI18n } from './i18n'
import axios from 'axios'

const authStore = useAuthStore()
const cartStore = useCartStore()
const savedStore = useSavedStore()
const router = useRouter()
const { t, setLang, currentLang } = useI18n()

const menuOpen = ref(false)
const isScrolled = ref(false)
const showLangMenu = ref(false)
const footer = ref({})

const handleScroll = () => { isScrolled.value = window.scrollY > 50 }
const changeLang = (lang) => {
  setLang(lang)
  showLangMenu.value = false
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  axios.get('http://localhost:8080/api/public/footer')
    .then(r => { footer.value = r.data || {} })
    .catch(() => {})
})
onUnmounted(() => window.removeEventListener('scroll', handleScroll))

const logout = () => { authStore.logout(); router.push('/') }
</script>

<style scoped>
.navbar { position: sticky; top: 0; z-index: 100; background: rgba(26,10,0,0.97); backdrop-filter: blur(12px); border-bottom: 1px solid rgba(200,146,42,0.15); transition: all 0.3s ease; }
.navbar-inner { display: flex; align-items: center; justify-content: space-between; height: 68px; gap: 24px; }
.navbar-brand { display: flex; align-items: center; gap: 10px; color: white; font-size: 22px; font-weight: 700; }
.navbar-nav { display: flex; align-items: center; gap: 4px; flex: 1; justify-content: center; }
.navbar-nav a { color: rgba(255,255,255,0.75); padding: 8px 14px; border-radius: 8px; font-size: 14px; font-weight: 500; }
.navbar-nav a:hover, .navbar-nav a.router-link-active { color: #c8922a; background: rgba(200,146,42,0.1); }
.navbar-actions { display: flex; align-items: center; gap: 8px; }

/* Language Switcher */
.lang-switcher { position: relative; }
.lang-btn { background: rgba(255,255,255,0.1); color: white; border: none; padding: 6px 12px; border-radius: 8px; font-size: 13px; font-weight: 700; cursor: pointer; }
.lang-dropdown { position: absolute; top: 100%; right: 0; background: white; border-radius: 8px; box-shadow: 0 10px 25px rgba(0,0,0,0.2); overflow: hidden; margin-top: 8px; display: flex; flex-direction: column; z-index: 101; }
.lang-dropdown button { background: none; border: none; padding: 10px 20px; text-align: left; cursor: pointer; font-weight: 600; font-size: 13px; transition: 0.2s; color: #1a0a00; }
.lang-dropdown button:hover { background: #f0f0f0; color: #c8922a; }

.cart-btn { position: relative; color: white; font-size: 22px; padding: 6px; border-radius: 8px; }
.cart-badge { position: absolute; top: -2px; right: -2px; background: #c8922a; color: white; font-size: 10px; width: 18px; height: 18px; border-radius: 50%; display: flex; align-items: center; justify-content: center; }
.main-content { min-height: calc(100vh - 68px); }

.site-footer { background: #1a0a00; color: rgba(255,255,255,0.8); padding: 60px 0 20px; border-top: 1px solid rgba(200,146,42,0.2); }
.footer-grid { display: grid; grid-template-columns: 2fr 1fr 1.5fr; gap: 48px; padding-bottom: 40px; }
.footer-brand h3 { color: #c8922a; font-family: 'Playfair Display', serif; font-size: 24px; margin-bottom: 12px; }
.footer-brand p { font-size: 14px; color: rgba(255,255,255,0.6); line-height: 1.6; }
.footer-links h4, .footer-contact h4 { color: white; font-size: 16px; margin-bottom: 20px; font-family: 'Playfair Display', serif; }
.footer-links ul { list-style: none; }
.footer-links li { margin-bottom: 10px; }
.footer-links a { color: rgba(255,255,255,0.6); font-size: 14px; transition: 0.2s; }
.footer-links a:hover { color: #c8922a; }
.footer-contact p { font-size: 14px; color: rgba(255,255,255,0.6); margin-bottom: 10px; }
.footer-bottom { border-top: 1px solid rgba(255,255,255,0.05); padding: 20px 0; text-align: center; }
.footer-bottom p { font-size: 12px; color: rgba(255,255,255,0.4); }

@media (max-width: 768px) {
  .navbar-nav { display: none; }
  .navbar-nav.open { display: flex; flex-direction: column; position: fixed; top: 68px; left: 0; right: 0; background: #1a0a00; padding: 20px; border-bottom: 1px solid #c8922a; }
  .hamburger { display: flex; flex-direction: column; gap: 5px; background: none; }
  .hamburger span { width: 22px; height: 2px; background: white; border-radius: 2px; }
  .footer-grid { grid-template-columns: 1fr; gap: 32px; }
}
</style>
