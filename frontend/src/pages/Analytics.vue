<template>
    <v-container fluid>

        <v-card class="ma-4 pa-8" elevation="0">
            <AnalyticsBar></AnalyticsBar>
            <AnalyticsCharts></AnalyticsCharts>
        </v-card>
    </v-container>
</template>

<script>
import AnalyticsBar from '@/components/AnalyticsBar.vue';
import AnalyticsCharts from '@/components/AnalyticsCharts.vue';

export default {
  components: {
    AnalyticsBar,
    AnalyticsCharts,
  },
};
</script>