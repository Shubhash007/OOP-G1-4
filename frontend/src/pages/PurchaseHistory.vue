<template>
    <v-container fluid >
        <!-- <v-btn color="primary" @click="goToPage">
        Go to Home Page
        </v-btn> -->
        <v-card class="d-flex ma-4 px-8 justify-end" elevation="0">
            <v-btn prepend-icon="mdi-download-circle" color="secondary">Export to CSV</v-btn>
        </v-card>
        <v-card class="ma-4 pa-8" elevation="0">
            <PurchaseHistoryBar></PurchaseHistoryBar>
            <Table></Table>
        </v-card>
    </v-container>
</template>
    
<script>
    export default {
        methods: {
            // goToPage() {
            //     this.$router.push('/home');
            // },
        },
    };
</script>
    