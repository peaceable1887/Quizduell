<!--   
    Version: 3.2.41
    Auhtor: Felix Hansmann
    
    Die Komponente "HeaderProfil.vue" beinhaltet die Anwenungdslogik und Darstellung, 
    des Profils, und der dazugehörigen Interaktions-Elemente (Profil bearbeiten, Abmelden), im Hauptmenü.
-->
<template>
    <div class="container">
        <div class="profil">
            <div class="profilInfo">
                <span>Hallo, {{text}}</span>
                <div class="links">
                    <a href="/editProfil">Profil bearbeiten</a>
                    &nbsp;|&nbsp;
                    <a href="javascript:void(0)" @click="logout">Abmelden</a>
                </div>
            </div>
            <div class="profilIcon"><img :src="profilIcon"></div>
        </div>
    </div>
</template>

<script>
export default 
{
    name: "HeaderProfil",
    props:
    {
        text: String,
    },
    data()
    {
        return{
            profilIcon: 'http://test.burmeister.hamburg/static/' + localStorage.getItem("userId") + '.jpg',
        }  
    },
    /**
     * Der Lifecycle Hook "created" stellt alle benötigten REST Api und/oder Websocket Verbinungen her.
     */
    async created()
    {
        //REST API Endpunkt (User-Details abrufen)
        await fetch("http://localhost:8080/api/auth/v1/details", {
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
            this.file = data.file;
        })
        .catch(err => console.log("ERROR: " + err))
    },
    methods:
    {
        /**
        * Über die Methode "logout" wird beim Klick auf "Abmelden" ausgelöst und entfernt das JSON Web Token
        * und leitet einen zurück zum Login Bildschrim.
        */
        logout()
        {
            localStorage.removeItem("token");
            this.$router.push("/");
        }
    }
}
</script>

<style scoped>
.container
{
    display: flex;
    justify-content: end;
    color:#184e98;
    width: 100%;
    
}
.profil
{
    display: flex;
    flex-direction: row;
    padding: 1%;
}
.profilInfo
{
    display: flex;
    justify-content: space-around;
    flex-direction: column;
    margin-right: 20px;
}
.profilInfo span
{
    font-weight: bold;
    font-size: 24px;
}
.links
{
    display: flex;
    font-size: 14px;
    font-weight: bold;
    margin-left: 2px;;
}
.profilIcon
{
    width: 60px;
    height: 60px;
    background-color: white;
    border: 1px rgb(168, 168, 168) solid;
}
img
{
    width: 60px;
    height: 60px;
    border: 1px rgb(168, 168, 168) solid;
}
</style>