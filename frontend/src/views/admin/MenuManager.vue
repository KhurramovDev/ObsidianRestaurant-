<template>
  <div class="admin-page">
    <div class="admin-page-header">
      <div><h2>📖 Menyu Boshqarish</h2><p>Kategoriya va taomlarni boshqarish</p></div>
      <button class="btn-primary" @click="openAddCat">+ Kategoriya qo'shish</button>
    </div>

    <div class="menu-admin-layout">
      <!-- CATEGORIES -->
      <div class="cats-panel card">
        <h3 class="panel-title">Bo'limlar (Varaqlari)</h3>
        <div class="cats-list">
          <div
            v-for="cat in categories" :key="cat.id"
            class="cat-row"
            :class="{ active: selectedCat?.id === cat.id }"
            @click="selectCat(cat)"
          >
            <span class="cat-num-badge">{{ cat.pageNumber }}</span>
            <span class="cat-name">{{ cat.title }}</span>
            <div class="cat-row-actions">
              <button class="icon-btn" @click.stop="openEditCat(cat)" title="Tahrirlash">✏</button>
              <button class="icon-btn danger" @click.stop="deleteCat(cat.id)" title="O'chirish">🗑</button>
            </div>
          </div>
          <div v-if="categories.length === 0" class="empty-cats">Bo'lim yo'q</div>
        </div>
      </div>

      <!-- ITEMS -->
      <div class="items-panel">
        <div class="items-panel-head">
          <h3 class="panel-title" v-if="selectedCat">{{ selectedCat.title }} — Taomlar</h3>
          <h3 class="panel-title" v-else>Kategoriya tanlang</h3>
          <button v-if="selectedCat" class="btn-primary btn-sm" @click="openAddItem">+ Taom qo'shish</button>
        </div>

        <div v-if="selectedCat" class="table-wrap">
          <table>
            <thead>
              <tr><th>Rasm</th><th>Nomi</th><th>Narxi</th><th>Yulduz</th><th>Amallar</th></tr>
            </thead>
            <tbody>
              <tr v-for="item in items" :key="item.id">
                <td>
                  <img v-if="item.imageUrl" :src="item.imageUrl" class="tbl-img" @error="e=>e.target.style.display='none'" />
                  <span v-else>🍽</span>
                </td>
                <td><strong>{{ item.name }}</strong></td>
                <td>{{ formatPrice(item.price) }} so'm</td>
                <td>
                  <span v-for="s in 5" :key="s" :style="{color: s<=(item.rating||4)?'#c8922a':'#ddd'}">★</span>
                </td>
                <td>
                  <div style="display:flex;gap:8px;">
                    <button class="btn-outline btn-sm" @click="openEditItem(item)">✏</button>
                    <button class="btn-danger btn-sm" @click="deleteItem(item.id)">🗑</button>
                  </div>
                </td>
              </tr>
              <tr v-if="items.length===0"><td colspan="5" style="text-align:center;padding:32px;color:#9ca3af;">Bu bo'limda taom yo'q</td></tr>
            </tbody>
          </table>
        </div>
        <div v-else class="select-hint"><span>👈</span><p>Chap tarafdan bo'lim tanlang</p></div>
      </div>
    </div>

    <!-- Category Modal -->
    <div class="modal-overlay" v-if="showCatModal" @click.self="showCatModal=false">
      <div class="modal-box">
        <div class="modal-header">
          <h3 class="modal-title">{{ editingCat ? 'Bo\'limni tahrirlash' : 'Yangi bo\'lim (Varaq)' }}</h3>
          <button class="modal-close" @click="showCatModal=false">×</button>
        </div>
        <div class="form-group"><label>Sarlavha *</label><input v-model="catForm.title" placeholder="Masalan: Milliy taomlar" /></div>
        <div class="form-group"><label>Varaq raqami *</label><input v-model.number="catForm.pageNumber" type="number" min="1" placeholder="1" /></div>
        <div style="display:flex;gap:12px;justify-content:flex-end;">
          <button class="btn-ghost" @click="showCatModal=false">Bekor</button>
          <button class="btn-primary" @click="saveCat" :disabled="savingCat">{{ savingCat ? 'Saqlanmoqda...' : 'Saqlash' }}</button>
        </div>
      </div>
    </div>

    <!-- Item Modal -->
    <div class="modal-overlay" v-if="showItemModal" @click.self="showItemModal=false">
      <div class="modal-box">
        <div class="modal-header">
          <h3 class="modal-title">{{ editingItem ? 'Taomni tahrirlash' : 'Yangi taom qo\'shish' }}</h3>
          <button class="modal-close" @click="showItemModal=false">×</button>
        </div>
        <div class="form-group"><label>Taom nomi *</label><input v-model="itemForm.name" placeholder="Taom nomi" /></div>
        <div class="form-group"><label>Narxi (so'm) *</label><input v-model.number="itemForm.price" type="number" min="0" placeholder="25000" /></div>
        <div class="form-group"><label>Rasm URL (ixtiyoriy)</label><input v-model="itemForm.imageUrl" placeholder="https://..." /></div>
        <div class="form-group">
          <label>Yulduz reytingi (avtomatik 4)</label>
          <div class="star-select">
            <button v-for="s in 5" :key="s" type="button" @click="itemForm.rating=s" class="star-btn" :class="{active: s<=itemForm.rating}">★</button>
          </div>
        </div>
        <div style="display:flex;gap:12px;justify-content:flex-end;">
          <button class="btn-ghost" @click="showItemModal=false">Bekor</button>
          <button class="btn-primary" @click="saveItem" :disabled="savingItem">{{ savingItem ? 'Saqlanmoqda...' : 'Saqlash' }}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const categories = ref([])
const selectedCat = ref(null)
const items = ref([])

const showCatModal = ref(false)
const editingCat = ref(null)
const savingCat = ref(false)
const catForm = ref({ title: '', pageNumber: 1 })

const showItemModal = ref(false)
const editingItem = ref(null)
const savingItem = ref(false)
const itemForm = ref({ name: '', price: '', imageUrl: '', rating: 4 })

const formatPrice = (p) => Number(p || 0).toLocaleString('uz-UZ')

const loadCats = async () => {
  try { categories.value = (await axios.get('http://localhost:8080/api/public/menu/categories')).data || [] } catch {}
}
const selectCat = async (cat) => {
  selectedCat.value = cat
  try { items.value = (await axios.get(`http://localhost:8080/api/public/menu/items/${cat.id}`)).data || [] } catch { items.value = [] }
}

const openAddCat = () => { editingCat.value = null; catForm.value = { title: '', pageNumber: categories.value.length + 1 }; showCatModal.value = true }
const openEditCat = (cat) => { editingCat.value = cat; catForm.value = { title: cat.title, pageNumber: cat.pageNumber }; showCatModal.value = true }

const saveCat = async () => {
  if (!catForm.value.title) return alert('Sarlavha kiriting!')
  savingCat.value = true
  try {
    if (editingCat.value) {
      await axios.put(`http://localhost:8080/api/admin/menu/categories/${editingCat.value.id}`, catForm.value)
    } else {
      await axios.post('http://localhost:8080/api/admin/menu/categories', catForm.value)
    }
    showCatModal.value = false; await loadCats()
  } catch { alert('Xatolik!') }
  savingCat.value = false
}

const deleteCat = async (id) => {
  if (!confirm('Bo\'limni o\'chirishni tasdiqlaysizmi?')) return
  await axios.delete(`http://localhost:8080/api/admin/menu/categories/${id}`)
  if (selectedCat.value?.id === id) { selectedCat.value = null; items.value = [] }
  await loadCats()
}

const openAddItem = () => { editingItem.value = null; itemForm.value = { name: '', price: '', imageUrl: '', rating: 4 }; showItemModal.value = true }
const openEditItem = (item) => {
  editingItem.value = item
  itemForm.value = { name: item.name, price: item.price, imageUrl: item.imageUrl || '', rating: item.rating || 4 }
  showItemModal.value = true
}

const saveItem = async () => {
  if (!itemForm.value.name || !itemForm.value.price) return alert('Nom va narxi kiriting!')
  savingItem.value = true
  try {
    const payload = { ...itemForm.value, category: { id: selectedCat.value.id } }
    if (editingItem.value) {
      await axios.put(`http://localhost:8080/api/admin/menu/items/${editingItem.value.id}`, payload)
    } else {
      await axios.post('http://localhost:8080/api/admin/menu/items', payload)
    }
    showItemModal.value = false; await selectCat(selectedCat.value)
  } catch { alert('Xatolik!') }
  savingItem.value = false
}

const deleteItem = async (id) => {
  if (!confirm('Taomni o\'chirishni tasdiqlaysizmi?')) return
  await axios.delete(`http://localhost:8080/api/admin/menu/items/${id}`)
  await selectCat(selectedCat.value)
}

onMounted(loadCats)
</script>

<style scoped>
.admin-page { padding: 32px; }
.admin-page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 28px; }
.admin-page-header h2 { font-size: 24px; color: var(--primary); }
.admin-page-header p { color: var(--text-gray); font-size: 14px; }

.menu-admin-layout { display: grid; grid-template-columns: 280px 1fr; gap: 24px; align-items: start; }

.cats-panel { padding: 20px; }
.panel-title { font-size: 15px; font-weight: 700; color: var(--primary); margin-bottom: 16px; }
.cats-list { display: flex; flex-direction: column; gap: 4px; }
.cat-row {
  display: flex; align-items: center; gap: 10px;
  padding: 10px 12px; border-radius: 10px;
  cursor: pointer; transition: all 0.2s;
  border: 1px solid transparent;
}
.cat-row:hover { background: var(--bg-cream); }
.cat-row.active { background: rgba(200,146,42,0.1); border-color: rgba(200,146,42,0.3); }
.cat-num-badge { width: 26px; height: 26px; background: rgba(200,146,42,0.12); color: var(--accent); border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 11px; font-weight: 700; flex-shrink: 0; }
.cat-name { flex: 1; font-size: 14px; font-weight: 500; }
.cat-row-actions { display: flex; gap: 4px; opacity: 0; transition: opacity 0.2s; }
.cat-row:hover .cat-row-actions { opacity: 1; }
.icon-btn { background: none; border: none; cursor: pointer; padding: 4px 6px; border-radius: 6px; font-size: 14px; transition: background 0.2s; }
.icon-btn:hover { background: var(--bg-cream); }
.icon-btn.danger:hover { background: #fef2f2; }
.empty-cats { text-align: center; color: var(--text-light); padding: 20px; font-size: 13px; }

.items-panel-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.tbl-img { width: 48px; height: 38px; object-fit: cover; border-radius: 6px; }
.select-hint { display: flex; flex-direction: column; align-items: center; gap: 12px; padding: 60px; color: var(--text-light); }
.select-hint span { font-size: 40px; }

.star-select { display: flex; gap: 6px; margin-top: 4px; }
.star-btn { background: none; border: none; font-size: 24px; cursor: pointer; color: #ddd; transition: color 0.2s; padding: 2px; }
.star-btn.active { color: #c8922a; }
button:disabled { opacity: 0.6; cursor: not-allowed; }

@media (max-width: 800px) { .menu-admin-layout { grid-template-columns: 1fr; } }
</style>
