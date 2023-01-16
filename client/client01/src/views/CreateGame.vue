<!--   
    Version: 3.2.41
    Auhtor: Felix Hansmann
    
    Die Komponente "CreateGame.vue" ist für Darstellung der Seite "Spiel erstellen" zuständig .
-->
<template>
    <!-- Header Komponente -->
    <Header></Header>
    <!-- Formular Komponente (Spiel bearbeiten) -->
    <CreateGameForm></CreateGameForm>
</template>

<script>

import Header from "../components/Header";
import CreateGameForm from "../components/CreateGameForm";

export default 
{
    name: "CreateGame",
    components: 
    { 
        Header,
        CreateGameForm, 
    }
}
</script>

<style scoped>

</style>