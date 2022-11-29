<template>
    <Header></Header>
    <Headline class="headline" text="Mehrspieler"></Headline>   
    <div class="containerLobby">
        <div class="activLobbyWrapper">
            <JoinCreatedGame></JoinCreatedGame>
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
        }
    },
    created()
    {
        const token = "Bearer " + localStorage.getItem("token");
        console.log(token);
        let sub_first_msg_new_lobby = true;
      
        this.connection = new SockJS("http://localhost:8080/lobby-websocket");
        const stompClient = Stomp.over(this.connection);

        console.log("---1---");
        stompClient.onConnect = function (frame) 
        {
            console.log(frame);
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
                        showLobbies(message.body);
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