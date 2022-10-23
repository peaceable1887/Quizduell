<template>
    <main >
        <div v-if="!isHidden">
            <div class="headline">
                <h2>Quizduell</h2>
            </div>
            <div>
                <form id="loginForm">
                    <div class="formData">
                        <label for="accountName">Accountname</label>
                        <input type="text" id="accountName">
                    </div>
                    <div class="formData">
                        <label for="password">Passwort</label>
                        <input type="text" id="password">
                    </div>
                    <button >Anmelden</button>
                </form>
                <div class="register">
                    <button @click="activeSite = 'RegisterPage', isHidden = true" id="registerLink">Registrieren</button>
                </div>
            </div>   
        </div>
            <keep-alive>
                <component :is="activeSite"></component>
            </keep-alive>     
    </main>
</template>

<script>
    import RegisterPage from "./RegisterPage.vue";
    import LoadUserData from "./LoadUserData.vue"

    export default 
    {
        name: "LoginPage",
        components: 
        {
            RegisterPage,
            LoadUserData,
        },
        data: ()=>
        (
            {
                activeSite: "",
                isHidden: false,
            }  
        ),
        methods:
        {
            hideLoginPage()
            {
                console.log("versteckt!");
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
#loginForm button
{
    color: white;
    background-color: #184e98;
    font-size: 22px;
    border: none;
    margin: 18% auto auto auto;
    padding: 12px 80px 12px 80px;
}
.register
{
    margin: auto;
    padding: 6%;
    display: flex;
    justify-content: center;
}
.register button
{
    color:#184e98;
    background-color: white;
    font-weight: bold;
    align-self: flex-end;
    border: none;
   
}
</style>