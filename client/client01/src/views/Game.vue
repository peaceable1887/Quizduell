<template>
    <Header></Header>
   <!-- <GameRound></GameRound>
    <Versus></Versus>
    <Question></Question>-->
    <button @click="abortQuiz()">Spiel Abbrechen</button>
</template>

<script>
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import Header from "../components/Header.vue";
import GameRound from "../components/GameRound.vue";
import Versus from "../components/Versus.vue";
import Question from "../components/Question.vue";

    export default 
    {
        name: "GameItem",   
        components:
        {
            Header,
            GameRound,
            Versus,
            Question,
        },
        data()
        {
            return{
               test: "",
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
                .then(res => res.json())
                .then(data => 
                {
                    console.log("Erfolgreich mit Quiz verbunden!")
                    console.log(data)
                })
            
                const token = "Bearer " + localStorage.getItem("token");
    
                this.connection = new SockJS("http://localhost:8080/quiz-websocket");
                const stompClient = Stomp.over(this.connection);

                stompClient.connect({ Authorization: token }, 
                    (frame) =>
                    {
                        console.log("Connected: " + frame);
                        
                        stompClient.subscribe("/topic/quiz/" + localStorage.getItem("lobbyId"), 
                            (message) =>
                            {
                                console.log("Quiz WS");
                                let json = JSON.parse(message.body);
                            
                                console.log(json)

                            }
                        );
                    }
                );
        
            await fetch("http://localhost:8080/api/quiz/v1/get?lobbyId="+localStorage.getItem("lobbyId"), {
                method: "GET",
                headers: 
                {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + localStorage.getItem("token")
                }    
                })
                .then(res => res.json())
                .then(data => 
                {
                    console.log("Einzelnes Quiz holen")
                    console.log(data)
                })
                .catch(err => console.log(err))

            await fetch("http://localhost:8080/api/quiz/v1/get-session?lobbyId="+localStorage.getItem("lobbyId"), {
                method: "GET",
                headers: 
                {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + localStorage.getItem("token")
                }    
                })
                .then(res => res.json())
                .then(data => 
                {
                    console.log("Quizsession holen")
                    console.log(data)
                })
                .catch(err => console.log(err))

        },
        methods:
        {
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
        
                    }else{
                        
                        console.log("Quiz konnte nicht beendet werden.")
                    }
                }) 
            }
        }
    }
</script>

<style scoped>

</style>