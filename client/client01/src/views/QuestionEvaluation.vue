<template>
    <Header></Header>
    <Headline class="headline" text="Spielauswertung"></Headline>
    <div class="evaluationContent">
        <div class="tableWrapper">
            <table>
                <tr>
                    <th>{{ player[1].name }}</th>
                    <th>Runde</th>
                    <th>{{player[0].name}}</th>
                </tr>
                <tr v-for="round in rounds" :key="round">
                    <td>{{ round.playerList[0].chosenAnswer }}</td>
                    <td class="categoryName">{{ round.categoryName }}Programmierung</td>
                    <td>{{ round.playerList[1].chosenAnswer }}</td>
                </tr>
                <tr class="tableSum">
                    <td></td>
                    <td class="resultCol">Ergebnis</td>
                    <td></td>
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
            rounds: [],
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

            })
    },
    methods:
    {
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
    padding-top: 30px;
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