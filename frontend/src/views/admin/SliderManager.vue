<template>
  <div class="admin-page">
    <div class="admin-page-header">
      <div>
        <h2>🖼 Slayd Rasmlar</h2>
        <p>Bosh oyna uchun slaydshow rasmlari</p>
      </div>
      <button class="btn-primary" @click="openAdd">+ Rasm qo'shish</button>
    </div>

    <div class="slides-grid">
      <div v-for="img in slides" :key="img.id" class="slide-card card">
        <div class="slide-preview">
          <img :src="img.imageUrl" :alt="img.caption" @error="e=>e.target.src='https://via.placeholder.com/300x200?text=Rasm'" />
        </div>
        <div class="slide-body">
          <p class="slide-caption">{{ img.caption || 'Sarlavha yo\'q' }}</p>
          <p class="slide-order">Tartib: {{ img.sortOrder }}</p>
          <div class="slide-actions">
            <button class="btn-outline btn-sm" @click="openEdit(img)">✏ Tahrirlash</button>
            <button class="btn-danger btn-sm" @click="deleteSlide(img.id)">🗑 O'chirish</button>
          </div>
        </div>
      </div>
      <div v-if="slides.length === 0 && !loading" class="empty-state">
        <span>🖼</span><p>Hali rasm qo'shilmagan</p>
      </div>
    </div>

    <!-- Modal -->
    <div class="modal-overlay" v-if="showModal" @click.self="showModal = false">
      <div class="modal-box">
        <div class="modal-header">
          <h3 class="modal-title">{{ editing ? 'Rasmni tahrirlash' : 'Yangi rasm qo\'shish' }}</h3>
          <button class="modal-close" @click="showModal = false">×</button>
        </div>
        <div class="form-group">
          <label>Rasm URL manzili *</label>
          <input v-model="form.imageUrl" placeholder="https://example.com/image.jpg" required />
          <div v-if="form.imageUrl" class="url-preview">
            <img :src="form.imageUrl" @error="e=>e.target.style.display='none'" />
          </div>
        </div>
        <div class="form-group">
          <label>Sarlavha (ixtiyoriy)</label>
          <input v-model="form.caption" placeholder="Rasm haqida qisqacha ma'lumot" />
        </div>
        <div class="form-group">
          <label>Tartib raqami</label>
          <input v-model.number="form.sortOrder" type="number" min="0" placeholder="0" />
        </div>
        <div class="form-group" v-if="!editing" style="margin-top: 10px; display: flex; align-items: center; gap: 8px;">
          <input type="checkbox" id="addToNews" v-model="form.addToNews" style="width: 16px; height: 16px; cursor: pointer;">
          <label for="addToNews" style="cursor: pointer; margin-bottom: 0;">Yangiliklarga ham qo'shish</label>
        </div>
        <div style="display:flex;gap:12px;justify-content:flex-end;">
          <button class="btn-ghost" @click="showModal = false">Bekor</button>
          <button class="btn-primary" @click="save" :disabled="saving">{{ saving ? 'Saqlanmoqda...' : 'Saqlash' }}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const slides = ref([])
const loading = ref(true)
const showModal = ref(false)
const editing = ref(null)
const saving = ref(false)
const form = ref({ imageUrl: '', caption: '', sortOrder: 0, addToNews: false })

const load = async () => {
  try { slides.value = (await axios.get('http://localhost:8080/api/public/slider')).data || [] } catch {}
  loading.value = false
}

const openAdd = () => { editing.value = null; form.value = { imageUrl: '', caption: '', sortOrder: slides.value.length, addToNews: false }; showModal.value = true }
const openEdit = (img) => { editing.value = img; form.value = { imageUrl: img.imageUrl, caption: img.caption || '', sortOrder: img.sortOrder || 0, addToNews: false }; showModal.value = true }

const save = async () => {
  if (!form.value.imageUrl) return
  saving.value = true
  try {
    if (editing.value) {
      await axios.put(`http://localhost:8080/api/admin/slider/${editing.value.id}`, form.value)
    } else {
      await axios.post('http://localhost:8080/api/admin/slider', form.value)
      if (form.value.addToNews && form.value.caption) {
        await axios.post('http://localhost:8080/api/admin/news', {
          title: form.value.caption,
          content: "Restoranimizning yangi taklifi yoki e'loni.",
          imageUrl: form.value.imageUrl
        })
      }
    }
    showModal.value = false
    await load()
  } catch (e) { alert('Xatolik yuz berdi') }
  saving.value = false
}

const deleteSlide = async (id) => {
  if (!confirm('Rasmni o\'chirishni tasdiqlaysizmi?')) return
  await axios.delete(`http://localhost:8080/api/admin/slider/${id}`)
  await load()
}

onMounted(load)
</script>

<style scoped>
.admin-page { padding: 32px; }
.admin-page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 28px; }
.admin-page-header h2 { font-size: 24px; color: var(--primary); }
.admin-page-header p { color: var(--text-gray); font-size: 14px; }

.slides-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(260px, 1fr)); gap: 20px; }
.slide-card { overflow: hidden; }
.slide-preview { height: 180px; overflow: hidden; background: var(--bg-cream); }
.slide-preview img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.4s; }
.slide-card:hover .slide-preview img { transform: scale(1.05); }
.slide-body { padding: 16px; }
.slide-caption { font-size: 14px; font-weight: 600; color: var(--primary); margin-bottom: 4px; }
.slide-order { font-size: 12px; color: var(--text-gray); margin-bottom: 12px; }
.slide-actions { display: flex; gap: 8px; }

.empty-state { grid-column: 1/-1; text-align: center; padding: 60px; color: var(--text-gray); }
.empty-state span { font-size: 48px; display: block; margin-bottom: 12px; }

.url-preview { margin-top: 10px; border-radius: 8px; overflow: hidden; max-height: 120px; }
.url-preview img { width: 100%; height: 120px; object-fit: cover; }
</style>
