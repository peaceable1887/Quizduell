<template>
    <Header></Header>
    <div class="containerGame">
        <Headline class="headline" :text="`${lobby.name}`"></Headline>
        <div class="gameInfos">
            <span>Anzahl der Runden: 4</span>
            <span>erstellt von: {{lobby.players[0].userId}}</span>
        </div>
        <div class="playerContainer" v-for="player in players" :key="player">
            <PlayerInLobby 
                :playerName="`${player.userId}`" 
                @toggleBtn="toggle()"
                :btnText="`${playerStatus ? 'Bereit' : 'Nicht bereit'}`">
            </PlayerInLobby>
        </div>
        <div class="btnWrapper">
            <router-link to="/lobby">
                <Button @click="deleteLobby(this.urlId)" text="Verlassen"></Button>
            </router-link>
            <Button type="submit" text="Spiel starten"></Button>                 
        </div>
    </div>  
</template>

<script>
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import Header from "../components/Header.vue";
import Headline from "../components/Headline.vue";
import PlayerInLobby from "../components/PlayerInLobby";
import Button from "../components/Button.vue"

export default 
{
    name: "GameLobby",
    components:
    {
        Header,
        Headline,
        PlayerInLobby,
        Button,
    },
    data()
    {
        return{
            connection: null,
            lobby: [],
            urlId: "",
            players:[],
            playerStatus: false,
            token: "",
        }
    },
    created()
    {
        const url = window.location.href;
        const lastSegment = url.split("/").pop();
        this.urlId = lastSegment;
        
        const token = "Bearer " + localStorage.getItem("token");
    
        this.connection = new SockJS("http://localhost:8080/lobby-websocket");
        const stompClient = Stomp.over(this.connection);

        stompClient.connect({ Authorization: token }, 
            (frame) =>
            {
                console.log("Connected: " + frame);
                
                stompClient.subscribe("/topic/lobby/" + this.urlId, 
                    (message) =>
                    {
                        console.log("new lobby");
                        let json = JSON.parse(message.body);
                        this.lobby = json;
                        this.players = this.lobby.players;

                        console.log("hier:" + JSON.stringify(this.lobby.players[0].userId))

                    }
                );
                stompClient.subscribe("/topic/lobby/" + this.urlId + "/status", 
                    (message) =>
                    {
                        console.log("change status");
                        let json = JSON.parse(message.body);
                        console.log(JSON.stringify(json))
                    }
                );
            }
        );
        
        

    },
    methods:
    {
        async deleteLobby(lobbyId)
        {
            fetch("http://localhost:8080/api/lobby/v1/disconnect",
            {
                method: "POST",
                headers:
                {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + localStorage.getItem("token")
                },
                body: JSON.stringify(
                {
                    lobbyId: lobbyId,
                })
            })
            .then(res => 
                {
                    if(res.ok)
                    {
                        console.log("Lobby wurde gelöscht!")
                    }else{
                        console.log("Fehler beim löschen der Lobby!")
                    }
                })
            .then(data => console.log(data))
            .catch(error => console.log("ERROR"))       
        },

        toggle() 
        {
            this.playerStatus = this.playerStatus ? false : true;
        },

    }

}
</script>

<style scoped>
.containerGame
{
    margin: 0 15% 0 15%;
}
.headline
{
    font-size: 45px;
    padding: 50px 0 50px 0;
}
.gameInfos
{
    display: flex;
    width: 100%;
    color: black;
    font-weight: bold;
    justify-content: space-between;
    font-size: 20px;
    padding: 1% 1% 1% 1%;
    margin-bottom: 40px;
    border-bottom: 1px black solid;
}
.btnWrapper
{
    display: flex;
    justify-content: space-evenly;
    width: 100%;
    bottom: 0;
}
Button
{
    width: 200px;
    padding: 12px 0 12px 0;
    font-size: 22px;
}
</style>