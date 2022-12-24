<template>
    <Header></Header>
    <div class="container">
        <Headline class="headline" text="Meine Statistik"></Headline>
        <div class="statContent">
            <div class="statData">
                <span class="text">gewonnene Spiele</span>
                <span class="data">20</span>
            </div>
            <div class="statData">
                <span class="text">verlorene Spiele</span>
                <span class="data">0</span>
            </div>
            <div class="statData">
                <span class="text">Win Rate</span>
                <span class="data">100 %</span>
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