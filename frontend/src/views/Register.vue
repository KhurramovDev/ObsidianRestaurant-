<template>
  <div class="auth-page">
    <div class="auth-split">
      <div class="auth-image">
        <img src="https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?w=800&q=80" alt="Restaurant" />
        <div class="auth-image-overlay">
          <h2>Oilaga qo'shiling! 🍽️</h2>
          <p>Minglab mijozlarimiz bilan birga bo'ling va buyurtmalaringizni bot orqali kuzatib boring.</p>
        </div>
      </div>
      <div class="auth-form-side">
        <div class="auth-box">
          <div class="auth-logo">✨</div>
          <h1>Ro'yxatdan o'tish</h1>
          <p class="auth-sub">Ma'lumotlaringizni to'ldiring</p>

          <form @submit.prevent="handleRegister" class="auth-form">
            <div class="form-grid">
              <div class="form-group">
                <label>Login (username)</label>
                <input v-model="form.name" type="text" placeholder="login_nom" required />
              </div>
              <div class="form-group">
                <label>To'liq ism</label>
                <input v-model="form.fullname" type="text" placeholder="Ism Familiya" required />
              </div>
            </div>

            <div class="form-group">
              <label>Email manzil</label>
              <input v-model="form.email" type="email" placeholder="siz@email.com" required />
            </div>

            <div class="form-grid">
              <div class="form-group">
                <label>Telefon raqam</label>
                <input v-model="form.phoneNumber" type="tel" placeholder="+998 90 123 45 67" required />
              </div>
              <div class="form-group">
                <label>Telegram Chat ID (ixtiyoriy)</label>
                <input v-model="form.telegramChatId" type="text" placeholder="ID raqamingiz" />
                <small class="hint">Botdan status olish uchun (@userinfobot)</small>
              </div>
            </div>

            <div class="form-group">
              <label>Parol</label>
              <div class="input-pass">
                <input v-model="form.password" :type="showPass ? 'text' : 'password'" placeholder="Kamida 6 ta belgi" required minlength="6" />
                <button type="button" class="pass-toggle" @click="showPass = !showPass">{{ showPass ? '🙈' : '👁' }}</button>
              </div>
            </div>

            <div v-if="errorMsg" class="alert-error">⚠ {{ errorMsg }}</div>
            <div v-if="successMsg" class="alert-success">✅ {{ successMsg }}</div>

            <button type="submit" class="btn-primary w-full" :disabled="loading">
              <span v-if="loading">⏳ Ro'yxatdan o'tilmoqda...</span>
              <span v-else>Ro'yxatdan o'tish →</span>
            </button>
          </form>

          <p class="auth-switch">
            Hisobingiz bormi?
            <router-link to="/login">Kirish</router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const authStore = useAuthStore()
const router = useRouter()
const form = ref({
  name: '',
  fullname: '',
  email: '',
  password: '',
  phoneNumber: '',
  telegramChatId: ''
})
const loading = ref(false)
const errorMsg = ref('')
const successMsg = ref('')
const showPass = ref(false)

const handleRegister = async () => {
  loading.value = true
  errorMsg.value = ''
  successMsg.value = ''
  const result = await authStore.register(form.value)
  loading.value = false
  if (result.ok) {
    successMsg.value = 'Muvaffaqiyatli ro\'yxatdan o\'tdingiz! Kirish sahifasiga o\'tmoqdasiz...'
    setTimeout(() => router.push('/login'), 1800)
  } else {
    errorMsg.value = result.msg
  }
}
</script>

<style scoped>
.auth-page { min-height: 100vh; font-family: 'Inter', sans-serif; }
.auth-split { display: grid; grid-template-columns: 1fr 1fr; min-height: 100vh; }
.auth-image { position: relative; overflow: hidden; }
.auth-image img { width: 100%; height: 100%; object-fit: cover; }
.auth-image-overlay {
  position: absolute; inset: 0;
  background: linear-gradient(to top, rgba(26,10,0,0.95) 0%, rgba(26,10,0,0.2) 100%);
  display: flex; flex-direction: column;
  justify-content: flex-end; padding: 60px;
}
.auth-image-overlay h2 { color: white; font-size: 42px; font-weight: 800; margin-bottom: 12px; font-family: 'Playfair Display', serif; }
.auth-image-overlay p { color: rgba(255,255,255,0.8); font-size: 18px; line-height: 1.6; max-width: 400px; }

.auth-form-side { display: flex; align-items: center; justify-content: center; background: #fff; padding: 40px; }
.auth-box { width: 100%; max-width: 500px; animation: slideUp 0.5s ease-out; }
.auth-logo { font-size: 48px; margin-bottom: 20px; }
.auth-box h1 { font-size: 36px; font-weight: 800; color: #1a0a00; margin-bottom: 10px; font-family: 'Playfair Display', serif; }
.auth-sub { color: #64748b; margin-bottom: 32px; font-size: 16px; }

.form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.form-group { margin-bottom: 20px; }
.form-group label { display: block; font-size: 14px; font-weight: 700; color: #1e293b; margin-bottom: 8px; }
.hint { font-size: 11px; color: #94a3b8; display: block; margin-top: 4px; }

.input-pass { position: relative; }
.pass-toggle { position: absolute; right: 12px; top: 50%; transform: translateY(-50%); background: none; border: none; font-size: 18px; cursor: pointer; }

.alert-error { background: #fff1f2; border: 1px solid #fecdd3; color: #e11d48; padding: 12px; border-radius: 12px; font-size: 14px; margin-bottom: 20px; font-weight: 600; }
.alert-success { background: #f0fdf4; border: 1px solid #bbf7d0; color: #16a34a; padding: 12px; border-radius: 12px; font-size: 14px; margin-bottom: 20px; font-weight: 600; }

.btn-primary {
  background: #c8922a; color: white; border: none; padding: 16px; border-radius: 14px;
  font-weight: 800; font-size: 16px; cursor: pointer; width: 100%; transition: 0.3s;
}
.btn-primary:hover { transform: translateY(-3px); box-shadow: 0 10px 25px rgba(200,146,42,0.3); }

.auth-switch { text-align: center; margin-top: 24px; font-size: 15px; color: #64748b; }
.auth-switch a { color: #c8922a; font-weight: 700; text-decoration: none; }

@keyframes slideUp { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }

@media (max-width: 900px) { .auth-split { grid-template-columns: 1fr; } .auth-image { display: none; } }
@media (max-width: 500px) { .form-grid { grid-template-columns: 1fr; } }
</style>
