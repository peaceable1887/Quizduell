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
                }).then(res => {
                        if(res.ok){

                            console.log("Verbinden mit dem Spiel hat geklappt!")
                            
            
                        }else{
                            
                            console.log("Fehler ist aufgetreten. Verbinden mit dem Spiel hat nicht geklappt!")
                        }
                    }),

            await fetch("http://localhost:8080/api/quiz/v1/get", {
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
                }).then(res => {
                        if(res.ok){

                            console.log("Quiz wurde geholt!")
                            
            
                        }else{
                            
                            console.log("Fehler ist aufgetreten. Quiz konnte nicht geholt werden!")
                        }
                    })
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
                }).then(res => {
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