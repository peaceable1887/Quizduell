import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import MainMenu from '../views/MainMenu.vue';

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
    path: '/MainMenu',
    name: 'MainMenu',
    component: MainMenu,
  },
]

const router = createRouter({history: createWebHistory(process.env.BASE_URL), routes,});

export default router;