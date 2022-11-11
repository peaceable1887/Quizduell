<template>
    <div class="container">
        <div class="topic">
            {{topic}}
        </div>
    </div>
</template>

<script>
    export default 
    {
        name: "QuestionCategory",    
        props:
        {
            topic: String,
        }
    }
</script>

<style scoped>
.container
{
    display: flex;
    justify-content: center;
}
.topic
{
    background-color: #184e98;
    color: white;
    font-size: 30px;
    width: 250px;
    text-align: center;
    padding: 15px 80px 15px 80px;
    box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
    -webkit-box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
    -moz-box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
}
@media screen and (max-width:650px) 
{
    .topic{font-size: 20px; width: 100px; margin: 20px 0 20px 0}
}
</style>