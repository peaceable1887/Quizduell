<template>
    <div>
        <Headline text="Registrieren"></Headline>
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
                    <label for="password">Passwort</label>
                    <input type="text" name="password" v-model="password">
                </div>
                <div class="formData">
                    <label for="passwordRepeat">Passwort wiederholen</label>
                    <input type="text" name="passwordRepeat" v-model="passwordRepeat">
                </div>
                <div class="btnWrapper">
                    <router-link to="/"><Button text="Zurück"></Button></router-link>
                    <Button type="submit" text="Registrieren"></Button>               
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
        }
    },
    methods:
    {
        async onSubmit()
        {
            await fetch("http://localhost:8080/auth/register", {
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
                            console.log("Account wurde erfolgreich angelegt !")
                            console.log(res);
                            this.$router.push("/")
                        }else{
                            console.log("Fehler ist aufgetreten. Account konnte nicht erstellt werden" )
                        }
                    }) 
            /*      
            /*if(!this.accountName)
            {
                alert("Der Accountname fehlt!");
                return;
            }
            if(!this.eMail)
            {
                alert("Die E-Mail Adresse fehlt!");
                return;
            }
            if(!this.password)
            {
                alert("Das Password fehlt!");
                return;
            }
            if(this.password !== this.passwordRepeat)
            {
                alert("Passwort ist nicht identisch");
                return;
            }

            const newAccount = 
            {
                id: Math.floor(Math.random() * 100000),
                accountName: this.accountName,
                eMail: this.eMail,
                password: this.password
            }

            this.$emit('new-account', newAccount);
            console.log('new-account', newAccount);
            alert("Account wurde erstellt!");

            this.accountName = '';
            this.eMail = '';
            this.password = "";
            this.passwordRepeat = "";*/

        },
    },
}
</script>

<style scoped>
.headline
{
    display: flex;
    justify-content: center;
    padding: 20%;
}
h2
{
    margin: 0;
    padding: 0;
    color: #184e98;
    font-size: 100px;
}
#registerForm
{
    display: flex;
    justify-content: center;
    flex-direction: column;

}
.formData
{
    display: flex;
    justify-content: space-between;
    color: #184e98;
    font-size: 28px;
    font-weight: bold;
    padding: 15px 0 15px 0;
}
.formData input
{
    margin: 0 0 0 40px;
    width: 300px;
}
.btnWrapper 
{
    display: flex;
    justify-content: space-around;
    margin-top: 10%;
}
Button
{
    width: 260px;
    padding: 12px 0 12px 0;
    font-size: 22px;
}

</style>