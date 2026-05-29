<template>
  <div class="home">
    <!-- HERO SECTION -->
    <section class="hero">
      <div class="container">
        <div class="hero-inner">
          <!-- LEFT TEXT -->
          <div class="hero-text">
            <span class="badge badge-accent">{{ t('hero.badge') }}</span>
            <h1>{{ t('hero.title') }}</h1>
            <p>{{ t('hero.sub') }}</p>
            <div class="hero-actions">
              <router-link to="/menu" class="btn-primary">{{ t('hero.btn_menu') }}</router-link>
              <router-link to="/about" class="btn-outline" style="color:#c8922a;border-color:#c8922a;">{{ t('hero.btn_about') }}</router-link>
            </div>
            <div class="hero-stats">
              <div class="stat"><strong>500+</strong><span>{{ t('hero.stat_dishes') }}</span></div>
              <div class="stat-divider"></div>
              <div class="stat"><strong>10K+</strong><span>{{ t('hero.stat_clients') }}</span></div>
              <div class="stat-divider"></div>
              <div class="stat"><strong>15</strong><span>{{ t('hero.stat_experience') }}</span></div>
            </div>
          </div>

          <!-- RIGHT SLIDESHOW -->
          <div class="hero-slider">
            <div class="slider-wrap">
              <transition-group name="slide-fade" tag="div" class="slider-stage">
                <img
                  v-for="(img, i) in sliderImages"
                  :key="i"
                  :src="img"
                  v-show="currentSlide === i"
                  alt="Restaurant"
                  class="slider-img"
                  @error="onImgError($event)"
                />
              </transition-group>
              <div class="slider-dots">
                <button
                  v-for="(img, i) in sliderImages"
                  :key="i"
                  class="dot"
                  :class="{ active: currentSlide === i }"
                  @click="currentSlide = i"
                ></button>
              </div>
              <button class="slider-arrow left" @click="prevSlide">&#8249;</button>
              <button class="slider-arrow right" @click="nextSlide">&#8250;</button>
            </div>
            <div class="slider-glow"></div>
          </div>
        </div>
      </div>
      <div class="hero-bg-circle"></div>
    </section>

    <!-- SECTION CARDS -->
    <section class="sections-preview">
      <div class="container">
        <div class="section-header">
          <span class="badge badge-accent">{{ t('home.services_badge') }}</span>
          <h2 class="section-title">{{ t('home.services_title') }}</h2>
          <p class="section-subtitle">{{ t('home.services_sub') }}</p>
        </div>

        <!-- About Preview -->
        <div class="preview-row">
          <div class="preview-text">
            <span class="badge badge-accent mb-16">🏛 {{ t('nav.about') }}</span>
            <h2>{{ t('home.about_title') }}</h2>
            <p>{{ t('home.about_desc') }}</p>
            <router-link to="/about" class="btn-primary">{{ t('home.more') }}</router-link>
          </div>
          <div class="preview-image">
            <img src="https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=600&q=80" alt="Restoran" />
          </div>
        </div>

        <!-- Menu Preview -->
        <div class="preview-row reverse">
          <div class="preview-image">
            <img src="https://images.unsplash.com/photo-1544025162-d76694265947?w=600&q=80" alt="Menyu" />
          </div>
          <div class="preview-text">
            <span class="badge badge-accent mb-16">🍽 {{ t('nav.menu') }}</span>
            <h2>{{ t('home.menu_title') }}</h2>
            <p>{{ t('home.menu_desc') }}</p>
            <router-link to="/menu" class="btn-primary">{{ t('home.more') }}</router-link>
          </div>
        </div>

        <!-- News Preview -->
        <div class="preview-row">
          <div class="preview-text">
            <span class="badge badge-accent mb-16">📰 {{ t('nav.news') }}</span>
            <h2>{{ t('home.news_title') }}</h2>
            <p>{{ t('home.news_desc') }}</p>
            <router-link to="/news" class="btn-primary">{{ t('home.more') }}</router-link>
          </div>
          <div class="preview-image">
            <img src="https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=600&q=80" alt="Yangiliklar" />
          </div>
        </div>

        <!-- Contact Preview -->
        <div class="preview-row reverse">
          <div class="preview-image">
            <img src="https://images.unsplash.com/photo-1559339352-11d035aa65de?w=600&q=80" alt="Aloqa" />
          </div>
          <div class="preview-text">
            <span class="badge badge-accent mb-16">📞 {{ t('nav.contact') }}</span>
            <h2>{{ t('home.contact_title') }}</h2>
            <p>{{ t('home.contact_desc') }}</p>
            <router-link to="/contact" class="btn-primary">{{ t('home.more') }}</router-link>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import axios from 'axios'
import { useI18n } from '../i18n'

const { t } = useI18n()

const defaultImages = [
  'https://images.unsplash.com/photo-1514933651103-005eec06c04b?w=700&q=80',
  'https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?w=700&q=80',
  'https://images.unsplash.com/photo-1559339352-11d035aa65de?w=700&q=80',
]

const sliderImages = ref([...defaultImages])
const currentSlide = ref(0)
let timer = null

const nextSlide = () => { currentSlide.value = (currentSlide.value + 1) % sliderImages.value.length }
const prevSlide = () => { currentSlide.value = (currentSlide.value - 1 + sliderImages.value.length) % sliderImages.value.length }
const onImgError = (e) => { e.target.src = defaultImages[0] }

onMounted(() => {
  axios.get('http://localhost:8080/api/public/slider')
    .then(r => { if (r.data?.length > 0) sliderImages.value = r.data.map(s => s.imageUrl) })
    .catch(() => {})
  timer = setInterval(nextSlide, 4500)
})
onUnmounted(() => clearInterval(timer))
</script>

<style scoped>
.hero { background: linear-gradient(135deg, #1a0a00 0%, #2d1500 50%, #1a0a00 100%); min-height: 90vh; display: flex; align-items: center; position: relative; overflow: hidden; padding: 60px 0; }
.hero-inner { display: grid; grid-template-columns: 1fr 1fr; gap: 64px; align-items: center; }
.hero-text { color: white; }
.hero-text h1 { font-size: clamp(36px, 5vw, 58px); font-weight: 800; margin: 16px 0 20px; line-height: 1.15; }
.hero-text p { font-size: 17px; color: rgba(255,255,255,0.7); line-height: 1.8; margin-bottom: 32px; max-width: 440px; }
.hero-actions { display: flex; gap: 14px; margin-bottom: 40px; }
.hero-stats { display: flex; align-items: center; gap: 20px; }
.stat strong { display: block; font-size: 24px; font-weight: 700; color: #c8922a; }
.stat span { font-size: 12px; color: rgba(255,255,255,0.5); text-transform: uppercase; }
.stat-divider { width: 1px; height: 36px; background: rgba(255,255,255,0.15); }
.hero-slider { position: relative; }
.slider-wrap { border-radius: 24px; overflow: hidden; box-shadow: 0 32px 80px rgba(0,0,0,0.5); aspect-ratio: 4/3; }
.slider-img { position: absolute; inset: 0; width: 100%; height: 100%; object-fit: cover; }
.sections-preview { padding: 100px 0; background: var(--bg-cream); }
.preview-row { display: grid; grid-template-columns: 1fr 1fr; gap: 80px; align-items: center; margin-bottom: 100px; }
.preview-row.reverse .preview-text { order: 2; }
.preview-row.reverse .preview-image { order: 1; }
.preview-text h2 { font-size: clamp(24px, 3vw, 36px); color: var(--primary); margin-bottom: 16px; }
.preview-text p { color: var(--text-gray); font-size: 16px; line-height: 1.8; margin-bottom: 28px; }
.preview-image { border-radius: 20px; overflow: hidden; box-shadow: 0 20px 60px rgba(0,0,0,0.15); }
.preview-image img { width: 100%; height: 400px; object-fit: cover; }
@media (max-width: 900px) { .hero-inner, .preview-row { grid-template-columns: 1fr; gap: 40px; } .preview-row.reverse .preview-text, .preview-row.reverse .preview-image { order: 0; } }
</style>
