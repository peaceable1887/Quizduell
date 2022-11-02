<template>
    <headline text="Quizduell"></headline>
    <div class="container">
        <Button text="Einzelspiel"></Button>
        <Button text="Mehrspieler"></Button>
        <Button text="Statistik"></Button>
    </div>
</template>

<script>

import Headline from "../components/Headline.vue";
import Button from "../components/Button.vue";
import axios from "axios";

export default 
{
    name: "MainMenu",
    components:
    {
        Headline,
        Button,
    },
  
    async created()
    {
        console.log("token: " + localStorage.getItem("token"))

        await axios.get("http://localhost:8080/auth/",{
                    headers:
                    {
                        Authorization: "Bearer " + localStorage.getItem("token")
                    }
                }).then(resp => {
                    console.log("resp: " + JSON.stringify(resp.data))
                    alert(JSON.stringify(resp.data))
                }).catch((err) => {
                    console.log("Nicht erfolgreich")
                    console.log(err)
                })
    },   
}
</script>

<style scoped>
    
    .container
    {
        display:flex;
        justify-content: center;
        flex-direction: column;
    }
    /*noch ansehen warum das css nicht anspringt*/ 
    Headline
    {
        color: black;
    }
    Button
    {
        width: 260px;
        margin: 20px auto auto auto;
        padding: 12px 0 12px 0;
        font-size: 22px;
    }

</style>