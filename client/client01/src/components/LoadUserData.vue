<template>    
    <div>
        <button @click="getUserDatas">Lade User Daten</button>
        <h3 v-if="errorMsg">{{errorMsg}}</h3>
        <div v-for="user in users" :key="user.accountName">
            <h3>{{user.accountName}}. {{user.eMail}}</h3>
            <p>{{user.password}}</p>
            <hr>
        </div>
    </div>    
</template>

<script>
import axios from "axios";

export default {
    name: "LoadUserData",

    data(){
        return{
            users: [],
            errorMsg:"",
        }
    },
    methods:{
        getUserDatas(){
            axios.get("https://jsonplaceholder.typicode.com/posts")
            .then((response) =>{
                console.log(response.data)
                this.users = response.data
            })
            .catch((error) =>{
                console.log(error)
                this.errorMsg = "Fehler ist aufgetaucht"
            })
        }
    }
}
</script>

<style scoped>

</style>