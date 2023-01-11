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
    border-radius: 5px;
}
button:hover
{
    color: rgba(255, 255, 255, 0.678);
}
</style>