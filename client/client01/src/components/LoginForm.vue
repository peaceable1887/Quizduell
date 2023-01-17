<!--   
    Version: 3.2.41
    Auhtor: Felix Hansmann
    
    Die Komponente "LoginForm.vue" definiert wie der Login Seite auszusehen hat und enthält entsprechenden Funktionen.
-->
<template>
    <div class="container">
        <Headline class="headline" text="Quizduell"></Headline>
        <div>
            <form id="loginForm" @submit.prevent="onSubmit">
                <div class="formData">
                    <label for="accountName">Accountname</label>
                    <input type="text" name="accountName" v-model="accountName">
                </div>
                <div class="formData">
                    <label for="password">Passwort</label>
                    <input type="password" name="password" v-model="password">
                </div>
                <div class="errMsg" v-html="errMsg"></div>
                <div class="btnWrapper">
                    <Button type="submit" text="Anmelden"></Button>
                </div>
            </form>
            <div class="register">
                <router-link to="/register">Registrieren</router-link>
            </div>
        </div>   
    </div>
</template>

<script>

import axios from "axios";
import Headline from "./Headline.vue"
import Button from "./Button.vue"

export default 
{
    name: "LoginForm",
    components:
    {
        Headline,
        Button
    },
    data()
    {
        return{
            accountName: "",
            password: "",
            errMsg: "",
        }
    },
    methods:
    {
        /**
        * Die Methode "onSubmit" prüft die Logindaten (Benutzername/Passwort) und leitet in das Hauptmenü weiter, bei korrekten Daten.
        */
        async onSubmit()
        {
            //REST API Endpunkt (Login/ JWT anfordern)
            await axios.get("http://localhost:8080/api/auth/v1/token",
            {
                auth:
                {
                    username: this.accountName,
                    password: this.password
                }
            }).then(resp => 
            {
                localStorage.setItem("token", resp.data.token)
                localStorage.setItem("userId", resp.data.userId)
                this.$router.push("/main")
                
            }).catch((err) => 
            {
                this.errMsg = "Accountname und/oder Passwort falsch!";
                
                if(!this.accountName)
                {
                    this.errMsg = "Der Accountname fehlt!";
                }
                if(!this.password)
                {
                    this.errMsg = "Das Password fehlt!";
                }
                if(!this.accountName && !this.password)
                {
                    this.errMsg = "Accountname und Passwort fehlen!";
                }
                console.log(err)
            })           
        },
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
    display: flex;
    flex-direction: column;
    margin: auto;
}
#loginForm
{
    display: flex;
    flex-direction: column;
    padding: 2%;
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
}
.formData input
{
    height: 30px;
    width: 250px;
}
.errMsg
{
    color: red;
}
.btnWrapper
{
    display: flex;
    justify-content: center;
    margin-top: 80px;
}
Button
{
    width: 260px;
    padding: 12px 0 12px 0;
    font-size: 22px;
}
.register
{
    display: flex;
    justify-content: center;
    padding-top: 30px;
    color:#184e98;
}
@media screen and (max-width:650px) 
{
    .headline{font-size: 60px}
    #loginForm { width: 100%;}
    .formData{font-size: 22px; }
    .formData input{width: 200px; }
    Button
    {
        width: 200px;
        font-size: 20px;
    }
}


</style>