<template>
    <div>
        <div class="headline">
            <h2>Registrieren</h2>
        </div>
        <div>
            <form id="registerForm" @submit="onSubmit">
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
                    <button><router-link to="/">Zurück</router-link></button>
                    <button><input type="submit" value="Registrieren"></button>               
                </div>
            </form>
        </div>        
    </div>
</template>

<script>
export default 
{
    name: "RegisterForm",
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
        onSubmit(e)
        {
            e.preventDefault();

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
            }*/

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
            this.passwordRepeat = "";
     
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