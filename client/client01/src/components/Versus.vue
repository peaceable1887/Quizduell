<template>
    <div class="container">
        <div class="versus">
            <div class="player"></div>
            <span>VS</span>
            <div class="player"></div>
        </div>
    </div>
</template>

<script>
    export default 
    {
        name: "Versus",
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
        background-color: blueviolet;
    }
    @media screen and (max-width:650px) 
    {
        span{font-size: 24px;}
       .player{width: 50px; height: 50px;}
    }
</style>