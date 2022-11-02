<template>
    <div >
        <Headline text="Quizduell"></Headline>
        <div>
            <form id="loginForm" @submit.prevent="onSubmit">
                <div class="formData">
                    <label for="accountName">Accountname</label>
                    <input type="text" name="accountName" v-model="accountName">
                </div>
                <div class="formData">
                    <label for="password">Passwort</label>
                    <input type="text" name="password" v-model="password">
                </div>
                <Button type="submit" text="Anmelden"></Button>
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
        }
    },
    methods:
    {
        async onSubmit()
        {
            await axios.get("http://localhost:8080/auth/token",{
                    auth:
                    {
                        username: this.accountName,
                        password: this.password
                    }
                }).then(resp => {
                    console.log("Erfolgreich eingeloggt!")
                    console.log(resp);
                    localStorage.setItem("token", resp.data)
                    this.$router.push("/MainMenu")
                }).catch((err) => {
                    console.log("Login fehlgeschlagen!")
                    console.log(err)
                })

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
            const account = 
            {
                accountName: this.accountName,
                password: this.password
            }

            this.$emit('auth-account', account);
            console.log('auth-account', account);

            this.accountName = '';
            this.password = "";*/

     
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
#loginForm
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
Button
{
    width: 260px;
    margin: 10% auto auto auto;
    padding: 12px 0 12px 0;
    font-size: 22px;
}
.register
{
    margin: auto;
    padding: 6%;
    display: flex;
    justify-content: center;
}
.register 
{
    color:#184e98;
    background-color: white;
    font-weight: bold;
    align-self: flex-end;
    border: none;
   
}
</style>