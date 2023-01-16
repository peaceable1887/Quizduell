<!--   
    Version: 3.2.41
    Auhtor: Felix Hansmann
    
    Die Komponente "Register.vue" ist für Darstellung der Seite "Registrieren" zuständig, 
-->
<template>
    <!-- Header Komponente -->
    <Header></Header>
    <!-- Formular Komponente (Register) -->
    <RegisterForm></RegisterForm>
</template>

<script>

import RegisterForm from "../components/RegisterForm.vue";
import Header from "../components/Header.vue";

export default 
{
    name: "RegisterItem",
    components:
    {
        RegisterForm,
        Header
    },
}
</script>

<style scoped>

</style>