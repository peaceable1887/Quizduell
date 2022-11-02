<template>
    <button>{{ text }}</button>
</template>

<script>
    export default 
    {
        name: "Button",
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
        background-color: #216bd3;
    }
</style>