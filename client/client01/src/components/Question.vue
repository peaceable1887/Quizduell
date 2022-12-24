<template>
    <QuestionCategory :topic="`${category[this.rndNumber].name}`"></QuestionCategory>
    <div class="container">
        <div class="question">
            <div class="counter">
                {{count}}/4
            </div>
            <div class="text">
                {{category[this.rndNumber].fragen[count-1].frage}}
            </div>            
        </div> 
        <div id="time">
            <div id="bar" ref="bar"></div>
        </div> 
        <form class="answers" @submit="nextQuestion">
            <div class="option">
                <input type="submit" :value="`${category[this.rndNumber].fragen[count-1].antworten[0]}`">
                <input type="submit" :value="`${category[this.rndNumber].fragen[count-1].antworten[1]}`">
            </div>
            <div class="option">
                <input type="submit" :value="`${category[this.rndNumber].fragen[count-1].antworten[2]}`">
                <input type="submit" :value="`${category[this.rndNumber].fragen[count-1].antworten[3]}`">
            </div>
        </form>
    </div>
</template>

<script>
import QuestionCategory from "./QuestionCategory.vue";

export default 
{
    name: "QuestionItem",
    components:
    {
        QuestionCategory,
    },
    data()
    {
        return{
            
            count: Number(localStorage.getItem("count")),  
            rndNumber: Number(localStorage.getItem("rndNumber")),
            category:
            [
                {
                    name: "Informatik",
                    fragen: 
                    [
                        {
                            frage: "Informatik Frage 1",
                            antworten:["A11", "B11", "C11", "D11"]
                        },
                        {
                            frage: "Informatik Frage 2",
                            antworten:["A21", "B21", "C21", "D21"]
                        },                       
                        {
                            frage: "Informatik Frage 3",
                            antworten:["A31", "B31", "C31", "D31"]
                        },
                        {
                            frage: "Informatik Frage 4",
                            antworten:["A41", "B41", "C41", "D41"]
                        },
                    ],
                },
                {
                    name: "Programmmierung",
                    fragen: 
                    [
                        {
                            frage: "Programmierung Frage 1",
                            antworten:["A12", "B12", "C12", "D12"]
                        },
                        {
                            frage: "Programmierung Frage 2",
                            antworten:["A22", "B22", "C22", "D22"]
                        },                       
                        {
                            frage: "Programmierung Frage 3",
                            antworten:["A32", "B32", "C32", "D32"]
                        },
                        {
                            frage: "Programmierung Frage 4",
                            antworten:["A42", "B42", "C42", "D42"]
                        },
                    ],
                },
                {
                    name: "Datenbanken",
                    fragen: 
                    [
                        {
                            frage: "Datenbanken Frage 1",
                            antworten:["A13", "B13", "C13", "D13"]
                        },
                        {
                            frage: "Datenbanken Frage 2",
                            antworten:["A23", "B23", "C23", "D23"]
                        },                       
                        {
                            frage: "Datenbanken Frage 3",
                            antworten:["A33", "B33", "C33", "D33"]
                        },
                        {
                            frage: "Datenbanken Frage 4",
                            antworten:["A43", "B43", "C43", "D43"]
                        },
                    ],
                }                          
            ],                    
        }
    },
    created()
    {
        if(!localStorage.getItem("rndNumber"))
        {
            this.rndNumber =  Math.floor(Math.random() * this.category.length);
            localStorage.setItem("rndNumber", this.rndNumber);
        }else
        {
            console.log(localStorage.getItem("rndNumber"))
        }
        
        console.log("Zufallszahl: " + this.rndNumber);
        console.log("Zähler: " + this.count);

        if(this.count == 0)
        {
            this.count = 1;
            localStorage.setItem("count", this.count)
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
            if(this.count <= 3)
            {
                localStorage.setItem("count", this.count++ +1)
                
            }else
            {
                this.$router.push("/questionEvaluation")
                localStorage.removeItem("count");
                localStorage.removeItem("rndNumber");  
            }                  
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