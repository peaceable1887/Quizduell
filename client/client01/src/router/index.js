import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import Main from '../views/Main.vue';
import EditProfil from '../views/EditProfil.vue';
import Game from '../views/Game.vue';
import GameEvaluation from '../views/GameEvaluation.vue';
import MultiplayerLobby from '../views/MultiplayerLobby.vue';
import CreateGame from '../views/CreateGame.vue';
import GameLobby from '../views/GameLobby.vue';
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
    path: '/game',
    name: 'Game',
    component: Game,
  },
  {
    path: '/gameEvaluation',
    name: 'GameEvaluation',
    component: GameEvaluation,
  },
  {
    path: '/lobby',
    name: 'MultiplayerLobby',
    component: MultiplayerLobby,
  },
  {
    path: '/createGame',
    name: 'CreateGame',
    component: CreateGame,
  },
  {
    path: '/gameLobby/:id',
    name: 'GameLobby',
    component: GameLobby,
  },
  {
    path: '/statistics',
    name: 'Statistics',
    component: Statistics,
  },
]

const router = createRouter({history: createWebHistory(process.env.BASE_URL), routes,});

export default router;