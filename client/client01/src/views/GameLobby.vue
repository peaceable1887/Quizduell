<template>
    <Header></Header>
    <div class="containerGame">
        <Headline class="headline" :text="`${lobby.name}`"></Headline>
        <div class="gameInfos">   
            <span><b>Anzahl der Runden:</b> 6</span>
            <span><b>erstellt von:</b> {{lobby.players[0].name}}</span>
        </div>
        <div class="playerContainer" v-for="player in players" :key="player">
            <PlayerInLobby :playerName="`${player.name}`" :profilIcon="`${'http://test.burmeister.hamburg/static/' + player.userId + '.jpg'}`"></PlayerInLobby>       
        </div>
        <div class="btnWrapper">
            <router-link to="/lobby">
                <Button @click="deleteLobby(this.urlId)" text="Verlassen"></Button>
            </router-link>
            <Button type="submit" 
                :class="`${notReady ? 'red' : 'green'}`" 
                :text="`${playerStatus ? 'Nicht bereit' : 'Bereit zum Spiel'}`"
                @click="toggle(), waitForPlayer = !waitForPlayer">
            </Button>                 
        </div>
        <div class="infoMsg">
            <span class="waitForPlayer" v-if="waitForPlayer">Warte bis der Gegenspieler bereit ist
                <div class="col-3">
                    <div class="snippet" data-title="dot-flashing">
                        <div class="stage">
                            <div class="dot-flashing"></div>
                        </div>
                    </div>
                </div>
            </span><br>
            <span class="showCountdown" v-if="showCountdown">Start in: <b>{{countdown}} Sekunden</b>{{ msg }}</span>
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
            notReady: false,
            status: "",
            token: "",
            waitForPlayer: false,
            showCountdown: false,
            abortMsg: false,
            countdown: "5",
            msg: "",
            profilIcon: localStorage.getItem("profilIcon"),
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
                stompClient.subscribe("/topic/lobby/" + this.urlId + "/start-lobby", 
                    (message) =>
                    {
                        this.waitForPlayer = false;
                        this.showCountdown = true;

                        let json = JSON.parse(message.body);
                        this.countdown = JSON.stringify(json.countdown);
                        localStorage.setItem("gameToken", json.gameToken);
                        localStorage.setItem("lobbyId", this.urlId)
                        console.log(this.countdown)
                        console.log("Status: " + JSON.stringify(json.status))

                        let removeChars = JSON.stringify(json.status);
                        removeChars = removeChars.split('"').join('');      
        
                        if(removeChars === "abort")
                        {
                            this.showCountdown = false;
                            this.waitForPlayer  = true;
                            console.log("abgebrochen")
                        }
                        if(this.countdown == 0)
                        {
                            this.$router.push("/game"); 
                        }
                            
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

        async toggle() 
        {
            if(localStorage.getItem("userId"))
            {
                console.log("du hast abgebrochen")
                this.waitForPlayer = false;
            }
            if(!this.playerStatus)
            {
                this.status = "ready" 
                console.log("------------------ready----------------")   
            }
            if(this.playerStatus)
            {
                this.status = "wait"
                console.log("------------------wait------------------")  
            }

            this.notReady = !this.notReady;
            this.playerStatus = this.playerStatus ? false : true; 
        
            const token = "Bearer " + localStorage.getItem("token");
    
            this.connection = new SockJS("http://localhost:8080/lobby-websocket");
            const stompClient = Stomp.over(this.connection);

            stompClient.connect({ Authorization: token }, 
                (frame) =>
                {
                    stompClient.send("/app/lobby/" + this.urlId + "/status-player", {}, JSON.stringify({status: this.status}));
                }
            );
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
    justify-content: space-between;
    font-size: 24px;
    padding: 1% 1% 1% 1%;
    margin-bottom: 40px;
    border-bottom: 1px rgb(168, 168, 168) solid;
}
.gameInfos span b
{
    color: #184e98;
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
.green
{
    background-color: green;
}
.red {
  background-color: red;
}
.infoMsg
{
    
    display: flex;
    justify-content: center;
    font-size: 24px;
    margin-top: 40px;
}
.waitForPlayer
{
    color: #184e98;
}
.showCountdown
{
    color: #184e98;
}
.showCountdown b
{
    color: green;
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