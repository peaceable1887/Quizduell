<template>
    <div class="container">
        <h5>Spiel 1</h5>
        <div class="gameInfos">
            <span>Anzahl der Runden:</span>
            <span>erstellt von:</span>
            <form>
                <Button text="beitreten"></Button>
            </form>
        </div>
    </div>
</template>

<script>
import Button from "./Button.vue"

export default 
{
    name: "JoinCreatedGame",    
    components:
    {
        Button,
    }
}
</script>

<style scoped>
    .container
    {
        display: flex;
        flex-direction: column;
        width: 1000px;
        border: 1px black solid;
    }
    .gameInfos
    {
        display: flex;
        justify-content: space-around;
    }
</style>