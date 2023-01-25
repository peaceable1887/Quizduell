<!--   
    Version: 3.2.41
    Auhtor: Felix Hansmann
    
    Die Komponente "GameEvaluation.vue" ist für die Darstellung und Anwendungslogik, 
    der Auswertung des gesamten Spiels zuständig.
-->
<template>
    <!-- Header Komponente -->
    <Header></Header>
    <!-- Headline Komponente -->
    <Headline class="headline" text="Spielauswertung"></Headline>
    <!-- zeigt alle Antworten der entsprechenden Runden -->
    <div class="evaluationContent">
        <div class="tableWrapper">
            <table>
                <tr>
                    <th>{{playerTwo}}</th>
                    <th>Runde</th>
                    <th>{{playerOne}}</th>
                </tr>
                <!-- listet alle ausgewählten Antworten jeder Runde auf. Mit entsprechender Farbe nach Richtig/Falsch -->
                <tbody v-for="round in rounds" :key="round" >
                    <tr>
                        <td :style="{color: answerRight}" v-if="round.correctAnswer === round.playerList[1].chosenAnswer">{{answerAsText(round, 1)}} 
                        </td>
                        <td :style="{color: answerFalse}" v-else>{{answerAsText(round, 1)}} 
                        </td>
                        <td class="categoryName">{{ round.categoryName }}</td>
                        <td :style="{color: answerRight}" v-if="round.correctAnswer === round.playerList[0].chosenAnswer">{{answerAsText(round, 0)}} 
                        </td>
                        <td :style="{color: answerFalse}" v-else>{{answerAsText(round, 0)}} 
                        </td>
                    </tr>
                </tbody>
                    <!-- zeigt Endergebnis -->
                    <tr class="tableSum">
                        <td>{{resultTwo}}</td>
                        <td class="resultCol">Ergebnis</td>
                        <td>{{resultOne}}</td>
                    </tr>             
            </table>
        </div>
        <!-- Button führt zurück zum Hauptmenü -->
        <div class="btnWrapper">
            <Button text="zum Hauptmenü" @click="backToMain"></Button>
        </div>
    </div>
</template>

<script>

import Header from "../components/Header.vue";
import Headline from "../components/Headline.vue";
import Button from "../components/Button.vue";

export default 
{
    name: "GameEvaluation",
    components:
    {
        Header,
        Headline,
        Button,
    },
    data()
    {
        return{
            playerOne: "",
            playerTwo: "",
            quizEvaluation: "",
            categoryName: "",
            chosenAnswer: "",
            answerText: "",
            resultOne: "",
            resultTwo: "",
            points: "",
            rounds: [],
            answers:[],
            answerRight: "green",
            answerFalse: "red"

        }
    },
    /**
     * Der Lifecycle Hook "created" stellt alle benötigten REST Api und/oder Websocket Verbinungen her.
     */
    async created()
    {
        //Spielergebnis des Spiels
        this.quizEvaluation = localStorage.getItem("quizEvaluation")

        await fetch("http://localhost:8080/api/quiz/v1/get-session?lobbyId=" + localStorage.getItem("lobbyId"), {
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
                console.log(data) 
                this.rounds = data.roundList;
                this.resultOne = data.quizSessionResult.players[0].playerResult;
                this.resultTwo = data.quizSessionResult.players[1].playerResult;
                this.playerOne = data.playerList[0].name;
                this.playerTwo = data.playerList[1].name;
                
            })
            .catch(err => console.log("ERROR: " + err))
    },
    methods:
    {
        /**
         * Die Methode "answerAsText" zeigt die Antwort, in Textform, entsprechend der Array-Position an.
         * 
         * @param {roundlist}
         * @param {number}
         * @return {answerOne, answerTwo, answerThree, answerFour, keine Antwort}
         * 
         */
        answerAsText(roundList, number)
        {
            if(roundList.playerList[number].chosenAnswer === 1)
            {
                return roundList.answerOne;
            }
            else if(roundList.playerList[number].chosenAnswer === 2)
            {
                return roundList.answerTwo
            }
            else if(roundList.playerList[number].chosenAnswer === 3)
            {
                return roundList.answerThree
            }
            else if(roundList.playerList[number].chosenAnswer === 4)
            {
                return roundList.answerFour
            }
            else
            {
                return "keine Antwort"
            }            
        },

        /**
         * Die Methode "backToMain" beendet die Quizsession, löscht lokal gespeicherte Items und leitet in Hauptmenü zurück.
         * 
         */
        async backToMain()
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

            localStorage.removeItem("quizEvaluation");
            this.$router.push("/main")
        }
    }
}
</script>

<style scoped>
.headline
{
    font-size: 40px;
    padding: 80px 0 80px 0;
    text-align: center;
}
.evaluationContent
{
    display: flex;
    flex-direction: column;
    justify-content: center;
}
.tableWrapper
{
    display: flex;
    justify-content: center;
}
table
{
    width: 60%
}
th
{
    font-size: 24px;
    color: #184e98;
    text-align: center;
    border-bottom: 1px rgb(168, 168, 168) solid;
    padding: 0 0 15px 0;
}
.categoryName
{
    border-left: 1px rgb(168, 168, 168) solid;
    border-right: 1px rgb(168, 168, 168) solid;
}
td
{
    font-size: 22px;
    text-align: center;
    padding: 10px 0 10px 0;
}
.tableSum
{
    color: #184e98;
    font-weight: bold;
    font-size: 24px;
}
.tableSum .resultCol
{
    border-left: 1px rgb(168, 168, 168) solid;
    border-right: 1px rgb(168, 168, 168) solid;
}
.btnWrapper
{
    display: flex;
    justify-content: center;
    padding: 100px 0 0 0;
}
Button
{
    width: 200px;
    padding: 12px 0 12px 0;
    font-size: 22px;
    text-align: center;
}
.noAnswer
{
    color: black;
}
</style>