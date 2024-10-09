import { createRouter, createWebHistory } from 'vue-router';
import Layout from '@/components/Layout.vue';  
import Home from '../pages/Home.vue';
import About from '../pages/About.vue';
import Landing from '../pages/Landing.vue';
import PurchaseHistory from '@/pages/PurchaseHistory.vue';
import Admin from '@/pages/Admin.vue';
import Analytics from '@/pages/Analytics.vue';

const routes = [
  {
    path: '/',
    name: 'Landing',
    component: Landing,  
  },
  {
    path: '/',
    component: Layout,  
    children: [
      {
        path: 'home',
        name: 'Home',
        component: Home,
      },
      {
        path: 'about',
        name: 'About',
        component: About,
      },
      {
        path: 'purchaseHistory',
        name: 'Purchase History',
        component: PurchaseHistory,
      },
      {
        path: 'admin',
        name: 'Admin',
        component: Admin,
      },
      {
        path: 'analytics',
        name: 'Analytics',
        component: Analytics,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
