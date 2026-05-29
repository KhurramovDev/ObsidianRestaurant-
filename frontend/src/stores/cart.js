import { defineStore } from 'pinia'

export const useCartStore = defineStore('cart', {
  state: () => ({
    items: JSON.parse(localStorage.getItem('cart') || '[]'),
  }),
  getters: {
    totalItems: (state) => state.items.reduce((sum, i) => sum + i.quantity, 0),
    totalAmount: (state) => state.items.reduce((sum, i) => sum + Number(i.price) * i.quantity, 0),
  },
  actions: {
    saveToStorage() {
      localStorage.setItem('cart', JSON.stringify(this.items))
    },
    addItem(item) {
      const existing = this.items.find(i => i.id === item.id)
      if (existing) {
        existing.quantity++
      } else {
        this.items.push({ ...item, quantity: 1 })
      }
      this.saveToStorage()
    },
    removeItem(id) {
      this.items = this.items.filter(i => i.id !== id)
      this.saveToStorage()
    },
    increaseQty(id) {
      const item = this.items.find(i => i.id === id)
      if (item) { item.quantity++; this.saveToStorage() }
    },
    decreaseQty(id) {
      const item = this.items.find(i => i.id === id)
      if (item) {
        if (item.quantity > 1) { item.quantity--; this.saveToStorage() }
        else this.removeItem(id)
      }
    },
    clearCart() {
      this.items = []
      this.saveToStorage()
    }
  }
})
