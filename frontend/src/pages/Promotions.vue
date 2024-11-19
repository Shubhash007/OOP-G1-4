<template>
    <v-container fluid>

        <v-card class="ma-4 pa-8" elevation="0">
            <PromotionsTable ref="promotionTable"></PromotionsTable>
            <CreatePromotions @refresh="refreshTable"></CreatePromotions>
        </v-card>
    </v-container>
</template>

<script>
import CreatePromotions from '../components/CreatePromotions.vue'
import PromotionsTable from '../components/PromotionsTable.vue'

export default {
    components: {
        CreatePromotions,
        PromotionsTable
    },
    methods: {
        refreshTable() {
        console.log("fresho");
        this.$refs.promotionTable.fetchPromotions() // call fetch method in the table component
        },
    }
    }
</script>