<template>
    <RegisterForm @new-account="addAccount"></RegisterForm>
</template>

<script>
import RegisterForm from "../components/RegisterForm.vue";

export default 
{
    name: "Register",
    components:
    {
        RegisterForm,
    },
    data()
    {
        return{
            accounts:[],
        }
    },
    methods:
    {
        async addAccount(account) 
        {
            const res = await fetch('api/accounts', 
            {
                method: 'POST',
                headers: 
                {
                    'Content-type': 'application/json',
                },
                body: JSON.stringify(account),
            })
            const data = await res.json()
            this.accounts = [...this.accounts, data]
        },

        async deleteAccount(id) 
        {
            if (confirm('Sin Sie sicher den Account zu löschen?')) 
            {
                const res = await fetch(`api/accounts/${id}`, 
                {
                    method: 'DELETE',
                })
                res.status === 200 ? (this.accounts = this.accounts.filter((account) => account.id !== id)): alert('Error deleting task')
            }
        },

        async fetchAccounts() 
        {
            const res = await fetch('api/accounts')
            const data = await res.json()
            return data
        },

        async fetchAccount(id) 
        {
            const res = await fetch(`api/accounts/${id}`)
            const data = await res.json()
            return data
        },

        async created() 
        {
            this.accounts = await this.fetchAccounts()
        },
    }
}
</script>

<style scoped>

</style>