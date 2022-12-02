<template>
    <div class="container">
        <h5>{{lobbyName}}</h5>
        <div class="gameInfos">
            <span><b>Anzahl der Runden:</b> {{roundQuantity}}</span>
            <span><b>erstellt von:</b> {{playerName}}</span>
            <router-link :to="'/gameLobby/' + lobbyId">
                <Button text="Verbinden" @click="$emit('connect-lobby')"></Button>
            </router-link>
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
        lobbyId: String,
    },
}
</script>

<style scoped>
    .container
    {
        display: flex;
        flex-direction: column;
        width: 100%;
        border: 1px rgb(168, 168, 168) solid;
        padding: 10px 15px 10px 15px;
        margin-bottom: 30px;
        border-radius: 5px;
        box-shadow: 0px 5px 5px -1px rgba(64,64,64,0.74);
        -webkit-box-shadow: 0px 5px 5px -1px rgba(64,64,64,0.74);
        -moz-box-shadow: 0px 5px 5px -1px rgba(64,64,64,0.74);
    }
    h5
    {
        font-size: 30px;
        margin-bottom: 15px;
    }
    .gameInfos
    {
        display: flex;
        justify-content: space-between;
        margin-bottom: 5px;
    }
    span
    {
        font-size: 19px;
        padding: 5px 0 5px 0;
    }
    span b
    {
        color: #184e98;
    }
    Button
    {
        font-size: 20px;
        padding: 5px 40px 5px 40px;
        font-weight: bold;
    }
</style>