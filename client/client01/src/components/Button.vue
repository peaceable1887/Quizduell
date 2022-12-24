<template>
    <button>{{ text }}</button>
</template>

<script>
    export default 
    {
        name: "ButtonItem",
        props:
        {
            text: String,
        }
        
    }
</script>

<style scoped>
    button
    {
        color: white;
        background-color: #184e98;
        border: none;
        cursor: pointer;
    }
    button:hover
    {
        color: rgb(177, 177, 177);
    }
</style>