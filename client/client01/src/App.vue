<template>
  <router-view v-bind:showLogin="showLogin"></router-view>
</template>

<script>

export default 
{
  name: 'App',
  components: 
  {
  
  },
  data()
  {
    return{
      showLogin: true,
    }
  },
  methods:
  {
    toggleRegisterForm() 
    {
      this.showLogin = !this.showLogin;
    }
  }
}
</script>

<style>
  *
  {
    margin: 0;
    padding: 0;
  }
  body
  {
    font-family: 'Roboto', sans-serif;
    display: flex;
    justify-content: center;
  }

</style>
