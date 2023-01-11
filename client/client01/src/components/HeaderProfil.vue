<template>
    <div class="container">
        <div class="profil">
            <div class="profilInfo">
                <span>Hallo, {{text}}</span>
                <div class="links">
                    <a href="/editProfil">Profil bearbeiten</a>
                    &nbsp;|&nbsp;
                    <a href="javascript:void(0)" @click="handleClick">Abmelden</a>
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
        async created()
        {
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
                console.log(data)
                this.file = data.file;
                console.log(this.file)
            })
        },
        methods:
        {
            handleClick()
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