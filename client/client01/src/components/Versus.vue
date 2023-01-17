<!--   
    Version: 3.2.41
    Auhtor: Felix Hansmann
    
    Die Komponente "Versus.vue" wird in die Game Komponente geladen und ist für de Darstellung der Spieler, die gegeneinander spielen, dar.
-->
<template>
    <div class="container">
        <div class="versus">
            <div class="player"><img :src="`${profilIconTwo}`"></div>
            <span>VS</span>
            <div class="player"><img :src="`${profilIconOne}`"></div>
        </div>
    </div>
</template>

<script>

export default 
{
    name: "VersusItem",
    props:
    {
        profilIconOne: String,
        profilIconTwo: String
    },
}
    
</script>

<style scoped>
.container
{
    display: flex;
    justify-content: center;
    padding: 10px;
}
.versus
{
    display: flex;
    justify-content: space-between;
    width: 308px;
    align-items: center;
}
span
{
    font-weight: bold;
    color: #184e98;
    font-size: 30px;
}
.player
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
@media screen and (max-width:650px) 
{
    span{font-size: 24px;}
    .player{width: 50px; height: 50px;}
}
</style>