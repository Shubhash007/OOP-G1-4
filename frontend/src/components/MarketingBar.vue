<template>
    <v-container>
      <v-row>
        <v-col cols="4">
          <v-select
            v-model="selectedRecency"
            :items="recencyOptions"
            label="Recent Purchases"
            variant="outlined"
          ></v-select>
        </v-col>
  
        <v-col cols="4">
          <v-select
            v-model="selectedFrequency"
            :items="frequencyOptions"
            label="Frequency of Purchases"
            variant="outlined"
          ></v-select>
        </v-col>
  
        <v-col cols="4">
          <v-select
            v-model="selectedMonetary"
            :items="monetaryOptions"
            label="Total Spending"
            variant="outlined"
          ></v-select>
        </v-col>
      </v-row>

    </v-container>
  </template>
  
  <script>
  export default {
    data() {
      return {
        // Selected values for each filter
        selectedRecency: null,
        selectedFrequency: null,
        selectedMonetary: null,
  
        // Recency Filter Options
        recencyOptions: [
          'Active Customers (within last 30 days)',
          'Dormant Customers (no purchase in 6 months)',
          'Returning Customers (first purchase in over a year)'
        ],
  
        // Frequency Filter Options
        frequencyOptions: [
          'Frequent Shoppers (more than 10 purchases per month)',
          'Occasional Shoppers (3-5 times per quarter)',
          'One-time Buyers (one purchase ever)'
        ],
  
        // Monetary Segment Filter Options
        monetaryOptions: [
          'High-Value Customers (top 10%)',
          'Mid-Tier Customers (10% - 80%)',
          'Low-Spend Customers (bottom 20%)'
        ],
      };
    },
  };
  </script>
  
  <style scoped>
  /* Optional: Adjust padding/margins for the dropdowns */
  .v-select {
    margin-bottom: 16px;
  }
  </style>
  