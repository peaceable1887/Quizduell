<template>
    <Header></Header>
   <!-- <GameRound></GameRound>
    <Versus></Versus>
    <Question></Question>-->
    <button @click="abortQuiz()">Spiel Abbrechen</button>
</template>

<script>
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
                    console.log("ID: " + data.id);
                    console.log("Lobby ID: " + data.lobbyId);

                    for(let i = 0; i < 2; i++)
                    {
                        console.log("User ID: " + data.players[i].userId)
                        console.log("Name: " + data.players[i].name)
                    }
                })
            
               /* const token = "Bearer " + localStorage.getItem("token");

                this.connection = new SockJS("http://localhost:8080/lobby-websocket");
                const stompClient = Stomp.over(this.connection);

                stompClient.connect({ Authorization: token }, 
                    (frame) =>
                    {
                        console.log("Connected: " + frame);
                        
                        stompClient.subscribe("/topic/quiz/session/" + localStorage.getItem("lobbyId"), 
                            (message) =>
                            {
                                let json = JSON.parse(message.body);
                                this.lobbies = json;
                            }
                        );
                        stompClient.subscribe("/topic/lobby/delete-lobby", 
                            (message) =>
                            {
                                console.log("deleted lobbies");
                                let json = JSON.parse(message.body);
                                console.log("gelöschte lobbies:" + JSON.stringify(json))
                            }
                        );
                    }
                );
*/
           /* await fetch("http://localhost:8080/api/quiz/v1/get", {
                method: "GET",
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

                        console.log("Quiz wurde geholt!")
                        
        
                    }else{
                        
                        console.log("Fehler ist aufgetreten. Quiz konnte nicht geholt werden!")
                    }
                })*/

                await fetch("http://localhost:8080/api/quiz/v1/get", {
                    method: "POST",
                    headers: 
                    {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + localStorage.getItem("gameToken")
                    },      
                })
                .then(res => res.json())
                .then(data => 
                {
                    console.log("Quiz ID: " + data.quizId);
                    console.log("Lobby ID: " + data.lobbyId);
                    console.log("Quizstatus: " + data.quizStatus);

                    for(let i = 0; i < 2; i++)
                    {
                        console.log("User ID: " + data.quizStatus[i].userId)
                        console.log("Name: " + data.quizStatus[i].name)
                        console.log("Status: " + data.quizStatus[i].status)
                    }
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