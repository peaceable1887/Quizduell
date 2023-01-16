<!--   
    Version: 3.2.41
    Auhtor: Felix Hansmann
    
    Die Komponente "Statisics.vue" zeigt die Statistik des Spielers an.
-->
<template>
    <!-- Header Komponente -->
    <Header></Header>
    <div class="container">
        <!-- Headline Komponente (Meine Statistik)-->
        <Headline class="headline" text="Meine Statistik"></Headline>
        <div class="statContent">
            <div class="statData">
                <span class="text">Spiele gesamt</span>
                <span class="data">{{gameCount}}</span>
            </div>
            <div class="statData">
                <span class="text">gewonnene Spiele</span>
                <span class="data">{{gameWonCount}}</span>
            </div>
            <div class="statData">
                <span class="text">verlorene Spiele</span>
                <span class="data">{{gameLossCount}}</span>
            </div>
            <div class="statData">
                <span class="text">Win Rate</span>
                <span class="data">{{winRate}} %</span>
            </div>
            <form action="/main">
                <Button text="Zurück"></Button>
            </form>
        </div>
    </div>
</template>

<script>

import Header from "../components/Header.vue";
import Headline from "../components/Headline.vue";
import Button from "../components/Button.vue";

export default 
{
    name: "StatisticsItem",
    components:
    {
        Header,
        Headline,
        Button,
    },
    data()
    {
        return{
            gameCount: "",
            gameWonCount: "",
            gameLossCount: "",
            gameDrawCount: "",
            winRate: "",
        }
    },
    /**
     * Der Lifecycle Hook "created" stellt alle benötigten REST Api und/oder Websocket Verbinungen her.
     */
    async created()
    {
        await fetch("http://localhost:8080/api/stats/v1/get", {
            method: "GET",
            headers: 
            {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem("token")
            }
        })
        .then(res => res.json())
        .then(data => 
        {
            this.gameCount = data.gameCount;
            this.gameWonCount = data.gameWonCount;
            this.gameLossCount = data.gameLossCount;
            this.gameDrawCount = data.gameDrawCount;
        })

        //WinRate wird berechnet
        let number = (this.gameWonCount/this.gameCount)*100;
        this.winRate = Math.round(number * 100) / 100
    }
}
</script>

<style scoped>
.container
{
    margin: 0;   
}
.headline
{
    font-size: 40px;
    padding: 100px 0 60px 0;
}
.statContent
{
    display: flex;
    flex-direction: column;
    align-items: center;
}
.statData
{
    display: flex;
    justify-content: space-between;
    width: 600px;
    font-size: 40px;
    color: #184e98;
    font-weight: bold;
    margin: 30px 0 30px 0
}
Button
{
    width: 200px;
    padding: 12px 0 12px 0;
    margin: 80px 0 0 0 ;
    font-size: 22px;
}
@media screen and (max-width:650px) 
{
    .headline{font-size: 33px;}
    .statData{width: 400px;font-size: 33px; margin: 30px 0 30px 0}
}
</style>