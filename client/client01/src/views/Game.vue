<template>
    <Header></Header>
    <div class="countdown" v-show="!seen">
        <span><b>Start in:</b></span>
        <span class="showCountdown"><b>{{startCountdown}}</b></span>
    </div>
    <div class="game" v-show="seen && !seenGivenAnswer">
        <GameRound :text="`${this.currentRound}`"></GameRound>
        <Versus 
            :profilIconOne="`${'http://test.burmeister.hamburg/static/' + profilIconOne + '.jpg'}`" 
            :profilIconTwo="`${'http://test.burmeister.hamburg/static/' + profilIconTwo + '.jpg'}`">
        </Versus>
        <Question
            :topic="`${this.categoryName}`" 
            :question="`${this.questionText}`" 
            :answerOne="`${this.answerOne}`" @answerOne="chosenAnswer(this.answerValue[0])" 
            :answerTwo="`${this.answerTwo}`" @answerTwo="chosenAnswer(this.answerValue[1])"
            :answerThree="`${this.answerThree}`" @answerThree="chosenAnswer(this.answerValue[2])" 
            :answerFour="`${this.answerFour}`" @answerFour="chosenAnswer(this.answerValue[3])"
            :roundCountdown="`${this.roundCountdown}`">
        </Question>
        <div class="btnWrapper">
            <Button text="Spiel abbrechen" @click="abortQuiz()"></Button>
        </div>
    </div>
    <div class="roundEvaluation" v-show="seenGivenAnswer">
        <span class="round">Runde {{ currentRound }}</span>
        <span class="answer">Deine Antwort: <br><b>{{ answerAsText }}</b></span> 
        <span class="result" :style="{color: this.textColor}">{{ isCorrectAnswer }}</span>   
        <span class="waitNextRound">Gleich gehts weiter
            <div class="col-3">
                <div class="snippet" data-title="dot-flashing">
                    <div class="stage">
                        <div class="dot-flashing"></div>
                    </div>
                </div>
            </div>
        </span>  
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
            seenGivenAnswer: false,
            answerAsText: "",
            isCorrectAnswer: "",
            startCountdown: "5",
            currentRound: "6",
            categoryName: "",
            questionText: "",
            answerOne: "",
            answerTwo: "",
            answerThree: "",
            answerFour: "",
            answerValue: ["1","2","3","4"],
            roundCountdown: "",
            profilIcon: localStorage.getItem("profilIcon"),
            profilIconOne: "",
            profilIconTwo: "",
            textColor: "black",
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
            .then(res => 
            {
                console.log(res)
            })
            .then(data => 
            {
                console.log("Erfolgreich mit Quiz verbunden!")
                console.log(data)
            })
            .catch(err => 
            {
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

                            this.currentRound = json.currentRound
                            this.categoryName = json.categoryName
                            this.questionText = json.questionText
                            this.answerOne = json.answerOne
                            this.answerTwo = json.answerTwo
                            this.answerThree = json.answerThree
                            this.answerFour = json.answerFour

                            //In Funktion auslagern
                            for(let i = 0; i < 2; i++)
                            {
                                if(json.playerList[i].playerRoundStatus === "FINISH")
                                {
                                    if(json.roundStatus === "CLOSE" && (json.playerList[i].chosenAnswer === json.correctAnswer) && (json.playerList[i].playerId === localStorage.getItem("userId")))
                                    {
                                        this.isCorrectAnswer = "Richtig";
                                        this.textColor = "green";
                                    }
                                    if(json.roundStatus === "CLOSE" && (json.playerList[i].chosenAnswer != json.correctAnswer) && (json.playerList[i].playerId === localStorage.getItem("userId")))
                                    {
                                        this.isCorrectAnswer = "Falsch";
                                        this.textColor = "red";
                                    }
                                }
                                else
                                {
                                    this.isCorrectAnswer = "";
                                }            
                            }                            
                        }
                    );

                    stompClient.subscribe("/topic/quiz/" + localStorage.getItem("lobbyId"), 
                        (message) =>
                        {
                            console.log("---------------------- QUIZ WS ----------------------");
                            let json = JSON.parse(message.body);
                            console.log(json)
                            this.profilIconOne = json.players[0].userId;
                            this.profilIconTwo = json.players[1].userId;
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
                            localStorage.setItem("roundCountdown", this.roundCountdown)

                            if(this.roundCountdown === "20")
                            {
                                console.log("json.roundStatus")
                                this.seenGivenAnswer = false;
                            }
                            if(this.roundCountdown === "0")
                            {
                                console.log("json.roundStatus")
                                this.seenGivenAnswer = true;
                            }
                        }
                    );   
                    console.log("---------------------- SET STATUS TO READY ----------------------");
                    stompClient.send("/app/quiz/" + localStorage.getItem("lobbyId") + "/status-player", {}, JSON.stringify({status: "ready"}));
                    /*stompClient.send("/app/quiz/" + this.urlId + "/status-player", , {}, JSON.stringify({status: this.status}));*/         
                }
            );
    },
    methods:
    {
        chosenAnswer(answer)
        {
            this.seenGivenAnswer = true;
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
                        console.log(this.answerOne)
                        this.answerAsText = this.answerOne;
                    }
                    if(answer === "2")
                    {
                        console.log("---------------------- Antowrt 2")
                        console.log("Antwort: " + this.answerValue[1])
                        stompClient.send("/app/quiz/session/" + localStorage.getItem("lobbyId") + "/answer", {}, JSON.stringify(this.answerValue[1]));
                        console.log(this.answerTwo)
                        this.answerAsText = this.answerTwo;
                    }
                    if(answer === "3")
                    {
                        console.log("---------------------- Antowrt 3")
                        console.log("Antwort: " + this.answerValue[2])
                        stompClient.send("/app/quiz/session/" + localStorage.getItem("lobbyId") + "/answer", {}, JSON.stringify(this.answerValue[2]));
                        this.answerAsText = this.answerThree;
                    }
                    if(answer === "4")
                    {
                        console.log("---------------------- Antowrt 4")
                        console.log("Antwort: " + this.answerValue[3])
                        stompClient.send("/app/quiz/session/" + localStorage.getItem("lobbyId") + "/answer", {}, JSON.stringify(this.answerValue[3]));
                        this.answerAsText = this.answerFour;
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
    height: 100vh;
    color: #184e98;
}
.roundEvaluation
{
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    text-align: center;
    font-size: 50px;
    height: 100vh;
    color: #184e98;
}
.round
{
    font-size: 60px;
    font-weight: bold;
}
.answer
{
    font-size: 40px;
    margin: 30px 0 50px 0;
    color: black;
}
.result
{
    font-size: 60px;
    margin-bottom: 50px;
    font-weight: bold;
}
.waitNextRound
{
    font-size: 24px;
    color: #184e98;
}
.showCountdown
{
    color: green;
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
/**
 * ==============================================
 * Dot Flashing
 * ==============================================
 */
 .col-3
 {
    display: flex;
    justify-content: center;
    margin-top: 20px;
 }
 .dot-flashing {
  position: relative;
  width: 10px;
  height: 10px;
  border-radius: 5px;
  background-color: #184e98;
  color: #184e98;
  animation: dot-flashing 1s infinite linear alternate;
  animation-delay: 0.5s;
}
.dot-flashing::before, .dot-flashing::after {
  content: "";
  display: inline-block;
  position: absolute;
  top: 0;
}
.dot-flashing::before {
  left: -15px;
  width: 10px;
  height: 10px;
  border-radius: 5px;
  background-color: #184e98;
  color: #184e98;
  animation: dot-flashing 1s infinite alternate;
  animation-delay: 0s;
}
.dot-flashing::after {
  left: 15px;
  width: 10px;
  height: 10px;
  border-radius: 5px;
  background-color: #184e98;
  color: #184e98;
  animation: dot-flashing 1s infinite alternate;
  animation-delay: 1s;
}

@keyframes dot-flashing {
  0% {
    background-color: #184e98;
  }
  50%, 100% {
    background-color: rgba(152, 128, 255, 0.2);
  }
}

</style>