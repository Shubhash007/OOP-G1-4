<template>
    <v-container fluid>

        <v-card class="ma-4 pa-8" elevation="0">
            <AnalyticsBar></AnalyticsBar>
            <AnalyticsCharts></AnalyticsCharts>
        </v-card>
    </v-container>
</template>

<script>
import AnalyticsCharts from '@/components/AnalyticsCharts.vue';

export default {
    methods: {

    },
};
</script>