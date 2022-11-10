<template>
    <QuestionCategory topic="Geographie"></QuestionCategory>
    <div class="container">
        <div class="question">
            <div class="counter">
                {{count}}/4
            </div>
            <div class="text">
                Wie heißt die Hauptstadt von Deutschland ?
            </div>            
        </div> 
        <div id="time">
            <div id="bar" ref="bar"></div>
        </div> 
        <form class="answers" @submit="nextQuestion">
            <div class="option">
                <input type="submit" value="Berlin">
                <input type="submit" value="Köln">
            </div>
            <div class="option">
                <input type="submit" value="Hamburg">
                <input type="submit" value="München">
            </div>
        </form>
    </div>
</template>

<script>
import QuestionCategory from "./QuestionCategory.vue";

export default 
{
    name: "Question",
    components:
    {
        QuestionCategory,
    },
    data()
    {
        return{
            count: Number(localStorage.getItem("count")) ,
        }
    },
    created()
    {
        if(this.count == 0)
        {
            this.count = 1;
            localStorage.setItem("count", this.count)
        }
        if(this.count == 5)
        {
            this.$router.push("/questionEvaluation")
            localStorage.removeItem("count");      
        }
    }, 
    mounted()
    {       
        let elem = this.$refs.bar;
        let width = 100;
        let id = setInterval(frame, 200);
        
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
        nextQuestion()
        {
            this.$router.push("/game")
            localStorage.setItem("count", this.count++ +1) 
        },
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
    }
    .question
    {
        box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
        -webkit-box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
        -moz-box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
        
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
    }
    .answers .option input
    {
        font-size: 30px;
        width: 300px;
        height: 150px;
        margin: 10px 20px 10px 20px;
        background-color: rgb(95, 95, 95);
        color: white;
        border: none;
        cursor: pointer;
        box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
        -webkit-box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
        -moz-box-shadow: 1px 4px 5px -1px rgba(0,0,0,0.74);
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