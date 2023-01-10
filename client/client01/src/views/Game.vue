<template>
    <Header></Header>
    <div class="countdown" v-show="!seen">
        <span><b>Start in:</b></span>
        <span class="showCountdown"><b>{{startCountdown}}</b></span>
    </div>
    <div class="game" v-show="seen">
        <GameRound :text="`${this.currentRound}`"></GameRound>
        <Versus></Versus>
        <Question 
            :topic="`${this.categoryName}`" 
            :question="`${this.questionText}`" 
            :answerOne="`${this.answerOne}`" @answerOne="chosenAnswer(this.answerValue[0])" 
            :answerTwo="`${this.answerTwo}`" @answerTwo="chosenAnswer(this.answerValue[1])"
            :answerThree="`${this.answerThree}`" @answerThree="chosenAnswer(this.answerValue[2])" 
            :answerFour="`${this.answerFour}`" @answerFour="chosenAnswer(this.answerValue[3])"
            :roundCountdown="`${this.roundCountdown}`">
        </Question>
    </div>
    <div class="btnWrapper">
        <Button text="Spiel abbrechen" @click="abortQuiz()"></Button>
    </div>
</template>

<script>
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import Header from "../components/Header.vue";
import Button from "../components/Button.vue";
import GameRound from "../components/GameRound.vue";
import Versus from "../components/Versus.vue";
import Question from "../components/Question.vue";

    export default 
    {
        name: "GameItem",   
        components:
        {
            Header,
            Button,
            GameRound,
            Versus,
            Question,
        },
        data()
        {
            return{
               seen: false,
               startCountdown: "5",
               currentRound: "6",
               categoryName: "",
               questionText: "",
               answerOne: "1",
               answerTwo: "2",
               answerThree: "3",
               answerFour: "4",
               answerValue: ["1","2","3","4"],
               roundCountdown: "",
            }
        },
        async created()
        {
            await fetch("http://localhost:8080/api/quiz/v1/connect", {
                    method: "POST",
                    headers: 
                    {
                        "Content-Type": "application/json",
                        Authorization: "Bearer " + localStorage.getItem("token")
                        
                    },
                    body: JSON.stringify
                    ({
                        gameToken: localStorage.getItem("gameToken"), 
                        lobbyId: localStorage.getItem("lobbyId"),
                        playerId: localStorage.getItem("userId"),
                    })
                })
                .then(res => {
                    console.log(res)
                })
                .then(data => 
                {
                    console.log("Erfolgreich mit Quiz verbunden!")
                    console.log(data)
                })
                .catch(err => {
                    console.log(err)
                })
            
                const token = "Bearer " + localStorage.getItem("token");
    
                this.connection = new SockJS("http://localhost:8080/quiz-websocket");
                const stompClient = Stomp.over(this.connection);

                stompClient.connect({ Authorization: token }, 
                    (frame) =>
                    {
                        stompClient.subscribe("/topic/quiz/session/" + localStorage.getItem("lobbyId"), 
                            (message) =>
                            {
                                console.log("---------------------- GET CURRENT SESSION STATUS ----------------------");
                                let json = JSON.parse(message.body);

                                this.currentRound = JSON.stringify(json.currentRound)
                                this.categoryName = JSON.stringify(json.categoryName)
                                this.questionText = JSON.stringify(json.questionText)
                                this.answerOne = JSON.stringify(json.answerOne)
                                this.answerTwo = JSON.stringify(json.answerTwo)
                                this.answerThree = JSON.stringify(json.answerThree)
                                this.answerFour = JSON.stringify(json.answerFour)

                            }
                        );

                        stompClient.subscribe("/topic/quiz/" + localStorage.getItem("lobbyId"), 
                            (message) =>
                            {
                                console.log("---------------------- QUIZ WS ----------------------");
                                let json = JSON.parse(message.body);
                                console.log(json)
                            }
                        );
                        stompClient.subscribe("/topic/quiz/" + localStorage.getItem("lobbyId") + "/start-quiz", 
                            (message) =>
                            {
                                console.log("---------------------- START QUIZ ----------------------");
                                let json = JSON.parse(message.body);
                                console.log(json)
                                this.startCountdown = JSON.stringify(json.countdown)
                                if(this.startCountdown == 0)
                                {
                                    this.seen = !this.seen;
                                    console.log("counter bei 0")
                                }
                                console.log(this.startCountdown)
                               
                            }
                        );
                        stompClient.subscribe("/topic/quiz/session/" + localStorage.getItem("lobbyId") + "/result", 
                            (message) =>
                            {
                                console.log("---------------------- RESULT STATUS ----------------------");
                                let json = JSON.parse(message.body);
                                console.log(JSON.stringify(json))
                                localStorage.setItem("quizEvaluation", JSON.stringify(json));
                                this.$router.push("/questionEvaluation")
                            }
                        );
                        stompClient.subscribe("/topic/quiz/session/" + localStorage.getItem("lobbyId") + "/round-countdown", 
                            (message) =>
                            {
                                console.log("---------------------- ROUND COUNTDOWN ----------------------");
                                let json = JSON.parse(message.body);
                                console.log(JSON.stringify(json))
                                this.roundCountdown = JSON.stringify(json)
                            }
                        );   
                        console.log("---------------------- SET STATUS TO READY ----------------------");
                        stompClient.send("/app/quiz/" + localStorage.getItem("lobbyId") + "/status-player", {}, JSON.stringify({status: "ready"}));
                        /*stompClient.send("/app/quiz/" + this.urlId + "/status-player", , {}, JSON.stringify({status: this.status}));*/
                       
                    }
                );
        },
        async mounted()
        {
          
                
        },
        methods:
        {
           chosenAnswer(answer)
            {
                const token = "Bearer " + localStorage.getItem("token");
                this.connection = new SockJS("http://localhost:8080/quiz-websocket");
                const stompClient = Stomp.over(this.connection);
                stompClient.connect({ Authorization: token }, 
                    (frame) =>
                    {
                        if(answer === "1")
                        {
                            console.log("---------------------- Antowrt 1")
                            console.log("Antwort: " +  JSON.stringify(this.answerValue[0]))
                            stompClient.send("/app/quiz/session/" + localStorage.getItem("lobbyId") + "/answer", {}, JSON.stringify(this.answerValue[0]));
                        }
                        if(answer === "2")
                        {
                            console.log("---------------------- Antowrt 2")
                            console.log("Antwort: " + this.answerValue[1])
                            stompClient.send("/app/quiz/session/" + localStorage.getItem("lobbyId") + "/answer", {}, JSON.stringify(this.answerValue[1]));
                        }
                        if(answer === "3")
                        {
                            console.log("---------------------- Antowrt 3")
                            console.log("Antwort: " + this.answerValue[2])
                            stompClient.send("/app/quiz/session/" + localStorage.getItem("lobbyId") + "/answer", {}, JSON.stringify(this.answerValue[2]));
                        }
                        if(answer === "4")
                        {
                            console.log("---------------------- Antowrt 4")
                            console.log("Antwort: " + this.answerValue[3])
                            stompClient.send("/app/quiz/session/" + localStorage.getItem("lobbyId") + "/answer", {}, JSON.stringify(this.answerValue[3]));
                        }
                     
                    }
                );
               
            },
            async abortQuiz()
            {
                await fetch("http://localhost:8080/api/quiz/v1/cancel", {
                    method: "POST",
                    headers: 
                    {
                        "Content-Type": "application/json",
                        Authorization: "Bearer " + localStorage.getItem("token")
                        
                    },
                    body: JSON.stringify
                    ({
                        lobbyId: localStorage.getItem("lobbyId"),      
                    })
                }).then(res => 
                {
                    if(res.ok){

                        console.log("Quiz wurde beendet.")
                        this.$router.push("/main")
        
                    }else{
                        
                        console.log("Quiz konnte nicht beendet werden.")
                    }
                }) 
            }
        }
    }
</script>

<style scoped>
.countdown
{
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    font-size: 60px;
    padding: 50% 0 50% 0;
    color: #184e98;
}
.btnWrapper
{
    display: flex;
    justify-content: center;
    padding: 40px 0 40px 0;
}
Button
{
    width: 220px;
    padding: 12px 0 12px 0;
    font-size: 22px;
    align-items: center;
    background-color: red;
}
</style>