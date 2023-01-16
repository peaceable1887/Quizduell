<!--   
    Version: 3.2.41
    Auhtor: Felix Hansmann
    
    Die Komponente "Login.vue" ist für Darstellung der Seite "Login" zuständig .
-->
<template>
    <!-- Formular Komponente (Login) -->
    <LoginForm></LoginForm>
</template>

<script>

import LoginForm from "../components/LoginForm";

export default 
{
    name: "LoginItem",
    components:
    {
        LoginForm,
    },
}

</script>

<style scoped>

</style>