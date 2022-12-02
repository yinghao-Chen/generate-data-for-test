import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

const proxyUrl = "http://192.168.128.238:8080";

export default defineConfig({
  build: {
	assetsDir: "static"
  },
  css: {
    preprocessorOptions: {
      less: {
        javascriptEnabled: true,
      },
    },
  },
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      vue: "vue/dist/vue.esm-bundler.js"
    }
  },
  server: {
    port: 3030,
    host: "0.0.0.0",
    proxy: {
      "/api": {
        target: proxyUrl,
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
    }
  }
})
