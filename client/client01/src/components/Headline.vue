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
        text-align: center;
        padding: 15% 0 15% 0;
    }
    h2
    {
        margin: 0;
        padding: 0;
        color: #184e98;
    }
</style>