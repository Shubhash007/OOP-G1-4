import { createRouter, createWebHistory } from 'vue-router';
import { jwtDecode } from "jwt-decode";
import Layout from '@/components/Layout.vue';  
import Home from '../pages/Home.vue';
import About from '../pages/About.vue';
import Landing from '../pages/Landing.vue';
import PurchaseHistory from '@/pages/PurchaseHistory.vue';
import Admin from '@/pages/Admin.vue';
import Analytics from '@/pages/Analytics.vue';
import Marketing from '@/pages/Marketing.vue';
import Profile from '@/pages/Profile.vue';

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
      { path: 'home', name: 'Home', component: Home },
      { path: 'about', name: 'About', component: About },
      { path: 'purchaseHistory', name: 'PurchaseHistory', component: PurchaseHistory },
      { path: 'admin', name: 'Admin', component: Admin },
      { path: 'analytics', name: 'Analytics', component: Analytics },
      { path: 'marketing', name: 'Marketing', component: Marketing },
      { path: 'profile', name: 'Profile', component: Profile },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

router.beforeEach(async (to, from, next) => {
  const isPublicRoute = to.name === 'Landing';
  const jwtToken = localStorage.getItem('jwt_token');
  
  if (jwtToken) {
    const decodedToken = jwtDecode(jwtToken);
    const currentTime = Math.floor(Date.now() / 1000); 

    if (decodedToken.exp < currentTime) {
      localStorage.removeItem('jwt_token');
      next({ name: 'Landing' });
      return;
    }
  }

  if (!isPublicRoute && !jwtToken) {
    next({ name: 'Landing' });
    return;
  }

  if (jwtToken) {
    try {
      const response = await fetch("http://localhost:8080/auth/users/get-user", {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${jwtToken}`,
          "Content-Type": "application/json",
        },
      });

      if (response.ok) {
        const user = await response.json();
        const userRole = user.role;

        if (to.name === 'Admin' && userRole !== 'ROLE_ADMIN') {
          next({ name: 'Home' });
        } else {
          next(); 
        }
      } else {
        next({ name: 'Landing' }); 
      }
    } catch (error) {
      console.error("Error fetching user info:", error);
      next({ name: 'Landing' }); 
    }
  } else {
    next();
  }
});

export default router;
