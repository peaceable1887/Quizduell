<!--   
    Version: 3.2.41
    Auhtor: Felix Hansmann
    
    Die Komponente "Question.vue" wird in die Game-Komponente geladen und definiert die Darstellung, der Fragen und dazugehörigen Antwortmöglichkeiten.
    Des Weiteren sind hier ein Timer, für jede Frage, mit eingebaut.
-->
<template>
    <QuestionCategory :topic="`${topic}`"></QuestionCategory>
    <div class="container">
        <div class="question">
            <div class="text">
                {{question}}
            </div>            
        </div>  
        <div id="time">
            <div :style="{color: timerColor}">{{roundCountdown}}</div>
        </div>
        <form class="answers" @submit.prevent>
            <div class="option">
                <input type="submit" :value="`${answerOne}`" @click="$emit('answerOne')">
                <input type="submit" :value="`${answerTwo}`" @click="$emit('answerTwo')">
            </div>
            <div class="option">
                <input type="submit" :value="`${answerThree}`" @click="$emit('answerThree')">
                <input type="submit" :value="`${answerFour}`" @click="$emit('answerFour')">
            </div>
        </form>
    </div>
</template>

<script>
import QuestionCategory from "./QuestionCategory.vue";

export default 
{
    name: "QuestionItem",
    emits: ["answerOne","answerTwo","answerThree","answerFour"],
    components:
    {
        QuestionCategory,
    },
    props:
    {
        topic: String,
        question: String,
        answerOne: String,
        answerTwo: String,
        answerThree: String,
        answerFour: String,
        roundCountdown: String,
    },
    data()
    {
        return{
            
           connection: "",
           timerColor: "",
        }
    },
    updated()
    {
        if(this.roundCountdown <= 5)
        {       
            this.timerColor = "red";

        }else if(this.roundCountdown <= 10)
        {
            this.timerColor = "yellow";

        }else if(this.roundCountdown <= 20)
        {
            this.timerColor = "green";
        }       
    },
}
</script>

<style scoped>
.container
{
    display: flex;
    margin: auto;
    flex-direction: column;
    margin-top: 15px;
    align-items: center;
    width: 850px;
}
.question
{
    box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
    -webkit-box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
    -moz-box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
    width: 100%; 
    border-radius: 5px;      
}
.question .counter
{
    font-size: 30px;
    font-weight: bold;
    padding: 10px 0 0 10px;
    color: #184e98;
}
.question .text
{
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: center;
    font-size: 30px;
    height: 200px;
}
#time
{
    width: 100%;
    display: flex;
    justify-content: center;
    margin: 30px 0 10px 0;
    font-size: 34px;
    font-weight: bold;
}
.answers
{
    padding-top: 20px;
}
.answers .option
{
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;  
}
.answers .option input
{
    font-size: 24px;
    width: 400px;
    height: 170px;
    margin: 10px 20px 10px 20px;
    background-color: #184e98;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
    -webkit-box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
    -moz-box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
    word-wrap: break-word;
}
.answers .option input:hover
{ 
    background-color: #0d2d5a;
    color: white;
    border: none;
}
@media screen and (max-width:650px) 
{
    .question .counter{font-size: 24px; padding: 5px 0 0 5px;}
    .question .text{font-size: 20px; height: 160px;}
    .answers{padding-top: 40px}
    .answers .option input{width: 180px;height: 100px; font-size: 20px;}
}
</style>