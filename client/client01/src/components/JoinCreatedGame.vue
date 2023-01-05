<template>
    <div class="container">
        <h5>{{lobbyName}}</h5>
        <div class="border"></div> 
        <div class="gameInfos">
            <span><b>Anzahl der Runden:</b> 6</span>
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
        margin-bottom: 30px;
        border-radius: 5px;
        box-shadow: 0px 5px 5px -1px rgba(64,64,64,0.74);
        -webkit-box-shadow: 0px 5px 5px -1px rgba(64,64,64,0.74);
        -moz-box-shadow: 0px 5px 5px -1px rgba(64,64,64,0.74);
    }
    h5
    {
        color: #184e98;
        font-size: 45px;
        text-align: center;
        margin: 15px 10px 0 10px;
    }
    .border
    {
        border-bottom: 1px solid black;
        margin-top: 15px;
        margin-bottom: 20px;
    }
    .gameInfos
    {
        display: flex;
        justify-content: space-between;
        margin: 0 15px 20px 15px;
    }
    span
    {
        font-size: 20px;
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
        background-color: green;
    }
</style>