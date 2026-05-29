<template>
  <div class="auth-page">
    <div class="auth-split">
      <!-- LEFT IMAGE -->
      <div class="auth-image">
        <img src="https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=800&q=80" alt="Restaurant" />
        <div class="auth-image-overlay">
          <h2>Obsidian ga xush kelibsiz!</h2>
          <p>Eng mazali taomlar sizni kutmoqda</p>
        </div>
      </div>

      <!-- RIGHT FORM -->
      <div class="auth-form-side">
        <div class="auth-box">
          <div class="auth-logo">🍽</div>
          <h1>Tizimga kirish</h1>
          <p class="auth-sub">Email va parolingizni kiriting</p>

          <form @submit.prevent="handleLogin" class="auth-form">
            <div class="form-group">
              <label>Email manzil</label>
              <input v-model="form.email" type="email" placeholder="siz@email.com" required />
            </div>
            <div class="form-group">
              <label>Parol</label>
              <div class="input-pass">
                <input v-model="form.password" :type="showPass ? 'text' : 'password'" placeholder="Parolni kiriting" required />
                <button type="button" class="pass-toggle" @click="showPass = !showPass">{{ showPass ? '🙈' : '👁' }}</button>
              </div>
            </div>

            <div v-if="errorMsg" class="alert-error">⚠ {{ errorMsg }}</div>

            <button type="submit" class="btn-primary w-full" :disabled="loading">
              <span v-if="loading">⏳ Kirish...</span>
              <span v-else>Kirish →</span>
            </button>
          </form>

          <p class="auth-switch">
            Hisobingiz yo'qmi?
            <router-link to="/register">Ro'yxatdan o'ting</router-link>
          </p>

          <div class="admin-hint">
            <span>Admin: admin@restaran.com / admin123</span>
          </div>
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
const form = ref({ email: '', password: '' })
const loading = ref(false)
const errorMsg = ref('')
const showPass = ref(false)

const handleLogin = async () => {
  loading.value = true
  errorMsg.value = ''
  const ok = await authStore.login(form.value.email, form.value.password)
  loading.value = false
  if (ok) {
    router.push(authStore.isAdmin ? '/admin' : '/')
  } else {
    errorMsg.value = 'Email yoki parol noto\'g\'ri!'
  }
}
</script>

<style scoped>
.auth-page { min-height: 100vh; }
.auth-split { display: grid; grid-template-columns: 1fr 1fr; min-height: 100vh; }

.auth-image { position: relative; overflow: hidden; }
.auth-image img { width: 100%; height: 100%; object-fit: cover; }
.auth-image-overlay {
  position: absolute; inset: 0;
  background: linear-gradient(to top, rgba(26,10,0,0.9) 0%, rgba(26,10,0,0.3) 100%);
  display: flex; flex-direction: column;
  justify-content: flex-end; padding: 48px;
}
.auth-image-overlay h2 { color: white; font-size: 28px; margin-bottom: 8px; }
.auth-image-overlay p { color: rgba(255,255,255,0.7); font-size: 16px; }

.auth-form-side {
  display: flex; align-items: center; justify-content: center;
  background: var(--bg-cream); padding: 40px 24px;
}
.auth-box { width: 100%; max-width: 400px; }
.auth-logo { font-size: 48px; margin-bottom: 16px; }
.auth-box h1 { font-size: 30px; color: var(--primary); margin-bottom: 8px; }
.auth-sub { color: var(--text-gray); margin-bottom: 32px; }

.auth-form { display: flex; flex-direction: column; gap: 4px; }
.input-pass { position: relative; }
.input-pass input { padding-right: 44px; }
.pass-toggle {
  position: absolute; right: 12px; top: 50%; transform: translateY(-50%);
  background: none; border: none; font-size: 18px; cursor: pointer;
}

.alert-error {
  background: #fef2f2; border: 1px solid #fca5a5;
  color: #dc2626; padding: 10px 14px;
  border-radius: 8px; font-size: 14px; margin: 4px 0;
}
.w-full { width: 100%; justify-content: center; }

.auth-switch { text-align: center; margin-top: 24px; font-size: 14px; color: var(--text-gray); }
.auth-switch a { color: var(--accent); font-weight: 600; }

.admin-hint {
  margin-top: 20px; text-align: center;
  padding: 10px; background: rgba(200,146,42,0.08);
  border-radius: 8px; border: 1px dashed rgba(200,146,42,0.3);
}
.admin-hint span { font-size: 12px; color: var(--accent); font-family: monospace; }

button:disabled { opacity: 0.6; cursor: not-allowed; }

@media (max-width: 768px) {
  .auth-split { grid-template-columns: 1fr; }
  .auth-image { height: 240px; }
}
</style>
