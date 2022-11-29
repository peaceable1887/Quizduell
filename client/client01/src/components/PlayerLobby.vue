<template>
    <div class="container">
        <span class="playerName">Spieler: {{playerName}}</span>
        <div class="playerCheck">
            <label for="playerCheck">Nicht bereit {{isReady}}</label> 
            <div class="checkboxContainer">
                <input type="checkbox" name="playerCheck" v-model="playerCheck">
            </div>
        </div>
    </div>
</template>

<script>


export default 
{
    name: "PlayerLobby",
    props:
    {
        playerName: String,
        isReady: false,
    }
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
    border: 1px solid #184e98;
}
.playerName
{
    color: #184e98;
    font-size: 20px;
    font-weight: bold;
}
.playerCheck
{
    display: flex;
    width: 150px;
    justify-content: space-between;
    align-items: center;
}
</style>