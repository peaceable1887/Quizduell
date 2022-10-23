<template> 
    <LoginPage></LoginPage>
</template>

<script>
import LoginPage from "./components/LoginPage.vue";

export default {
  name: 'App',
  components: 
  {
    LoginPage,
  },  
}
</script>

<style>
#app 
{
  display: flex;
  justify-content: center;
  font-family: 'Roboto', sans-serif;
  font-style: bold;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
