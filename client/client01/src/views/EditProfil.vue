<template>
    <Header></Header>
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