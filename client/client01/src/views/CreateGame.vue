<template>
    <Header></Header>
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