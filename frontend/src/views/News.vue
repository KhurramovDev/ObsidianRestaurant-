<template>
  <div class="news-page">
    <section class="news-header">
      <div class="container">
        <span class="badge badge-accent">📰 Yangiliklar</span>
        <h1>Restoran <span class="accent-text">Yangiliklari</span></h1>
        <p>Yangi taomlar, maxsus takliflar va muhim e'lonlar</p>
      </div>
    </section>

    <section class="section" style="background:var(--bg-cream);">
      <div class="container">
        <div v-if="loading" class="news-loading">
          <div class="spinner-lg"></div>
          <p>Yuklanmoqda...</p>
        </div>

        <div v-else-if="news.length === 0" class="news-empty">
          <span>📋</span>
          <h3>Hozircha yangiliklar yo'q</h3>
          <p>Tez orada yangiliklar qo'shiladi</p>
        </div>

        <div v-else class="news-grid">
          <article v-for="item in news" :key="item.id" class="news-card card" @click="openNews(item)">
            <div class="news-card-img">
              <img v-if="item.imageUrl" :src="item.imageUrl" :alt="item.title" @error="e => e.target.parentElement.style.display='none'" />
              <div v-else class="news-no-img">📰</div>
            </div>
            <div class="news-card-body">
              <div class="news-date">{{ formatDate(item.createdAt) }}</div>
              <h3>{{ item.title }}</h3>
              <p>{{ truncate(item.content, 120) }}</p>
              <span class="news-read-more">Batafsil o'qish →</span>
            </div>
          </article>
        </div>
      </div>
    </section>

    <!-- Modal -->
    <div class="modal-overlay" v-if="selected" @click.self="selected = null">
      <div class="modal-box news-modal">
        <div v-if="selected.imageUrl" class="news-modal-img">
          <img :src="selected.imageUrl" :alt="selected.title" />
        </div>
        <div class="news-modal-body">
          <div class="news-date">{{ formatDate(selected.createdAt) }}</div>
          <h2>{{ selected.title }}</h2>
          <p>{{ selected.content }}</p>
          <button class="btn-primary" @click="selected = null">Yopish</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const news = ref([])
const loading = ref(true)
const selected = ref(null)

const openNews = (item) => { selected.value = item }

const truncate = (text, max) => {
  if (!text) return ''
  return text.length > max ? text.slice(0, max) + '...' : text
}

const formatDate = (dt) => {
  if (!dt) return ''
  return new Date(dt).toLocaleDateString('uz-UZ', { year: 'numeric', month: 'long', day: 'numeric' })
}

onMounted(async () => {
  try {
    const r = await axios.get('http://localhost:8080/api/public/news')
    news.value = r.data || []
  } catch {}
  loading.value = false
})
</script>

<style scoped>
.news-header { background: linear-gradient(135deg, #1a0a00, #2d1500); padding: 60px 0; text-align: center; color: white; }
.news-header .badge { margin-bottom: 16px; display: inline-block; }
.news-header h1 { font-size: clamp(28px, 4vw, 48px); margin: 12px 0; }
.news-header p { color: rgba(255,255,255,0.7); }

.news-loading, .news-empty { display: flex; flex-direction: column; align-items: center; gap: 16px; padding: 80px; color: var(--text-gray); }
.news-empty span { font-size: 56px; }
.news-empty h3 { font-size: 22px; color: var(--primary); }
.spinner-lg { width: 48px; height: 48px; border: 4px solid var(--border); border-top-color: var(--accent); border-radius: 50%; animation: spin 0.8s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }

.news-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 28px; }
.news-card { cursor: pointer; transition: all 0.3s; }
.news-card:hover { transform: translateY(-6px); box-shadow: var(--shadow-lg); }

.news-card-img { height: 220px; overflow: hidden; background: var(--bg-cream); display: flex; align-items: center; justify-content: center; }
.news-card-img img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.5s; }
.news-card:hover .news-card-img img { transform: scale(1.06); }
.news-no-img { font-size: 56px; }

.news-card-body { padding: 20px 22px 24px; }
.news-date { font-size: 12px; color: var(--accent); font-weight: 600; margin-bottom: 8px; text-transform: uppercase; letter-spacing: 0.5px; }
.news-card-body h3 { font-size: 18px; color: var(--primary); margin-bottom: 10px; line-height: 1.3; }
.news-card-body p { font-size: 14px; color: var(--text-gray); line-height: 1.7; margin-bottom: 16px; }
.news-read-more { font-size: 13px; color: var(--accent); font-weight: 600; }

.news-modal { max-width: 640px; padding: 0; overflow: hidden; }
.news-modal-img { height: 280px; overflow: hidden; }
.news-modal-img img { width: 100%; height: 100%; object-fit: cover; }
.news-modal-body { padding: 28px 32px 32px; }
.news-modal-body h2 { font-size: 26px; margin: 8px 0 16px; color: var(--primary); }
.news-modal-body p { color: var(--text-gray); line-height: 1.8; margin-bottom: 24px; white-space: pre-wrap; }

@media (max-width: 600px) { .news-grid { grid-template-columns: 1fr; } }
</style>
