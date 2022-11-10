<template>
    <HeaderProfil text="Hallo, Felix"></HeaderProfil>
    <div class="container">
        <span v-if="token">{{token}}</span>
        <span v-if="!token">You are not logged in!</span>
        <Headline class="headline" text="Quizduell"></Headline>
        <div class="menu">
            <form action="/game">
                <Button text="Spiel starten"></Button>
            </form>
            <form action="">
                <Button text="Mehrspieler"></Button>
            </form>
            <form action="/statistics">
                <Button text="Statistik"></Button>  
            </form>            
        </div>
    </div>
</template>

<script>

import HeaderProfil from "../components/HeaderProfil.vue";
import Headline from "../components/Headline.vue";
import Button from "../components/Button.vue";
import axios from "axios";

export default 
{
    name: "MainMenu",
    data()
    {
        return{
            token: null,
        }
    },
    components:
    {
        HeaderProfil,
        Headline,
        Button,
    },
  
    async created()
    {
        console.log("token: " + localStorage.getItem("token"))

        await axios.get("http://localhost:8080/api/auth/v1/",
        {
            headers:
            {
                Authorization: "Bearer " + localStorage.getItem("token")
            }
        }).then(resp => {
            console.log("resp: " + JSON.stringify(resp.data))
            this.token = JSON.stringify(resp.data);

        }).catch((err) => {
            console.log("Nicht erfolgreich")
            console.log(err)
        })
    },   
}
</script>

<style scoped>
    .headline
    {
        font-size: 80px;
        padding: 100px 0 100px 0;
    }
    .container
    {
        margin: auto;
    }
    .menu
    {
        display:flex;
        flex-direction: column;
    }
    form
    {
        display:flex;
    }
    Button
    {
        width: 280px;
        margin: 30px auto auto auto;
        padding: 12px 0 12px 0;
        font-size: 22px;
    }
    @media screen and (max-width:650px) 
    {
        .headline{font-size: 60px}
        #loginForm { width: 100%;}
        .formData{font-size: 22px; }
        .formData input{width: 200px; }
        Button
        {
            width: 250px;
            font-size: 20px;
        }
    }

</style>