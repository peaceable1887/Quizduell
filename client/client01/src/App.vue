<template> 
    <main>
      <router-view></router-view>
    </main>
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
      
    }
  },
  
  methods:
  {
   
  
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
  }
  main
  {
    display: flex;
    justify-content: center;
    flex-direction: column;
  }

</style>
