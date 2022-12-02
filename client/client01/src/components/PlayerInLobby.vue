<template>
    <div class="container">
        <span class="playerName"><b>Spieler:</b> {{playerName}}</span>
        <div class="playerCheck">
            <div class="checkboxContainer">
                <button @click="$emit('toggleBtn')">{{btnText}}</button>
            </div>
        </div>
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
    },
}
</script>

<style scoped>
.container
{
    display: flex;
    width: 100%;
    justify-content: space-between;
    padding: 2% 1% 2% 1%;
    margin-bottom: 30px;
    border-radius: 5px;
    border: 1px rgb(168, 168, 168) solid;
    box-shadow: 0px 5px 5px -1px rgba(64,64,64,0.74);
    -webkit-box-shadow: 0px 5px 5px -1px rgba(64,64,64,0.74);
    -moz-box-shadow: 0px 5px 5px -1px rgba(64,64,64,0.74);
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
.playerCheck
{
    display: flex;
    width: 150px;
    justify-content: space-between;
    align-items: center;
}
.checkboxContainer
{

}
</style>