<template>
    <v-container fluid >
        <v-card class="ma-4 pa-8" elevation="0">
            <AccountsBar></AccountsBar>
            <AccountsTable></AccountsTable>

        </v-card>
    </v-container>
</template>
    
<script>
import AccountsBar from '@/components/AccountsBar.vue';
import AccountsTable from '@/components/AccountsTable.vue';

    export default {
        methods: {

        },
    };
</script>
    