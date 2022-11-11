<template>
    <div>
        QuestionEvaluation
        <Button text="nächste Runde" @click="nextRound"></Button>
    </div>
</template>

<script>
import Button from "../components/Button.vue";
export default 
{
    name: "QuestionEvaluation",
    components:
    {
        Button
    },
    methods:
    {
        nextRound()
        {
            this.$router.push("/game")
        }
    }
}
</script>

<style scoped>

</style>