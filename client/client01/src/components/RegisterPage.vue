<template>
   <div>
        <div class="headline">
            <h2>Registrieren</h2>
        </div>
        <div>
            <form id="registerForm" @submit.prevent="registerUser">
                <div class="formData">
                    <label for="accountName">Accountname</label>
                    <input type="text" id="accountName" v-model="userRegData.accountName">
                </div>
                <div class="formData">
                    <label for="eMail">E-Mail</label>
                    <input type="text" id="email" v-model="userRegData.eMail">
                </div>
                <div class="formData">
                    <label for="password">Passwort</label>
                    <input type="text" id="password" v-model="userRegData.password">
                </div>
                <div class="formData">
                    <label for="passwordRepeat">Passwort wiederholen</label>
                    <input type="text" id="passwordRepeat" v-model="userRegData.passwordRepeat">
                </div>
                <div class="btnWrapper">
                    <button>Zurück</button>
                    <button >Registrieren</button>               
                </div>
            </form>
        </div>        
    </div>
</template>

<script>
import axios from "axios";

export default 
{
    name: "RegisterPage",

    data(){
        return{
            userRegData: {
                accountName: "",
                eMail: "",
                password: "",
                passwordRepeat: "",

            }
        }
    },
    methods:
    {
        registerUser()
            {
                axios.post("https://jsonplaceholder.typicode.com/posts", this.userRegData)
                .then((response) => console.log(response))
                .catch((error) => console.log(error))
            }
    }
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
}
#registerForm button
{
    color: white;
    background-color: #184e98;
    font-size: 22px;
    border: none;
    margin: 18% auto auto auto;
    padding: 12px 80px 12px 80px;
}
</style>