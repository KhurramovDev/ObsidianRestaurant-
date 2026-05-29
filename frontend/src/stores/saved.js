import { defineStore } from 'pinia'

export const useSavedStore = defineStore('saved', {
  state: () => ({
    items: JSON.parse(localStorage.getItem('saved') || '[]'),
  }),
  getters: {
    totalItems: (state) => state.items.length,
    isSaved: (state) => (id) => state.items.some(i => i.id === id)
  },
  actions: {
    saveToStorage() {
      localStorage.setItem('saved', JSON.stringify(this.items))
    },
    toggleSaved(item) {
      const index = this.items.findIndex(i => i.id === item.id)
      if (index >= 0) {
        this.items.splice(index, 1)
      } else {
        this.items.push(item)
      }
      this.saveToStorage()
    },
    removeSaved(id) {
      this.items = this.items.filter(i => i.id !== id)
      this.saveToStorage()
    },
    clearSaved() {
      this.items = []
      this.saveToStorage()
    }
  }
})
