<template>
  <div class="admin-page">
    <div class="admin-page-header">
      <div><h2>📰 Yangiliklar</h2><p>Yangiliklar bo'limini boshqarish</p></div>
      <button class="btn-primary" @click="openAdd">+ Yangilik qo'shish</button>
    </div>

    <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Rasm</th>
            <th>Sarlavha</th>
            <th>Matn</th>
            <th>Sana</th>
            <th>Amallar</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in news" :key="item.id">
            <td>
              <img v-if="item.imageUrl" :src="item.imageUrl" class="tbl-img" @error="e=>e.target.style.display='none'" />
              <span v-else>📋</span>
            </td>
            <td><strong>{{ item.title }}</strong></td>
            <td class="txt-clip">{{ truncate(item.content, 60) }}</td>
            <td style="white-space:nowrap;">{{ formatDate(item.createdAt) }}</td>
            <td>
              <div style="display:flex;gap:8px;">
                <button class="btn-outline btn-sm" @click="openEdit(item)">✏</button>
                <button class="btn-danger btn-sm" @click="del(item.id)">🗑</button>
              </div>
            </td>
          </tr>
          <tr v-if="news.length === 0"><td colspan="5" style="text-align:center;padding:40px;color:#9ca3af;">Hali yangilik qo'shilmagan</td></tr>
        </tbody>
      </table>
    </div>

    <!-- Modal -->
    <div class="modal-overlay" v-if="showModal" @click.self="showModal=false">
      <div class="modal-box">
        <div class="modal-header">
          <h3 class="modal-title">{{ editing ? 'Yangilikni tahrirlash' : 'Yangi yangilik' }}</h3>
          <button class="modal-close" @click="showModal=false">×</button>
        </div>
        <div class="form-group">
          <label>Sarlavha *</label>
          <input v-model="form.title" placeholder="Yangilik sarlavhasi" required />
        </div>
        <div class="form-group">
          <label>Matn *</label>
          <textarea v-model="form.content" rows="5" placeholder="Yangilik matni..." required style="resize:vertical;"></textarea>
        </div>
        <div class="form-group">
          <label>Rasm URL (ixtiyoriy)</label>
          <input v-model="form.imageUrl" placeholder="https://..." />
          <div v-if="form.imageUrl" class="url-preview"><img :src="form.imageUrl" @error="e=>e.target.style.display='none'" /></div>
        </div>
        <div style="display:flex;gap:12px;justify-content:flex-end;">
          <button class="btn-ghost" @click="showModal=false">Bekor</button>
          <button class="btn-primary" @click="save" :disabled="saving">{{ saving ? 'Saqlanmoqda...' : 'Saqlash' }}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const news = ref([])
const showModal = ref(false)
const editing = ref(null)
const saving = ref(false)
const form = ref({ title: '', content: '', imageUrl: '' })

const truncate = (t, n) => t && t.length > n ? t.slice(0, n) + '...' : (t || '')
const formatDate = (d) => d ? new Date(d).toLocaleDateString('uz-UZ') : ''

const load = async () => {
  try { news.value = (await axios.get('http://localhost:8080/api/public/news')).data || [] } catch {}
}
const openAdd = () => { editing.value = null; form.value = { title: '', content: '', imageUrl: '' }; showModal.value = true }
const openEdit = (item) => { editing.value = item; form.value = { title: item.title, content: item.content, imageUrl: item.imageUrl || '' }; showModal.value = true }

const save = async () => {
  if (!form.value.title || !form.value.content) return alert('Sarlavha va matn to\'ldiring!')
  saving.value = true
  try {
    if (editing.value) {
      await axios.put(`http://localhost:8080/api/admin/news/${editing.value.id}`, form.value)
    } else {
      await axios.post('http://localhost:8080/api/admin/news', form.value)
    }
    showModal.value = false; await load()
  } catch { alert('Xatolik!') }
  saving.value = false
}

const del = async (id) => {
  if (!confirm('O\'chirishni tasdiqlaysizmi?')) return
  await axios.delete(`http://localhost:8080/api/admin/news/${id}`)
  await load()
}

onMounted(load)
</script>

<style scoped>
.admin-page { padding: 32px; }
.admin-page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 28px; }
.admin-page-header h2 { font-size: 24px; color: var(--primary); }
.admin-page-header p { color: var(--text-gray); font-size: 14px; }
.tbl-img { width: 52px; height: 40px; object-fit: cover; border-radius: 6px; }
.txt-clip { max-width: 200px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; color: var(--text-gray); font-size: 13px; }
.url-preview { margin-top: 8px; border-radius: 8px; overflow: hidden; max-height: 100px; }
.url-preview img { width: 100%; height: 100px; object-fit: cover; }
button:disabled { opacity: 0.6; cursor: not-allowed; }
</style>
