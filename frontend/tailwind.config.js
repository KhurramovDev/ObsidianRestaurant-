/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#0A2F1D', // Emerald Green from premium Weber context
        accent: '#D4AF37',  // Gold accent for premium feel
      }
    },
  },
  plugins: [],
}
