import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import Main from '../views/Main.vue';

const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login, 
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
  },
  {
    path: '/main',
    name: 'Main',
    component: Main,
  },
]

const router = createRouter({history: createWebHistory(process.env.BASE_URL), routes,});

export default router;