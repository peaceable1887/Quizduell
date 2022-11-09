import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import Main from '../views/Main.vue';
import EditProfil from '../views/EditProfil.vue';
import Statistics from '../views/Statistics.vue';

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
  {
    path: '/editProfil',
    name: 'EditProfil',
    component: EditProfil,
  },
  {
    path: '/statistics',
    name: 'Statistics',
    component: Statistics,
  },
]

const router = createRouter({history: createWebHistory(process.env.BASE_URL), routes,});

export default router;