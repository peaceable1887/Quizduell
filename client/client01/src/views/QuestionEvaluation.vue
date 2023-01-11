<template>
    <Header></Header>
    <Headline class="headline" text="Spielauswertung"></Headline>
    <div class="evaluationContent">
        <div class="tableWrapper">
            <table>
                <tr>
                    <th>{{player[1].name }}</th>
                    <th>Runde</th>
                    <th>{{player[0].name}}</th>
                </tr>
                <tbody v-for="round in rounds" :key="round" >
                    <tr>
                        <td :style="{color: answerRight}" v-if="round.correctAnswer === round.playerList[1].chosenAnswer">{{answerAsText(round, 1)}}</td>
                        <td :style="{color: answerFalse}" v-else>{{answerAsText(round, 1)}}</td>
                        <td class="categoryName">{{ round.categoryName }}Programmierung</td>
                        <td :style="{color: answerRight}" v-if="round.correctAnswer === round.playerList[0].chosenAnswer">{{answerAsText(round, 0)}}</td>
                        <td :style="{color: answerFalse}" v-else>{{answerAsText(round, 0)}}</td>
                    </tr>
                </tbody>
                <tr>
                        <td>{{ this.result.players[1].points }} / 6</td>
                        <td class="categoryName">Punkte</td>
                        <td>{{ this.result.players[0].points }} / 6</td>
                    </tr>
                    <tr class="tableSum">
                        <td>{{ this.result.players[1].playerResult }}</td>
                        <td class="resultCol">Ergebnis</td>
                        <td>{{ this.result.players[0].playerResult }}</td>
                    </tr>
                
            </table>
        </div>
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
    name: "QuestionEvaluation",
    components:
    {
        Header,
        Headline,
        Button,
    },
    data()
    {
        return{
            player: "",
            quizEvaluation: "",
            categoryName: "",
            chosenAnswer: "",
            answerText: "",
            result: "",
            points: "",
            rounds: [],
            answers:[],
            answerRight: "green",
            answerFalse: "red"

        }
    },
    async created()
    {
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
                console.log("Ausgabe: " + data.roundList[0].playerList[0].chosenAnswer);
                console.log(data)
                this.player = data.playerList
                this.rounds = data.roundList
                this.result = data.quizSessionResult

               for(let i = 0; i < 7; i++)
                {
                    console.log("-------Runde-------")
                    console.log(data.roundList[i].answerOne)
                    console.log(data.roundList[i].answerTwo)
                    console.log(data.roundList[i].answerThree)
                    console.log(data.roundList[i].answerFour)
                    console.log("korrekte antwort: " + data.roundList[i].correctAnswer)        
                }
            })
    },
    methods:
    {
        answerAsText(roundList, number)
        {
            if(roundList.playerList[number].chosenAnswer === 1)
            {
                return roundList.answerOne;
            }
            if(roundList.playerList[number].chosenAnswer === 2)
            {

                return roundList.answerTwo
            }
            if(roundList.playerList[number].chosenAnswer === 3)
            {
                return roundList.answerThree
            }
            if(roundList.playerList[number].chosenAnswer === 4)
            {
                return roundList.answerFour
            }
                
           
                
            
        },

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
</style>