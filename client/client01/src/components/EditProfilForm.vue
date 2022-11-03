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
                    <label for="profilIcon">Profilbild</label>
                    <input type="file" name="profilIcon" >
                </div>
                <div class="formData">
                    <label for="password">Passwort</label>
                    <input type="password" name="password" v-model="password">
                </div>
                <div class="formData">
                    <label for="passwordRepeat">Passwort wiederholen</label>
                    <input type="password" name="passwordRepeat" v-model="passwordRepeat">
                </div>
                <div class="errMsg" v-html="errMsg"></div>
                <div class="btnWrapper">
                    <router-link to="/main"><Button text="Zurück"></Button></router-link>
                    <Button type="submit" text="Speichern"></Button>               
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
                profilIcon: "", //noch ändern
                password: "",
                passwordRepeat: "",
                errMsg: "",
            }
        },
        
    }
</script>

<style scoped>
.headline
{
    font-size: 50px;
}
.container
{
    width:40%;
    margin: auto;
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
    height: 30px;
    width: 40%;
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
    margin-top: 10%;
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


</style>