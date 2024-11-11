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
import { Chart } from 'chart.js/auto';

export default {
  name: 'AnalyticsCharts',
  data() {
    return {
      totalNumberOfSales: [1500, 2000, 1000],
      totalAmountOfSales: [10000, 15000, 8000],
      charts: [],
    };
  },
  mounted() {
    this.createCharts();
  },
  beforeDestroy() {
    this.destroyCharts();
  },
  methods: {
    createCharts() {
      const chart1 = this.createChart(
        'chartCanvas1',
        ['Product A', 'Product B', 'Product C'],
        this.totalNumberOfSales,
        'Total Sales',
        ['#FF6384', '#36A2EB', '#FFCE56'] // Multiple colors for each bar
      );
      this.charts.push(chart1);

      const chart2 = this.createChart(
        'chartCanvas2',
        ['Product A', 'Product B', 'Product C'],
        this.totalAmountOfSales,
        'Total Amount ($)',
        ['#FF9F40', '#4BC0C0', '#9966FF']
      );
      this.charts.push(chart2);

      const chart3 = this.createChart(
        'chartCanvas3',
        ['Frequent', 'Occasional', 'New'],
        [400, 200, 100],
        'Customer Segments',
        ['#FF6384', '#36A2EB', '#FFCE56'],
        'pie' // Use pie chart for variety
      );
      this.charts.push(chart3);

      const chart4 = this.createChart(
        'chartCanvas4',
        ['Standard', 'Express', 'Overnight'],
        [300, 250, 150],
        'Shipping Methods',
        ['#FF9F40', '#4BC0C0', '#9966FF'],
        'doughnut' // Use doughnut chart for more variety
      );
      this.charts.push(chart4);
    },
    createChart(refName, labels, data, label, backgroundColors, chartType = 'bar') {
      const ctx = this.$refs[refName].getContext('2d');
      return new Chart(ctx, {
        type: chartType, // Default type is 'bar', but can be overridden
        data: {
          labels: labels,
          datasets: [
            {
              label: label,
              backgroundColor: backgroundColors, // Use different colors for each dataset
              data: data,
            },
          ],
        },
        options: {
          responsive: true,
          scales: chartType === 'bar' ? { // Only apply scales for bar charts
            y: {
              beginAtZero: true,
            },
          } : {},
        },
      });
    },
    destroyCharts() {
      this.charts.forEach((chart) => {
        chart.destroy();
      });
      this.charts = [];
    },
  },
};
</script>

<style scoped>
canvas {
  width: 100% !important;
}
</style>
