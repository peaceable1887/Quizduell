<template>
    <div class="headline">
        <h2>{{ text }}</h2>
    </div>
</template>

<script>
    export default 
    {
        name: "Headline",
        props:
        {
            text: String,
        }   
    }
</script>

<style scoped>
    .headline
    {
        display: flex;
        justify-content: center;
        padding: 20%;
    }
    h2
    {
        margin: 0;
        padding: 0;
        color: #184e98;
        font-size: 100px;
    }
</style>