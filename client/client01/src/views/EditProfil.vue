<!--   
    Version: 3.2.41
    Auhtor: Felix Hansmann
    
    Die Komponente "EditProfil.vue" ist für Darstellung der Seite "Profil bearbeiten" zuständig .
-->
<template>
    <!-- Header Komponente -->
    <Header></Header>
    <!-- Formular Komponente (Profil bearbeiten) -->
    <EditProfilForm></EditProfilForm>
</template>

<script>

import Header from "../components/Header.vue"
import EditProfilForm from '../components/EditProfilForm.vue';

export default 
{
    name: "EditProfil", 
    components:
    {
        EditProfilForm,
        Header,
    }
}
</script>

<style scoped>

</style>