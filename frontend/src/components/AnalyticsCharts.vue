<template>
    <v-container>
      <v-row>
        <v-col cols="6">
          <v-card>
            <v-card-title>Total Number of Sales</v-card-title>
            <v-card-text>
              <canvas ref="chartCanvas1"></canvas>
            </v-card-text>
          </v-card>
        </v-col>
        <v-col cols="6">
          <v-card>
            <v-card-title>Total Amount of Sales</v-card-title>
            <v-card-text>
              <canvas ref="chartCanvas2"></canvas>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="6">
          <v-card>
            <v-card-title>Sales by Customer Segment</v-card-title>
            <v-card-text>
              <canvas ref="chartCanvas3"></canvas>
            </v-card-text>
          </v-card>
        </v-col>
        <v-col cols="6">
          <v-card>
            <v-card-title>Sales by Shipping Method</v-card-title>
            <v-card-text>
              <canvas ref="chartCanvas4"></canvas>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </template>
  
  <script>
  import { Chart } from 'chart.js';
  
  export default {
    name: 'BarChartGrid',
    data() {
      return {
        totalNumberOfSales: [1500, 2000, 1000],
        totalAmountOfSales: [10000, 15000, 8000],
      };
    },
    mounted() {
      this.createChart('chartCanvas1', ['Product A', 'Product B', 'Product C'], this.totalNumberOfSales, 'Total Sales');
      this.createChart('chartCanvas2', ['Product A', 'Product B', 'Product C'], this.totalAmountOfSales, 'Total Amount ($)');
      this.createChart('chartCanvas3', ['Frequent', 'Occasional', 'New'], [400, 200, 100], 'Customer Segments');
      this.createChart('chartCanvas4', ['Standard', 'Express', 'Overnight'], [300, 250, 150], 'Shipping Methods');
    },
    methods: {
      createChart(ref, labels, data, label) {
        const ctx = this.$refs[ref].getContext('2d');
        new Chart(ctx, {
          type: 'bar',
          data: {
            labels: labels,
            datasets: [
              {
                label: label,
                backgroundColor: '#42A5F5',
                data: data,
              },
            ],
          },
          options: {
            responsive: true,
            scales: {
              y: {
                beginAtZero: true,
              },
            },
          },
        });
      },
    },
  };
  </script>
  
  <style scoped>
  canvas {
    width: 100%;
    height: 400px;
  }
  </style>
  