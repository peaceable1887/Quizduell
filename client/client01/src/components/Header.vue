<template>
    <div class="container">
        <span class="headline">Quizduell</span>
    </div>
</template>

<script>
    export default 
    {
        name: "Header",
    }
</script>

<style scoped>
.container
{
    display: flex;
    justify-content: center;
    background-color: #184e98;
    height: 50px;
    box-shadow: 0px 5px 5px -1px rgba(64,64,64,0.74);
    -webkit-box-shadow: 0px 5px 5px -1px rgba(64,64,64,0.74);
    -moz-box-shadow: 0px 5px 5px -1px rgba(64,64,64,0.74);
}
.headline{
    background-color: white;
    width: 25%;
    text-align: center;
    color: #184e98;
    font-size: 30px;
    font-weight: bold;
    padding-top: 9px;
}
</style>