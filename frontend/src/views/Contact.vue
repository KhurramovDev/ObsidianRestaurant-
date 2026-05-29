<template>
  <div class="contact-page">
    <section class="contact-header">
      <div class="container">
        <span class="badge badge-accent">📞 Aloqa</span>
        <h1>Biz bilan <span class="accent-text">bog'laning</span></h1>
        <p>Manzilimiz, telefon raqamlarimiz va ijtimoiy tarmoqlarimiz</p>
      </div>
    </section>

    <section class="section" style="background:var(--bg-cream);">
      <div class="container">
        <div class="contact-grid">
          <!-- Map -->
          <div class="map-col">
            <div class="map-wrap">
              <iframe
                v-if="footer.mapEmbedUrl"
                :src="footer.mapEmbedUrl"
                width="100%" height="420" style="border:0;" allowfullscreen loading="lazy"
              ></iframe>
              <iframe
                v-else
                src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2995.3565773596897!2d69.2800905!3d41.299698!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x38ae8b0cc379e9c3%3A0xa5a9323b4aa5cb98!2sTashkent%2C%20Yunusobod!5e0!3m2!1suz!2suz!4v1234567890"
                width="100%" height="420" style="border:0;" allowfullscreen loading="lazy"
              ></iframe>
            </div>
          </div>

          <!-- Info -->
          <div class="contact-info-col">
            <div class="info-card">
              <h3>📍 Manzil</h3>
              <p>{{ footer.address || 'Toshkent shahar, Yunusobod tumani, 14-ko\'cha' }}</p>
            </div>
            <div class="info-card">
              <h3>📞 Telefon</h3>
              <p><a :href="`tel:${footer.phone1 || '+998901234567'}`">{{ footer.phone1 || '+998 90 123 45 67' }}</a></p>
              <p v-if="footer.phone2"><a :href="`tel:${footer.phone2}`">{{ footer.phone2 }}</a></p>
            </div>
            <div class="info-card">
              <h3>🕐 Ish vaqti</h3>
              <p>Har kuni: 10:00 – 23:00</p>
            </div>
            <div class="info-card">
              <h3>🌐 Ijtimoiy tarmoqlar</h3>
              <div class="social-links">
                <a :href="footer.instagramUrl || '#'" target="_blank" class="social-link instagram">
                  <svg width="20" height="20" fill="currentColor" viewBox="0 0 24 24"><path d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zm0-2.163c-3.259 0-3.667.014-4.947.072-4.358.2-6.78 2.618-6.98 6.98-.059 1.281-.073 1.689-.073 4.948 0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98-1.281-.059-1.69-.073-4.949-.073zm0 5.838c-3.403 0-6.162 2.759-6.162 6.162s2.759 6.163 6.162 6.163 6.162-2.759 6.162-6.163c0-3.403-2.759-6.162-6.162-6.162zm0 10.162c-2.209 0-4-1.79-4-4 0-2.209 1.791-4 4-4s4 1.791 4 4c0 2.21-1.791 4-4 4zm6.406-11.845c-.796 0-1.441.645-1.441 1.44s.645 1.44 1.441 1.44c.795 0 1.439-.645 1.439-1.44s-.644-1.44-1.439-1.44z"/></svg>
                  Instagram
                </a>
                <a :href="footer.facebookUrl || '#'" target="_blank" class="social-link facebook">
                  <svg width="20" height="20" fill="currentColor" viewBox="0 0 24 24"><path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/></svg>
                  Facebook
                </a>
                <a :href="footer.telegramUrl || '#'" target="_blank" class="social-link telegram">
                  <svg width="20" height="20" fill="currentColor" viewBox="0 0 24 24"><path d="M11.944 0A12 12 0 0 0 0 12a12 12 0 0 0 12 12 12 12 0 0 0 12-12A12 12 0 0 0 12 0a12 12 0 0 0-.056 0zm4.962 7.224c.1-.002.321.023.465.14a.506.506 0 0 1 .171.325c.016.093.036.306.02.472-.18 1.898-.962 6.502-1.36 8.627-.168.9-.499 1.201-.82 1.23-.696.065-1.225-.46-1.9-.902-1.056-.693-1.653-1.124-2.678-1.8-1.185-.78-.417-1.21.258-1.91.177-.184 3.247-2.977 3.307-3.23.007-.032.014-.15-.056-.212s-.174-.041-.249-.024c-.106.024-1.793 1.14-5.061 3.345-.48.33-.913.49-1.302.48-.428-.008-1.252-.241-1.865-.44-.752-.245-1.349-.374-1.297-.789.027-.216.325-.437.893-.663 3.498-1.524 5.83-2.529 6.998-3.014 3.332-1.386 4.025-1.627 4.476-1.635z"/></svg>
                  Telegram
                </a>
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
const footer = ref({})
onMounted(() => {
  axios.get('http://localhost:8080/api/public/footer').then(r => { footer.value = r.data || {} }).catch(() => {})
})
</script>

<style scoped>
.contact-header { background: linear-gradient(135deg, #1a0a00, #2d1500); padding: 60px 0; text-align: center; color: white; }
.contact-header .badge { margin-bottom: 16px; display: inline-block; }
.contact-header h1 { font-size: clamp(28px, 4vw, 48px); margin: 12px 0; }
.contact-header p { color: rgba(255,255,255,0.7); }

.contact-grid { display: grid; grid-template-columns: 1.5fr 1fr; gap: 40px; align-items: start; }
.map-wrap { border-radius: 20px; overflow: hidden; box-shadow: var(--shadow-lg); }

.contact-info-col { display: flex; flex-direction: column; gap: 16px; }
.info-card { background: white; border-radius: 16px; padding: 20px 24px; border: 1px solid var(--border); box-shadow: var(--shadow-sm); }
.info-card h3 { font-size: 15px; font-weight: 700; color: var(--primary); margin-bottom: 10px; }
.info-card p { font-size: 14px; color: var(--text-gray); margin-bottom: 4px; }
.info-card a { color: var(--accent); font-weight: 600; }
.info-card a:hover { color: var(--accent-dark); }

.social-links { display: flex; flex-direction: column; gap: 10px; }
.social-link {
  display: flex; align-items: center; gap: 10px;
  padding: 10px 16px; border-radius: 10px;
  font-size: 14px; font-weight: 600;
  transition: all 0.2s; color: white;
}
.instagram { background: linear-gradient(135deg, #e1306c, #833ab4); }
.facebook { background: #1877f2; }
.telegram { background: #0088cc; }
.social-link:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.2); }

@media (max-width: 768px) { .contact-grid { grid-template-columns: 1fr; } }
</style>
