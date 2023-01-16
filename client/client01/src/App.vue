<!--   
    Version: 3.2.41
    Auhtor: Felix Hansmann
    
    Über die Komponente "main.vue" ist für Darstellung der gesamten Anwendung zuständig.
-->
<template> 
    <main>
      <router-view></router-view>
    </main>
</template>

<script>

export default 
{
  name: 'App',
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
  width: 100%;
  height: 100%;
}
main
{
  display: flex;
  justify-content: center;
  flex-direction: column;
}

</style>
