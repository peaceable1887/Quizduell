<!--   
    Version: 3.2.41
    Auhtor: Felix Hansmann
    
    Die Komponente "RegisterForm.vue" definiert wie das Formular, für die Registierung, auszusehen hat 
    und welche Funktionen es beinhaltet.
-->
<template>
    <div class="container">
        <Headline class="headline" text="Registrieren"></Headline>
        <div>
            <form id="registerForm" @submit.prevent="onSubmit">
                <div class="formData">
                    <label for="accountName">Accountname*</label>
                    <input type="text" name="accountName" v-model="accountName">
                </div>
                <div class="formData">
                    <label for="eMail">E-Mail</label>
                    <input type="email" name="eMail" v-model="eMail">
                </div>
                <div class="formData">
                    <label for="password">Passwort*</label>
                    <input type="password" name="password" v-model="password">
                </div>
                <div class="formData">
                    <label for="passwordRepeat">Passwort wiederholen*</label>
                    <input type="password" name="passwordRepeat" v-model="passwordRepeat">
                </div>
                <div class="errMsg" v-html="errMsg"></div>
                <div class="btnWrapper">
                    <router-link to="/">
                        <Button text="Zurück"></Button>
                    </router-link>
                    <Button text="Registrieren"></Button>               
                </div>
            </form>
            
        </div>        
    </div>
</template>

<script>

import Headline from "./Headline.vue"
import Button from "./Button.vue"

export default 
{
    name: "RegisterForm",
    components:
    {
        Headline,
        Button
    },
    data()
    {
        return{
            accountName: "",
            eMail: "",
            password: "",
            passwordRepeat: "",
            errMsg: "",
        }
    },
    methods:
    {
        /**
        * Die Methode "onSubmit" nimmt die eingebenen Daten aus dem Forumlar entgegen und sendet sie korrekter Eingabe an den Server.
        */
        async onSubmit()
        {

            //Validierung: prüft ob alle Daten für Registierung korrekt eingegeben wurden
            if(!this.accountName)
            {
                this.errMsg = "Der Accountname fehlt!";
            }
            if(!this.password)
            {
                this.errMsg = "Das Password fehlt!";
            }
            if(this.password !== this.passwordRepeat)
            {
                this.errMsg = "Passwörter ist nicht identisch!";
            } 
            else
            {
                //REST API Endpunkt (Registrieren)
                await fetch("api/auth/v1/register", {
                    method: "POST",
                    headers: 
                    {
                      "Content-Type": "application/json"
                    },
                    body: JSON.stringify
                    ({
                        name: this.accountName,
                        mail: this.eMail,
                        password: this.password
                    })
                })
                .then(res =>
                {
                    if(res.ok)
                    {
                        this.errMsg = "";
                        alert("Account wurde erfolgreich angelegt !");
                        this.$router.push("/");

                    }else
                    {       
                        this.errMsg = "Fehler ist aufgetreten. Account konnte nicht erstellt werden<br><br> Mögliche Ursache:<br><br> - Server nicht erreichbar<br> - Accountname bereits vergeben";
                    }
                })
                .catch(err => console.log("ERROR: " + err)) 
            }
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
#registerForm
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
}
.formData input
{
   height: 30px;
   width: 250px;
}
.errMsg
{
    color: red;
    font-style: bold;
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