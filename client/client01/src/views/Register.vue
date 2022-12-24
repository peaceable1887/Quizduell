<template>
    <Header></Header>
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
    data()
    {
       return{
        
       }
    }
}
</script>

<style scoped>

</style>