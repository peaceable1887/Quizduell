<template>
    <div class="container">
        <div class="profil">
            <div class="profilInfo">
                <span>Hallo, {{text}}</span>
                <div class="links">
                    <a href="/editProfil">Profil</a>
                    &nbsp;|&nbsp;
                    <a href="javascript:void(0)" @click="handleClick">Abmelden</a>
                </div>
            </div>
            <div class="profilIcon"></div>
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
        background-color: blueviolet;
    }
</style>