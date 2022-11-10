<template>
    <Header></Header>
    <GameRound></GameRound>
    <Versus></Versus>
    <Question></Question>
</template>

<script>
import Header from "../components/Header.vue";
import GameRound from "../components/GameRound.vue";
import Versus from "../components/Versus.vue";
import Question from "../components/Question.vue";

    export default 
    {
        name: "Game",   
        components:
        {
            Header,
            GameRound,
            Versus,
            Question,
        } 
    }
</script>

<style scoped>

</style>