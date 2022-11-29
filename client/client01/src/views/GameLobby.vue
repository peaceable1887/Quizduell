<template>
    <Header></Header>
    <div class="containerGame">
        <Headline class="headline" text="Spiel 1"></Headline>
        <div class="gameInfos">
            <span>Anzahl der Runden: 4</span>
            <span>erstellt von: Felix</span>
        </div>
        <div class="playerContainer">
            <PlayerLobby playerName="Felix"></PlayerLobby>
        </div>
        <div class="btnWrapper">
            <router-link to="/lobby">
                <Button text="Verlassen"></Button>
            </router-link>
            <Button type="submit" text="Spiel starten"></Button>                 
        </div>
    </div>  
</template>

<script>
import Header from "../components/Header.vue";
import Headline from "../components/Headline.vue";
import PlayerLobby from "../components/PlayerLobby";
import Button from "../components/Button.vue"

export default 
{
    name: "GameLobby",
    components:
    {
        Header,
        Headline,
        PlayerLobby,
        Button,
    }
}
</script>

<style scoped>
.containerGame
{
    margin: 0 15% 0 15%;
}
.headline
{
    font-size: 45px;
    padding: 50px 0 50px 0;
}
.gameInfos
{
    display: flex;
    width: 100%;
    color: black;
    font-weight: bold;
    justify-content: space-between;
    font-size: 20px;
    padding: 1% 1% 1% 1%;
    margin-bottom: 40px;
    border-bottom: 1px black solid;
}
.btnWrapper
{
    display: flex;
    justify-content: space-evenly;
    width: 100%;
    bottom: 0;
}
Button
{
    width: 200px;
    padding: 12px 0 12px 0;
    font-size: 22px;
}
</style>