import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/Home.vue')
    },{
      path: '/dataSource',
      name: 'dataSource',
      component: () => import('../views/dataSource/index.vue')
    }
  ]
})

export default router
