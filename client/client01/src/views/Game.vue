<!--   
    Version: 3.2.41
    Auhtor: Felix Hansmann
    
    Die Komponente "Game.vue" ist für die Darstellung und Anwendungslogik, 
    der Fragen und entsprechender Evaluation, zuständig.
-->
<template>
    <!-- Header Komponente -->
    <Header></Header>
    <!-- zeige Countdown -->
    <div class="countdown" v-show="!seen">
        <span><b>Start in:</b></span>
        <span class="showCountdown"><b>{{startCountdown}}</b></span>
    </div>
    <!-- zeige Frage und Antworten -->
    <div class="game" v-show="seen && !seenGivenAnswer">
        <!-- Komponente für die aktuelle Runde -->
        <GameRound :text="`${this.currentRound}`"></GameRound>
        <!-- Komponente für die Darstellung der teilnehmenden Spieler -->
        <Versus 
            :profilIconOne="`${'http://test.burmeister.hamburg/static/' + profilIconOne + '.jpg'}`" 
            :profilIconTwo="`${'http://test.burmeister.hamburg/static/' + profilIconTwo + '.jpg'}`">
        </Versus>
        <!-- Komponente für Frage und Antowrt-->
        <Question
            :topic="`${this.categoryName}`" 
            :question="`${this.questionText}`" 
            :answerOne="`${this.answerOne}`" @answerOne="chosenAnswer(this.answerValue[0])" 
            :answerTwo="`${this.answerTwo}`" @answerTwo="chosenAnswer(this.answerValue[1])"
            :answerThree="`${this.answerThree}`" @answerThree="chosenAnswer(this.answerValue[2])" 
            :answerFour="`${this.answerFour}`" @answerFour="chosenAnswer(this.answerValue[3])"
            :roundCountdown="`${this.roundCountdown}`">
        </Question>
        <!-- Button für Spielabbruch -->
        <div class="btnWrapper">
            <Button text="Spiel abbrechen" @click="abortQuiz()"></Button>
        </div>
    </div>
    <!-- zeige Evaluation der Frage -->
    <div class="roundEvaluation" v-show="seenGivenAnswer" >
        <span class="round">Runde {{ currentRound }}</span>
        <span class="answer">Deine Antwort: <br><b>{{ answerAsText }}{{ noAnswer }}</b></span>
        <span class="result" :style="{color: this.textColor}">{{ isCorrectAnswer }}</span>   
        <span class="waitNextRound" v-if="currentRound <= 5">Gleich gehts weiter
            <div class="col-3">
                <div class="snippet" data-title="dot-flashing">
                    <div class="stage">
                        <div class="dot-flashing"></div>
                    </div>
                </div>
            </div>
        </span> 
        <span class="waitNextRound" v-else>Spiel wird ausgewertet
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
            noAnswer: "",
            notSelected: false,
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

    /**
     * Der Lifecycle Hook "created" stellt alle benötigten REST Api und Websocket Verbinungen her.
     */
    async created()
    {
        //Stellt die Verbindung zum Quiz, per REST, her.
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
        
            //Initialisierung und Deklaration der WebSocket-Verbindung
            const token = "Bearer " + localStorage.getItem("token");
            this.connection = new SockJS("http://localhost:8080/quiz-websocket");
            const stompClient = Stomp.over(this.connection);

            //Stellt die Verbindung mit dem Quiz Websocket Endpunkt her.
            stompClient.connect({ Authorization: token }, 
                (frame) =>
                {
                    //Websocket Endpunkt zum Abonnieren für Updates einer QuizSession
                    stompClient.subscribe("/topic/quiz/session/" + localStorage.getItem("lobbyId"), 
                        (message) =>
                        {
                            let json = JSON.parse(message.body);
                            this.currentRound = json.currentRound
                            this.categoryName = json.categoryName
                            this.questionText = json.questionText
                            this.answerOne = json.answerOne
                            this.answerTwo = json.answerTwo
                            this.answerThree = json.answerThree
                            this.answerFour = json.answerFour
                    
                            this.questionEvaluation(json);
                        }
                    );
                    
                    //Websocket Endpunkt zum Abonnieren für Änderungen an einem Quiz
                    stompClient.subscribe("/topic/quiz/" + localStorage.getItem("lobbyId"), 
                        (message) =>
                        {
                            let json = JSON.parse(message.body);
                            this.profilIconOne = json.players[0].userId;
                            this.profilIconTwo = json.players[1].userId;
                        }
                    );

                    //Websocket Endpunkt zum Abonnieren, der über Start, Countdown und Abbruch informiert
                    stompClient.subscribe("/topic/quiz/" + localStorage.getItem("lobbyId") + "/start-quiz", 
                        (message) =>
                        {
                            let json = JSON.parse(message.body);
                            this.startCountdown = JSON.stringify(json.countdown)

                            //Spiel wird sichtbar wenn der Countdown bei 0 ist
                            if(this.startCountdown == 0)
                            {
                                this.seen = !this.seen;
                            }      
                        }
                    );

                    //Websocket Endpunkt zum Abonnieren für das Spielergebnis am Ende einer QuizSession
                    stompClient.subscribe("/topic/quiz/session/" + localStorage.getItem("lobbyId") + "/result", 
                        (message) =>
                        {
                            let json = JSON.parse(message.body);
                            localStorage.setItem("quizEvaluation", JSON.stringify(json));
                            this.$router.push("/gameEvaluation")
                        }
                    );

                    //Websocket Endpunkt zum Abonnieren für den Countdown einer Runde
                    stompClient.subscribe("/topic/quiz/session/" + localStorage.getItem("lobbyId") + "/round-countdown", 
                        (message) =>
                        {
                            let json = JSON.parse(message.body);
                            this.roundCountdown = JSON.stringify(json)
                            localStorage.setItem("roundCountdown", this.roundCountdown)

                            //ausgewählte Anwort wird angezeigt, spät. wenn der Countdown der Frage bei 0 ist
                            if(this.roundCountdown === "20")
                            {
                                this.seenGivenAnswer = false;
                            }
                            else if(this.roundCountdown === "0")
                            {
                                this.seenGivenAnswer = true;
                            }
                        }
                    );   

                    //Websocket Endpunkt zum Senden für Status-Update des Spielers
                    stompClient.send("/app/quiz/" + localStorage.getItem("lobbyId") + "/status-player", {}, JSON.stringify({status: "ready"}));       
                }
            );
    },
    methods:
    {
        /**
         * Die Methode "chosenAnswer" sendet die gewählte Antwort, des Users, an den WebSocket-Endpunkt und
         * weißt Diese der Variable "answerAsText", zur Darstellung in Evaluation der Fragerunde, zu.
         * 
         * @param answer
         * 
         */
        chosenAnswer(answer)
        {
            //ausgewählte Anwort wird angezeigt
            this.seenGivenAnswer = true;
            
            //WS-Connection noch rausnehmen!
            const token = "Bearer " + localStorage.getItem("token");
            this.connection = new SockJS("http://localhost:8080/quiz-websocket");
            const stompClient = Stomp.over(this.connection);

            stompClient.connect({ Authorization: token }, 
                (frame) =>
                {
                    //Sendet an den Websocket Endpunkt die ausgewählte Antwort, auf die Frage.
                    if(answer === "1")
                    {
                        stompClient.send("/app/quiz/session/" + localStorage.getItem("lobbyId") + "/answer", {}, JSON.stringify(this.answerValue[0]));
                        this.answerAsText = this.answerOne;
                    }
                    else if(answer === "2")
                    {
                        stompClient.send("/app/quiz/session/" + localStorage.getItem("lobbyId") + "/answer", {}, JSON.stringify(this.answerValue[1]));
                        this.answerAsText = this.answerTwo;
                    }
                    else if(answer === "3")
                    {
                        stompClient.send("/app/quiz/session/" + localStorage.getItem("lobbyId") + "/answer", {}, JSON.stringify(this.answerValue[2]));
                        this.answerAsText = this.answerThree;
                    }
                    else if(answer === "4")
                    {
                        stompClient.send("/app/quiz/session/" + localStorage.getItem("lobbyId") + "/answer", {}, JSON.stringify(this.answerValue[3]));
                        this.answerAsText = this.answerFour;
                    }
                }
            );        
        },

        /**
         * Die Methode "questionEvaluation" ist für die Darstellung des Feedbacks, nach einer Fragerunde, 
         * ob eine Frage richig oder falsch beantwortet wurde.
         * 
         * @param object
         * 
         */
        questionEvaluation(object)
        {
            for(let i = 0; i < 2; i++)
            {
                if(object.roundStatus === "CLOSE") 
                {
                    //Wenn die Antwort richtig ist
                    if((object.playerList[i].chosenAnswer === object.correctAnswer) && (object.playerList[i].playerId === localStorage.getItem("userId")))
                    {
                        this.noAnswer = "";
                        this.isCorrectAnswer = "Richtig";
                        this.textColor = "green";
                    }
                    //Wenn die Antwort falsch ist
                    else if((object.playerList[i].chosenAnswer != object.correctAnswer) && (object.playerList[i].playerId === localStorage.getItem("userId")))
                    {
                        this.noAnswer = "";
                        this.isCorrectAnswer = "Falsch";
                        this.textColor = "red";
                    }
                    //Wenn keine Antwort ausgewählt wurde (nochmal überarbeiten, funktioniert noch nicht wie gewollt)
                    else if((object.playerList[i].playerRoundStatus === "GUESS") && (object.playerList[i].playerId === localStorage.getItem("userId")))
                    {
                        console.log("keine antwort gewählt")
                        this.noAnswer = "Keine Antwort ausgewählt";
                        this.isCorrectAnswer = "Falsch";
                        this.textColor = "red";
                    }    
                }
                //Setzt die Antworten und das Ergebnis, für die neue Fragerunde, zurück
                else if(object.roundStatus === "OPEN" && object.playerList[i].playerRoundStatus === "GUESS")
                {
                    if((object.playerList[i].chosenAnswer === object.correctAnswer) && (object.playerList[i].playerId === localStorage.getItem("userId")))
                    {
                        this.noAnswer = "";
                        this.answerAsText = "";
                        this.isCorrectAnswer = "";        
                    }
                    else if((object.playerList[i].chosenAnswer != object.correctAnswer) && (object.playerList[i].playerId === localStorage.getItem("userId")))
                    {
                        this.noAnswer = "";
                        this.answerAsText = "";
                        this.isCorrectAnswer = "";
                    }  
                }
            }      
        },

        /**
         * Die Methode "abortQuiz" bricht die laufende Quiz-Session ab und leitet den User zurück zur Main-Seite.
         */
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

                    console.log("Quiz-Session wurde beendet.")
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
 .dot-flashing 
 {
  position: relative;
  width: 10px;
  height: 10px;
  border-radius: 5px;
  background-color: #184e98;
  color: #184e98;
  animation: dot-flashing 1s infinite linear alternate;
  animation-delay: 0.5s;
}
.dot-flashing::before, .dot-flashing::after 
{
  content: "";
  display: inline-block;
  position: absolute;
  top: 0;
}
.dot-flashing::before 
{
  left: -15px;
  width: 10px;
  height: 10px;
  border-radius: 5px;
  background-color: #184e98;
  color: #184e98;
  animation: dot-flashing 1s infinite alternate;
  animation-delay: 0s;
}
.dot-flashing::after 
{
  left: 15px;
  width: 10px;
  height: 10px;
  border-radius: 5px;
  background-color: #184e98;
  color: #184e98;
  animation: dot-flashing 1s infinite alternate;
  animation-delay: 1s;
}

@keyframes dot-flashing 
{
  0% 
  {
    background-color: #184e98;
  }
  50%, 100% 
  {
    background-color: rgba(152, 128, 255, 0.2);
  }
}

</style>