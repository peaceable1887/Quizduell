<template>
    <div class="container">
        <h5>{{lobbyName}}</h5>
        <div class="gameInfos">
            <span><b>Anzahl der Runden:</b> {{roundQuantity}}</span>
            <span><b>erstellt von:</b> {{playerName}}</span>
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
    },
    props:
    {
        lobbyName: String,
        roundQuantity: String,
        playerName: String,
    }
}
</script>

<style scoped>
    .container
    {
        display: flex;
        flex-direction: column;
        width: 100%;
        border: 1px black solid;
        padding: 10px 10px 5px 10px;
        margin-bottom: 30px;
    }
    h5
    {
        font-size: 30px;
        margin-bottom: 10px;
    }
    .gameInfos
    {
        display: flex;
        justify-content: space-between;
        margin-bottom: 5px;
    }
    span
    {
        font-size: 18px;
        padding: 5px 0 5px 0;
    }
    Button
    {
        font-size: 20px;
        padding: 5px 40px 5px 40px;
        font-weight: bold;
    }
</style>