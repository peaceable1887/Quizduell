<template>
    <div class="container">
        <div class="round">
            Runde {{ text }}
        </div>
    </div>
</template>

<script>
export default 
{
    name: "GameRound",
    props:
    {
        text: String,
    }
}
</script>

<style scoped>
.container
{
    display: flex;
    justify-content: center;    
}
.round
{
    font-weight: bold;
    color: #184e98;
    font-size: 30px;
    text-align: center;
    padding-top: 20px;
}
@media screen and (max-width:650px) 
{
    .round{font-size: 24px}
}
</style>