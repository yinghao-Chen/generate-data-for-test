import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import App from './App.vue'
import router from './router'

import 'element-plus/dist/index.css'

import * as api from "@/api"

const app = createApp(App)

app.use(router)
app.use(ElementPlus)

app.config.globalProperties.$api = api;

app.mount('#app')
