<template>
    <div class="container">
        <Headline text="Quizduell"></Headline>
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
            errMsg: "",
        }
    },
    methods:
    {
        async onSubmit()
        {
            if(!this.accountName)
            {
                this.errMsg = "Der Accountname fehlt!";
            }
            if(!this.password)
            {
                this.errMsg = "Das Password fehlt!";
            }else{
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
                    this.$router.push("/main")
                }).catch((err) => {
                    console.log("Login fehlgeschlagen!")
                    console.log(err)
                })
            }
        },
    },
}

</script>

<style scoped>
.container
{
    width:50%;
    margin: auto;
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
    height: 30px;
    width: 40%;

}
.errMsg
{
    color: red;
    font-style: bold;
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
    padding-top: 6%;
    text-align: center;
    color:#184e98;
}


</style>