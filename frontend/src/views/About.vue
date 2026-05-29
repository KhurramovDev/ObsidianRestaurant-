<template>
  <div class="about-page">
    <!-- Hero -->
    <section class="about-hero">
      <div class="container">
        <div class="about-hero-inner">
          <div class="about-hero-text">
            <span class="badge badge-accent">🏛 {{ t('nav.about') }}</span>
            <h1>{{ t('about.hero_title') }}</h1>
            <p>{{ t('about.hero_sub') }}</p>
          </div>
          <div class="about-hero-img">
            <img src="https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=700&q=80" alt="Restaurant interior" />
          </div>
        </div>
      </div>
    </section>

    <!-- Story -->
    <section class="section" style="background:white;">
      <div class="container">
        <div class="about-grid">
          <div class="about-img-col">
            <img src="https://images.unsplash.com/photo-1577219491135-ce391730fb2c?w=600&q=80" alt="Chef" class="about-img" />
            <img src="https://images.unsplash.com/photo-1551218808-94e220e084d2?w=600&q=80" alt="Food" class="about-img offset" />
          </div>
          <div class="about-content">
            <span class="badge badge-accent">{{ t('about.story_badge') }}</span>
            <h2>{{ t('about.story_title') }}</h2>
            <p>{{ t('about.story_p1') }}</p>
            <p>{{ t('about.story_p2') }}</p>

            <div class="about-values">
              <div class="value-item">
                <span class="value-icon">❤️</span>
                <div>
                  <strong>{{ t('about.value_quality') }}</strong>
                  <p>{{ t('about.value_quality_desc') }}</p>
                </div>
              </div>
              <div class="value-item">
                <span class="value-icon">👨‍🍳</span>
                <div>
                  <strong>{{ t('about.value_skill') }}</strong>
                  <p>{{ t('about.value_skill_desc') }}</p>
                </div>
              </div>
              <div class="value-item">
                <span class="value-icon">🌿</span>
                <div>
                  <strong>{{ t('about.value_natural') }}</strong>
                  <p>{{ t('about.value_natural_desc') }}</p>
                </div>
              </div>
              <div class="value-item">
                <span class="value-icon">🏆</span>
                <div>
                  <strong>{{ t('about.value_award') }}</strong>
                  <p>{{ t('about.value_award_desc') }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Stats -->
    <section class="about-stats">
      <div class="container">
        <div class="stats-grid">
          <div class="stat-card">
            <strong>15+</strong>
            <span>{{ t('about.stats_exp') }}</span>
          </div>
          <div class="stat-card">
            <strong>500+</strong>
            <span>{{ t('about.stats_dishes') }}</span>
          </div>
          <div class="stat-card">
            <strong>10,000+</strong>
            <span>{{ t('about.stats_clients') }}</span>
          </div>
          <div class="stat-card">
            <strong>50+</strong>
            <span>{{ t('about.stats_chefs') }}</span>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA -->
    <section class="about-cta">
      <div class="container">
        <div class="cta-box">
          <h2>{{ t('about.cta_title') }}</h2>
          <p>{{ t('about.cta_sub') }}</p>
          <div style="display:flex;gap:16px;justify-content:center;flex-wrap:wrap;">
            <router-link to="/menu" class="btn-primary">{{ t('hero.btn_menu') }}</router-link>
            <router-link to="/contact" class="btn-outline" style="color:#c8922a;border-color:#c8922a;">{{ t('nav.contact') }}</router-link>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { useI18n } from '../i18n'
const { t } = useI18n()
</script>

<style scoped>
.about-hero { background: linear-gradient(135deg, #1a0a00, #2d1500); padding: 80px 0; color: white; }
.about-hero-inner { display: grid; grid-template-columns: 1fr 1fr; gap: 60px; align-items: center; }
.about-hero-text h1 { font-size: clamp(28px, 4vw, 48px); margin: 16px 0; }
.about-hero-text p { color: rgba(255,255,255,0.7); font-size: 17px; line-height: 1.8; }
.about-hero-img { border-radius: 20px; overflow: hidden; box-shadow: 0 24px 60px rgba(0,0,0,0.4); }
.about-hero-img img { width: 100%; height: 380px; object-fit: cover; }
.about-grid { display: grid; grid-template-columns: 1fr 1.2fr; gap: 64px; align-items: start; }
.about-img-col { position: relative; }
.about-img { width: 100%; border-radius: 16px; box-shadow: var(--shadow-lg); }
.about-img.offset { margin-top: -60px; margin-left: 40px; width: calc(100% - 40px); }
.about-content h2 { font-size: 32px; color: var(--primary); margin: 12px 0 20px; }
.about-content p { color: var(--text-gray); line-height: 1.8; margin-bottom: 16px; }
.about-values { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-top: 28px; }
.value-item { display: flex; gap: 12px; align-items: flex-start; padding: 16px; background: var(--bg-cream); border-radius: 12px; border: 1px solid var(--border); }
.value-icon { font-size: 24px; flex-shrink: 0; }
.value-item strong { display: block; font-size: 14px; color: var(--primary); margin-bottom: 2px; }
.value-item p { font-size: 12px; color: var(--text-gray); margin: 0; }
.about-stats { background: var(--accent); padding: 60px 0; }
.stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 32px; text-align: center; }
.stat-card strong { display: block; font-size: 42px; font-weight: 800; color: white; font-family: 'Playfair Display', serif; }
.stat-card span { font-size: 14px; color: rgba(255,255,255,0.8); }
.about-cta { padding: 80px 0; background: var(--bg-cream); }
.cta-box { background: linear-gradient(135deg, #1a0a00, #2d1500); border-radius: 24px; padding: 64px; text-align: center; color: white; }
.cta-box h2 { font-size: 36px; margin-bottom: 12px; }
.cta-box p { color: rgba(255,255,255,0.7); margin-bottom: 32px; font-size: 16px; }
@media (max-width: 900px) { .about-hero-inner, .about-grid { grid-template-columns: 1fr; gap: 32px; } .stats-grid { grid-template-columns: repeat(2, 1fr); } .about-values { grid-template-columns: 1fr; } .about-img.offset { margin: 0; width: 100%; } }
</style>
