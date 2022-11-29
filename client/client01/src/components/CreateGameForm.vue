<template>
    <div class="container">
        <Headline class="headline" text="Spiel erstellen"></Headline>
        <div>
            <form id="createGameForm" @submit.prevent="onSubmit">
                <div class="formData">
                    <label for="gameName">Name des Spiels</label>
                    <input type="text" name="gameName" v-model="gameName">
                </div>
                <div class="formData">
                    <label for="roundQuantity">Rundenanzahl</label>
                    <input list="roundQuantity" name="roundQuantity">
                    <datalist id="roundQuantity">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                    </datalist>
                </div>
                <div class="formData">
                    <label for="passwordProtected">Passwortgeschützt</label> 
                    <div class="checkboxContainer">
                        <input type="checkbox" name="passwordProtected" v-model="passwordProtected">
                    </div>
                </div>
                <div class="formData">
                    <label for="password">Passwort</label>
                    <input type="password" name="password" v-model="password">
                </div>
                <div class="btnWrapper">
                    <router-link to="/lobby">
                        <Button text="Zurück"></Button>
                    </router-link>
                    <Button type="submit" text="Spiel erstellen"></Button>                 
                </div>
            </form>
        </div>   
    </div>
</template>

<script>
import axios from "axios";
import Headline from "./Headline.vue"
import Button from "./Button.vue"

export default 
{
    name: "CreateGameForm",
    components:
    {
        Headline,
        Button
    }, 
    methods:
    {
        async onSubmit()
        {
            this.$router.push("/gameLobby")
            
           /* await fetch("http://localhost:8080/api/auth/v1/register", {
                method: "POST",
                headers: 
                {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify
                ({
                    name: this.accountName,
                    password: this.password
                })
            }).then(res => {
                    if(res.ok){

                        this.$router.push("/gameLobby")

                    }else{
                        
                        console.log("Fehler ist aufgetreten. Account konnte nicht erstellt werden")
                        this.errMsg = "Fehler ist aufgetreten. Account konnte nicht erstellt werden"
                    }
                }) */
             
        },
    },
}
</script>

<style scoped>
.headline
{
    font-size: 45px;
    padding: 100px 0 100px 0;
}
.container
{
    display: flex;
    flex-direction: column;
    margin: auto;
}
#createGameForm
{
    display: flex;
    justify-content: center;
    flex-direction: column;
    padding: 2%;
    width: 600px;
}
.formData
{
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: #184e98;
    font-size: 24px;
    font-weight: bold;
    padding: 15px 0 15px 0;
}
.formData label
{
    text-align: center;
    font-size: 28px;
}
.formData input
{
   height: 30px;
   width: 250px;
}
.checkboxContainer
{
    width: 254px;
}
input[type=checkbox] 
{ 
    width: 30px;
}
.btnWrapper 
{
    display: flex;
    justify-content: space-around;
    margin-top: 80px;
}
a{
    width: auto;
}
Button
{
    width: 200px;
    padding: 12px 0 12px 0;
    font-size: 22px;
}
@media screen and (max-width:650px) 
{
    #registerForm { width: 100%;}
    .formData{font-size: 22px; }
    .formData input{width: 130px; }
    Button
    {
        width: 150px;
        font-size: 20px;
    }
}

</style>