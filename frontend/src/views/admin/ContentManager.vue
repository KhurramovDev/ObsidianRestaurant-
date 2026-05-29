<template>
  <div class="content-manager">
    <h2 class="text-2xl font-bold text-gray-800 mb-6">Kontent Boshqaruvi</h2>

    <div class="grid gap-8">
      <!-- About Section Management -->
      <div class="bg-white p-6 rounded-xl shadow border-l-4 border-blue-500">
        <h3 class="text-xl font-bold text-primary mb-4">Biz Haqimizda (Matn va Rasm)</h3>
        <div class="mb-4">
          <label class="block text-sm text-gray-600 mb-1">Sarlavha</label>
          <input v-model="aboutData.title" type="text" class="w-full px-4 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none">
        </div>
        <div class="mb-4">
          <label class="block text-sm text-gray-600 mb-1">Matn</label>
          <textarea v-model="aboutData.description" rows="5" class="w-full p-4 border rounded focus:ring-2 focus:ring-blue-500 outline-none"></textarea>
        </div>
        <div class="mb-4">
          <label class="block text-sm text-gray-600 mb-1">Rasm URL</label>
          <input v-model="aboutData.imageUrl" type="text" class="w-full px-4 py-2 border rounded focus:ring-2 focus:ring-blue-500 outline-none">
        </div>
        <button @click="saveAbout" class="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700" :disabled="savingAbout">{{ savingAbout ? 'Saqlanmoqda...' : 'Saqlash' }}</button>
      </div>

      <!-- Footer Management -->
      <div class="bg-white p-6 rounded-xl shadow border-l-4 border-purple-500">
        <h3 class="text-xl font-bold text-primary mb-4">Footer (Pastki qism va Aloqa)</h3>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
          <div>
            <label class="block text-sm text-gray-600 mb-1">Restoran nomi</label>
            <input v-model="footer.restaurantName" type="text" class="w-full px-4 py-2 border rounded">
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">Qisqa ta'rif</label>
            <input v-model="footer.description" type="text" class="w-full px-4 py-2 border rounded">
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">Manzil</label>
            <input v-model="footer.address" type="text" class="w-full px-4 py-2 border rounded">
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">Asosiy Telefon</label>
            <input v-model="footer.phone1" type="text" class="w-full px-4 py-2 border rounded">
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">Qo'shimcha Telefon</label>
            <input v-model="footer.phone2" type="text" class="w-full px-4 py-2 border rounded">
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">Google Xarita Embed URL</label>
            <input v-model="footer.mapEmbedUrl" type="text" class="w-full px-4 py-2 border rounded">
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">Instagram URL</label>
            <input v-model="footer.instagramUrl" type="text" class="w-full px-4 py-2 border rounded">
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">Telegram URL</label>
            <input v-model="footer.telegramUrl" type="text" class="w-full px-4 py-2 border rounded">
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">Facebook URL</label>
            <input v-model="footer.facebookUrl" type="text" class="w-full px-4 py-2 border rounded">
          </div>
        </div>
        <button @click="saveFooter" class="bg-purple-600 text-white px-6 py-2 rounded hover:bg-purple-700" :disabled="savingFooter">{{ savingFooter ? 'Saqlanmoqda...' : 'Saqlash' }}</button>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const aboutData = ref({
  sectionName: 'ABOUT',
  title: 'Bizning restoran tarixi va an\'analari',
  description: 'Obsidian 2009-yilda tashkil etilgan...',
  imageUrl: 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=600&q=80'
})

const footer = ref({
  restaurantName: 'Obsidian',
  description: 'Eng mazali taomlar va unutilmas muhit.',
  address: 'Toshkent shahar, Yunusobod tumani',
  phone1: '+998 90 123 45 67',
  phone2: '',
  instagramUrl: '',
  facebookUrl: '',
  telegramUrl: '',
  mapEmbedUrl: ''
})

const savingAbout = ref(false)
const savingFooter = ref(false)

onMounted(async () => {
  try {
    const contentRes = await axios.get('http://localhost:8080/api/admin/content')
    const about = contentRes.data?.find(c => c.sectionName === 'ABOUT')
    if (about) aboutData.value = about
    
    const footerRes = await axios.get('http://localhost:8080/api/admin/footer')
    if (footerRes.data && footerRes.data.id) {
      footer.value = footerRes.data
    }
  } catch (error) {
    console.error('Data load error', error)
  }
})

const saveAbout = async () => {
  savingAbout.value = true
  try {
    await axios.post('http://localhost:8080/api/admin/content', aboutData.value)
    alert("Biz haqimizda ma'lumotlari saqlandi!")
  } catch (error) {
    alert("Xatolik yuz berdi")
  }
  savingAbout.value = false
}

const saveFooter = async () => {
  savingFooter.value = true
  try {
    await axios.post('http://localhost:8080/api/admin/footer', footer.value)
    alert("Footer sozlamalari saqlandi!")
  } catch (error) {
    alert("Xatolik yuz berdi")
  }
  savingFooter.value = false
}
</script>

<style scoped>
.content-manager { padding: 32px; background: #f9fafb; min-height: 100vh; }
.text-2xl { font-size: 1.5rem; line-height: 2rem; }
.font-bold { font-weight: 700; }
.text-gray-800 { color: #1f2937; }
.text-gray-600 { color: #4b5563; }
.mb-6 { margin-bottom: 1.5rem; }
.mb-4 { margin-bottom: 1rem; }
.mb-1 { margin-bottom: 0.25rem; }
.grid { display: grid; }
.gap-8 { gap: 2rem; }
.gap-4 { gap: 1rem; }
.grid-cols-1 { grid-template-columns: repeat(1, minmax(0, 1fr)); }
.md\:grid-cols-2 { grid-template-columns: repeat(2, minmax(0, 1fr)); }
.bg-white { background-color: #ffffff; }
.p-6 { padding: 1.5rem; }
.rounded-xl { border-radius: 0.75rem; }
.rounded { border-radius: 0.25rem; }
.shadow { box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06); }
.border-l-4 { border-left-width: 4px; }
.border-blue-500 { border-left-color: #3b82f6; }
.border-purple-500 { border-left-color: #a855f7; }
.text-xl { font-size: 1.25rem; line-height: 1.75rem; }
.text-primary { color: #1a0a00; }
.text-sm { font-size: 0.875rem; line-height: 1.25rem; }
.w-full { width: 100%; }
.px-4 { padding-left: 1rem; padding-right: 1rem; }
.py-2 { padding-top: 0.5rem; padding-bottom: 0.5rem; }
.p-4 { padding: 1rem; }
.border { border-width: 1px; border-color: #e5e7eb; }
.focus\:ring-2:focus { box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.5); }
.outline-none { outline: 2px solid transparent; outline-offset: 2px; }
.bg-blue-600 { background-color: #2563eb; }
.bg-blue-700:hover { background-color: #1d4ed8; }
.bg-purple-600 { background-color: #9333ea; }
.bg-purple-700:hover { background-color: #7e22ce; }
.text-white { color: #ffffff; }
.px-6 { padding-left: 1.5rem; padding-right: 1.5rem; }
.block { display: block; }
button:disabled { opacity: 0.6; cursor: not-allowed; }
</style>
