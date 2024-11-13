import { createRouter, createWebHistory } from 'vue-router';
import Layout from '@/components/Layout.vue';  
import Home from '../pages/Home.vue';
import About from '../pages/About.vue';
import Landing from '../pages/Landing.vue';
import PurchaseHistory from '@/pages/PurchaseHistory.vue';
import Admin from '@/pages/Admin.vue';
import Analytics from '@/pages/Analytics.vue';
import Marketing from '@/pages/Marketing.vue';

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
        name: 'PurchaseHistory',
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
      {
        path: 'marketing',
        name: 'Marketing',
        component: Marketing,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  const isPublicRoute = to.name === 'Landing';
  const jwtToken = localStorage.getItem('jwt_token');

  if (!isPublicRoute && !jwtToken) {
    next({ name: 'Landing' });
  } else {
    next(); 
  }
});

export default router;
