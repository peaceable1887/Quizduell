<template>
    <Header></Header>
    <Headline class="headline" text="Mehrspieler"></Headline>  
    <div class="containerLobby">
        <div class="activLobbyWrapper" v-for="lobby in lobbies" :key="lobby">
            <JoinCreatedGame
                :lobbyName="`${lobby.name}`" 
                :playerName="`${lobby.players[0].name}`" 
                :lobbyId="`${lobby.id}`"
                @connect-lobby="connectLobby(lobby.id)">  
            </JoinCreatedGame>
        </div>
        <div class="btnWrapper">
            <router-link to="/main">
                <Button text="Zurück"></Button>
            </router-link>
            <router-link to="/createGame">
                <Button text="Spiel erstellen"></Button>
            </router-link>
        </div>
    </div>
</template>

<script>
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import Header from "../components/Header.vue";
import Headline from "../components/Headline.vue";
import Button from "../components/Button.vue";
import JoinCreatedGame from "../components/JoinCreatedGame.vue";

export default 
{
    name: "MultiplayerLobby",
    components: 
    {
        Header,
        Headline,
        Button,
        JoinCreatedGame,
    },
    data()
    {
        return{
            connection: null,
            lobbies: [],
            lobbyId: "",
            gameName: "",
            userName: "",
            passwordProtected: false,
        }
    },
    created()
    {
        const token = "Bearer " + localStorage.getItem("token");

        this.connection = new SockJS("http://localhost:8080/lobby-websocket");
        const stompClient = Stomp.over(this.connection);

        stompClient.connect({ Authorization: token }, 
            (frame) =>
            {
                console.log("Connected: " + frame);
                
                stompClient.subscribe("/topic/new-lobby", 
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
        
    },
    methods:
    { 
        async connectLobby(lobbyId)
        {
            console.log(lobbyId)

            fetch("http://localhost:8080/api/lobby/v1/connect",
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
                    password: "123"
                })
            })
            .then(res => 
                {
                    if(res.ok)
                    {
                        console.log("Erfolgreich mit der Lobby verbunden!")
                    }else{
                        console.log("Fehler beim verbinden mit der Lobby!")
                    }
                })
            .then(data => console.log(data))
            .catch(error => console.log("ERROR"))       
        },
    }

}
</script>

<style scoped>
    .headline
    {
        font-size: 45px;
        padding: 50px 0 50px 0;
    }
    .containerLobby
    {
        display: flex;
        flex-direction: column;
        margin: 0 15% 0 15%;
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