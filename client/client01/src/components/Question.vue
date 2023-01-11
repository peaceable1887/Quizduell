<template>
    <QuestionCategory :topic="`${topic}`"></QuestionCategory>
    <div class="container">
        <div class="question">
            <div class="text">
                {{question}}
            </div>            
        </div>  
        <div id="time">
            <div id="bar" ref="bar"></div>
            <div>Timer: {{ roundCountdown}} Sekungen</div>
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
        
        }
    },
    async created()
    {
    

    }, 
    updated()
    {
        let elem = this.$refs.bar;
        let width = 100;
        let millisec = localStorage.getItem("roundCountdown")*100;
        console.log("Roundcountdown" + millisec)
        let id = setInterval(frame, millisec);
    
        function frame() 
        {
            if (width == 0)
            {
                clearInterval(id);
                console.log("Die Zeit ist abgelaufen!");

            }else if(width >= 50)
            {       
                width--;
                elem.style.width = width + "%";

            }else if(width <= 50 && width >= 20)
            {
                elem.style.backgroundColor = "yellow";
                width--;
                elem.style.width = width + "%";

            }else if(width <= 20)
            {
                elem.style.backgroundColor = "red";
                width--;
                elem.style.width = width + "%";
            }
        }          
    },
   
    methods:
    {
        
    }   

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
    }
    .question
    {
        box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
        -webkit-box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
        -moz-box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
        width: 100%;
        
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
    }
    #bar
    {
        width: 100%;
        height: 20px;
        background-color: green;
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
        font-size: 30px;
        width: 400px;
        height: 170px;
        margin: 10px 20px 10px 20px;
        background-color: rgb(95, 95, 95);
        color: white;
        border: none;
        cursor: pointer;
        box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
        -webkit-box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
        -moz-box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
        word-wrap: break-word;
    }
    .answers .option input:hover
    { 
        background-color: white;
        color: black;
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