<template>
    <div class="headline">
        <h2>{{ text }}</h2>
    </div>
</template>

<script>
    export default 
    {
        name: "HeadlineItem",
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
    }
    h2
    {
        margin: 0;
        padding: 0;
        color: #184e98;
    }
</style>