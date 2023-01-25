<!--   
    Version: 3.2.41
    Auhtor: Felix Hansmann
    
    Die Komponente "EditProfilForm.vue" definiert wie das Formular, zum bearbeiten von dem Spielerprofil, auszusehen hat 
    und welche Funktionen es beinhaltet.
-->
<template>
    <div class="container">
        <Headline class="headline" text="Profil bearbeiten"></Headline>
        <div>
            <form id="registerForm" @submit.prevent="onSubmit">
                <div class="formData">
                    <label for="accountName">Accountname</label>
                    <input type="text" name="accountName" v-model="accountName">
                </div>
                <div class="formData">
                    <label for="eMail">E-Mail</label>
                    <input type="text" name="eMail" v-model="eMail">
                </div>
                <div class="formData">
                    <label for="selectedFile">Profilbild</label>
                    <input type="file" accept="image/jpeg" @change="onFileSelected">
                </div>
                <div class="formData">
                    <label for="password">Passwort*</label>
                    <input type="password" name="password" v-model="password">
                </div>
                <div class="formData">
                    <label for="passwordRepeat">Passwort wiederholen*</label>
                    <input type="password" name="passwordRepeat" v-model="passwordRepeat">
                </div>
                <!-- Meldung: ob das ändern das Profildaten geklappt hat -->
                <div v-if="errMsg" class="errMsg" v-html="errMsg"></div>
                <div v-if="sucMsg" class="sucMsg" v-html="sucMsg"></div>
                <div class="btnWrapper">
                    <router-link to="/main"><Button text="Zurück"></Button></router-link>
                    <Button text="Speichern"></Button>               
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
    name: "EditProfilForm",
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
            selectedFile: null,
            image: "",
            password: "",
            passwordRepeat: "",
            errMsg: "",
            sucMsg: "",
        }
    },
    /**
     * Der Lifecycle Hook "created" stellt alle benötigten REST Api und/oder Websocket Verbinungen her.
     */
    async created()
    {
        await fetch("api/auth/v1/details", {
            method: "GET",
            headers: 
            {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem("token")
            }
        })
        .then(res => res.json())
        .then(data => 
        {
            this.accountName = data.name;
            this.eMail = data.mail;
        })
        .catch(err => console.log("ERROR: " + err))       
    },
    methods:
    {
        /**
        * Die Methode "onSubmit" löscht die bestehende Spiellobby (erst wenn alle Spieler wieder rausgegangen sind).
        */
        async onSubmit()
        {
            //wird ausgeführt wenn die Passwörter identisch sind
            if(this.password === this.passwordRepeat)
            {
                //REST API Endpunkt (User-Details updaten)
                await fetch("api/auth/v1/update", {
                method: "POST",
                headers:
                {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + localStorage.getItem("token")
                },
                body: JSON.stringify
                ({
                    name:  this.accountName, 
                    mail: this.eMail,
                    password: this.password                    
                })
                })
                .then(res => 
                {
                    if(res.ok)
                    {
                        this.errMsg = "";
                        this.sucMsg = "Profildaten wurden erfolgreich geändert!";
                    }else
                    {
                        console.log("Fehler ist aufgetreten.");
                    }
                })
                //Wird ausgeführt wenn eine Datei ausgewählt wurde
                if(this.selectedFile != null)
                {
                    //FormDara Objekt wird erzeugt für das Hochladen eines Bildes
                    const formData = new FormData();
                    formData.append("file", this.selectedFile)

                    //REST API Endpunkt (Profilbild posten)
                    await fetch("api/auth/v1/image",{
                        method: "POST",
                        headers:
                        {
                            "Authorization": "Bearer " + localStorage.getItem("token")
                        }, 
                        body: formData
                    })
                    .then(res => 
                    {
                        if(res.ok)
                        {
                            console.log("Bild wurde hochgeladen");
                        
                        }else
                        {
                            this.errMsg = "Bild konnte nicht Hochgeladen werden.<br><br> Mögliche Ursache:<br><br> - nur das jpeg-Format ist erlaubt <br> - die Datei ist zu groß";
                            this.sucMsg = ""; 
                        }
                    })     
                }      
            }else
            {
                this.errMsg = "Passwort muss identisch sein!";
            }
        },

        /**
        * Über die Methode "onFileSelected" wird das ausgewählte File-Objekt, der Variabale "selectedFile", zugewiesen.
        * Des Weiteren findet eine Validierung statt, ob die Datei größer als 2 Mb ist.
        * 
        * @param event
        * 
        */
        onFileSelected(event)
        {
            this.selectedFile = event.target.files[0]
            
            // in MiB
            const fileSize = event.target.files[0].size / 1024 / 1024; 

            if(fileSize > 2) 
            {
                this.errMsg = "Die Datei ist zu groß! (max. 2 MB)"
            }else 
            {
                this.errMsg = ""
            }
        }
    }
    
}
</script>

<style scoped>
.headline
{
    font-size: 45px;
    padding: 100px 0 100px 0;
    text-align: center;
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
    width: 600px;
}
.formData
{
    display: flex;
    justify-content: space-between;
    color: #184e98;
    font-size: 24px;
    font-weight: bold;
    padding: 15px 15px 15px 15px;
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
.sucMsg
{
    color: green;
    font-style: bold;
}
.btnWrapper 
{
    display: flex;
    justify-content: space-around;
    margin-top: 80px
}
a{
    width: auto;
}
Button
{
    width: 220px;
    padding: 12px 0 12px 0;
    font-size: 22px;
}
@media screen and (max-width:650px) 
{
    #registerForm { width: 100%; }
    .formData{font-size: 22px; }
    .formData input{width: 160px; }
    Button{width: 150px;font-size: 20px;}
}

</style>