<template>
    <v-container style="position: relative;">
      <v-row class="pa-0 align-center">
        <v-col cols="8" md="8">
          <v-text-field
            v-model="searchQuery"
            variant="outlined"
            label="Search Products"
            prepend-inner-icon="mdi-magnify"
            hide-details
            full-width
          ></v-text-field>
        </v-col>
  
        <v-col cols="4" md="4">
          <v-btn color="primary" block @click="toggleFilters">
            {{ filtersVisible ? 'Hide Filters' : 'Show Filters' }}
          </v-btn>
        </v-col>
      </v-row>
  
      <v-expand-transition>
        <v-card
          v-if="filtersVisible"
          class="pa-6"
          style="position: absolute; top: 80px; z-index: 10; width: 100%;"
        >
          <v-row>
            <v-col cols="12" md="6">
              <v-select
                v-model="selectedSaleType"
                :items="saleTypes"
                label="Sale Type"
                clearable
                variant="outlined"
              ></v-select>
            </v-col>
  
            <v-col cols="12" md="6">
              <v-select
                v-model="selectedDigitalChannel"
                :items="digitalChannels"
                label="Digital Channel"
                clearable
                variant="outlined"
              ></v-select>
            </v-col>
  
            <v-col cols="12" md="6">
              <v-select
                v-model="selectedShippingMethod"
                :items="shippingMethods"
                label="Shipping Method"
                clearable
                variant="outlined"
              ></v-select>
            </v-col>
  
            <v-col cols="12" md="6">
              <v-select
                v-model="selectedProduct"
                :items="products"
                label="Product"
                clearable
                variant="outlined"
              ></v-select>
            </v-col>
  
            <v-col cols="12" md="6">
              <v-select
                v-model="selectedVariant"
                :items="variants"
                label="Variant"
                variant="outlined"
                clearable
              ></v-select>
            </v-col>
  
            <v-col cols="12" md="6">
              <v-select
                v-model="selectedRecencySegment"
                :items="recencySegments"
                label="Recency Segment"
                variant="outlined"
                clearable
              ></v-select>
            </v-col>
  
            <v-col cols="12" md="6">
              <v-select
                v-model="selectedFrequencySegment"
                :items="frequencySegments"
                label="Frequency Segment"
                variant="outlined"
                clearable
              ></v-select>
            </v-col>
  
            <v-col cols="12" md="6">
              <v-select
                v-model="selectedMonetarySegment"
                :items="monetarySegments"
                label="Monetary Segment"
                variant="outlined"
                clearable
              ></v-select>
            </v-col>
  
            <v-col cols="12" md="6">
              <v-slider
                v-model="quantityRange"
                :min="0"
                :max="5000"
                label="Quantity"
                range
                thumb-label
              ></v-slider>
            </v-col>
  
            <v-col cols="12" md="6">
              <v-slider
                v-model="priceRange"
                :min="0"
                :max="1000"
                label="Price Range"
                range
                thumb-label
              ></v-slider>
            </v-col>
          </v-row>
        </v-card>
      </v-expand-transition>
    </v-container>
  </template>
  
  <script>
  export default {
    data() {
      return {
        filtersVisible: false,
        searchQuery: '',
        selectedSaleType: null,
        selectedDigitalChannel: null,
        selectedShippingMethod: null,
        selectedProduct: null,
        selectedVariant: null,
        selectedRecencySegment: null,
        selectedFrequencySegment: null,
        selectedMonetarySegment: null,
        quantityRange: [0, 5000],
        priceRange: [0, 1000],
  
        // Example items for filtering
        saleTypes: ['Consignment', 'Direct - B2B', 'Direct - B2C', 'Marketing', 'Wholesaler'],
        digitalChannels: ['Offline', 'Online - Website', 'Shopee'],
        shippingMethods: ['Same Day Delivery', 'Self Collect', 'Standard Delivery'],
        products: ['1x100', 'Acacia', 'Basil Oil', 'Truffle Oil', 'Spaghetti', 'Gnocchi Sardi'],
        variants: ['30', '50', '100', '250', '500', '750', '1000', '2020', '2021', '5000'],
        recencySegments: ['Active Customers', 'Dormant Customers', 'Returning Customers'],
        frequencySegments: ['Frequent Shoppers', 'Occasional Shoppers', 'One-time Buyers'],
        monetarySegments: ['High-Value Customers', 'Mid-Tier Customers', 'Low-Spend Customers'],
      };
    },
    methods: {
      toggleFilters() {
        this.filtersVisible = !this.filtersVisible;
      },
    },
  };
  </script>
  
  <style scoped>
  .v-card {
    box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
  }
  </style>
  