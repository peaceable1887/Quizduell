<template>
    <Header></Header>
    <Headline class="headline" text="Mehrspieler"></Headline>   
    <div class="containerLobby">
        <div class="activLobbyWrapper">
            <JoinCreatedGame></JoinCreatedGame>
        </div>
        <div class="btnWrapper">
            <form action="/main">
                <Button text="Zurück"></Button>
            </form>
            <form action="/createGame">
                <Button text="Spiel erstellen"></Button>
            </form> 
        </div>
    </div>
    WebSocket Verbinung testen:<button @click="sendMessage('Test Erfolgreich!')">Test</button>
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
        }
    },
    created()
    {
       /*
        console.log("Starte die Verbindung zum WebSocket Server")
        console.log(localStorage.getItem("token"))
        this.connection = new WebSocket("wss://demo.piesocket.com/v3/channel_123?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self")
        //this.connection = new WebSocket("ws://test.burmeister.hamburg/lobby-websocket")

        this.connection.onopen = function(event) 
        {
            console.log(event)
            console.log("Erfolgreich verbunden mit dem WebSocket Server")
        }

        this.connection.onmessage = function(event) 
        {
            console.log("onmessage")
            console.log(event)        
        }*/

        const token = "Bearer " + localStorage.getItem("token");
        console.log(token);
      
        this.connection = new SockJS("http://localhost:8080/lobby-websocket");
        const stompClient = Stomp.over(this.connection);

        console.log("---1---");
        stompClient.onConnect = function (frame) 
        {
            console.log("Verbunden!");
        };

        console.log("---2---");
        stompClient.connect({ Authorization: token }, 
            function (frame) 
            {
                console.log("Connected: " + frame);
                
                stompClient.subscribe("/topic/new-lobby", 
                    function (message) 
                    {
                        let json = JSON.parse(message.body);
                        showLobbies(json);
                    }
                );

                stompClient.subscribe("/topic/test", 
                    function (message) 
                    {
                        showTest(message.body);
                    }
                );

                stompClient.send("/app/test", {}, "test");
            }
        );

        console.log("---3---");
        function showLobbies(lobbyJson)
        {
            if (sub_first_msg_new_lobby) 
            {
                sub_first_msg_new_lobby = false;

                for (let i = 0; i < lobbyJson.length; i++) 
                {
                    console.log(lobbyJson[i]);
                }

            }else
            {
                    
                console.log("---- NEW LOBBY ----");
                console.log(lobbyJson);
            }
        }

    },
    methods:
    {
        /*sendMessage(message)
        {
            console.log("message")
            console.log(this.connection)          
            this.connection.send(message)
        }*/
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
        align-items: center;
        flex-direction: column;
    }
    .btnWrapper
    {
        display: flex;
        justify-content: space-around;
        width: 40%;
    }
    Button
    {
        width: 200px;
        padding: 12px 0 12px 0;
        font-size: 22px;
    }
</style>