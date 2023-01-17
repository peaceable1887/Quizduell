<!--   
    Version: 3.2.41
    Auhtor: Felix Hansmann
    
    Die Komponente "PlayerInLobby.vue" dient als Schema, wie Spieler, die einer Lobby beigetreten sind, auszusehen haben.
-->
<template>
    <div class="container">
        <div class="playerNameContainer">
            <span class="playerName"><b>Spieler:</b> {{playerName}}</span>
        </div>
        <div class="profilIcon"><img :src="`${profilIcon}`"></div>
    </div>
</template>

<script>

export default 
{
    name: "PlayerInLobby",
    props:
    {
        playerName: String,
        status: String,
        btnText: String,
        profilIcon: String,
    },
}
</script>

<style scoped>
.container
{
    display: flex;
    width: 100%;
    justify-content: space-between;
    padding: 1% 1% 1% 1%;
    margin-bottom: 30px;
    border-radius: 5px;
    border: 1px rgb(168, 168, 168) solid;
    box-shadow: 0px 5px 5px -1px rgba(64,64,64,0.74);
    -webkit-box-shadow: 0px 5px 5px -1px rgba(64,64,64,0.74);
    -moz-box-shadow: 0px 5px 5px -1px rgba(64,64,64,0.74);
}
.playerNameContainer
{
    display: flex;
    align-items: center;
}
.playerName
{
    font-size: 20px;
}
.playerName b
{
    color: #184e98;
    font-size: 20px;
}
.profilIcon
{
    width: 60px;
    height: 60px;
    background-color: white;
    border: 1px rgb(168, 168, 168) solid;
}
img
{
    width: 60px;
    height: 60px;
    border: 1px rgb(168, 168, 168) solid;
}
</style>