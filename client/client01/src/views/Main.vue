<!--   
    Version: 3.2.41
    Auhtor: Felix Hansmann
    
    Die Komponente "Main.vue" ist für Darstellung des Hauptmenü zuständig .
-->
<template>
    <!-- Profil (Bild, Profil bearbeiten, Abmelden) -->
    <HeaderProfil v-if="token" :text="`${this.name}`"></HeaderProfil>
    <HeaderProfil v-if="!token" text="Nicht eingeloggt!"></HeaderProfil>
    <div class="container">
        <!-- Headline Komponente (Überschrift) -->
        <Headline class="headline" text="Quizduell"></Headline>
        <!-- Menü -->
        <div class="menu">
            <form action="/lobby">
                <Button text="Mehrspieler"></Button>
            </form>
            <form action="/statistics">
                <Button text="Meine Statistik"></Button>  
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
            userId: "",
            name: "",
            eMail: "",
        }
    },
    components:
    {
        HeaderProfil,
        Headline,
        Button,
    },
    /**
     * Der Lifecycle Hook "created" stellt alle benötigten REST Api und/oder Websocket Verbinungen her.
     */
    async created()
    {
        console.log("token: " + localStorage.getItem("token"))
        //nochmal überarbeiten und auf fetch wechseln !!!!
        await axios.get("http://localhost:8080/api/auth/v1/",
        {
            headers:
            {
                Authorization: "Bearer " + localStorage.getItem("token")
            }
        }).then(resp => 
        {
            console.log("resp: " + JSON.stringify(resp.data))
            this.token = JSON.stringify(resp.data);

        }).catch((err) => 
        {
            console.log("Nicht erfolgreich")
            console.log(err)
        })
        
        await axios.get("http://localhost:8080/api/auth/v1/details",
        {
            headers:
            {
                Authorization: "Bearer " + localStorage.getItem("token")
            }
        }).then(resp => 
        {
    
            let removeChars = JSON.stringify(resp.data.name);
            removeChars = removeChars.split('"').join('');      
            this.name = removeChars;          
            localStorage.setItem("userName", this.name);

        }).catch((err) => 
        {
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