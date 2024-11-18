<template>
  <v-row>
    <v-col cols="12" sm="6" md="2">
      <v-select label="Sale Type" v-model="saleType" variant="outlined" :items="filters" chips></v-select>
    </v-col>
    <v-col cols="12" sm="6" md="4">
      <v-date-input v-model="dateRange" label="Select Date Range" variant="outlined" max-width="368" multiple="range"
        prepend-icon="" prepend-inner-icon="$calendar"></v-date-input>
    </v-col>

    <v-col cols="12" sm="6" md="2">
      <v-text-field v-model="minPrice" variant="outlined" label="Min Price"
        prepend-inner-icon="mdi-currency-usd"></v-text-field>
    </v-col>

    <v-col cols="12" sm="6" md="2">
      <v-text-field v-model="maxPrice" variant="outlined" label="Max Price"
        prepend-inner-icon="mdi-currency-usd"></v-text-field>
    </v-col>
    <v-col cols="auto" class="mt-2">
      <v-btn color="primary" @click="fetchFilteredData">Search</v-btn>
    </v-col>
  </v-row>

  <v-row>
    <v-col cols="6">
      <v-card>
        <v-card-title>Total Number of Sales by Date</v-card-title>
        <v-card-text>
          <canvas ref="chartCanvas1"></canvas>
        </v-card-text>
      </v-card>
    </v-col>
    <v-col cols="6">
      <v-card>
        <v-card-title>Total Amount of Sales by Date</v-card-title>
        <v-card-text>
          <canvas ref="chartCanvas2"></canvas>
        </v-card-text>
      </v-card>
    </v-col>
  </v-row>
  <v-row>
    <v-col cols="6">
      <v-card>
        <v-card-title>Average Order Value (AOV) by Date</v-card-title>
        <v-card-text>
          <canvas ref="chartCanvas3"></canvas>
        </v-card-text>
      </v-card>
    </v-col>
    <v-col cols="6">
      <v-card>
        <v-card-title>Top 8 Product Count Distribution</v-card-title>
        <v-card-text>
          <canvas ref="chartCanvas4"></canvas>
        </v-card-text>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import "chartjs-adapter-date-fns";
import { Chart } from "chart.js/auto";

export default {
  name: "AnalyticsCharts",
  data() {
    return {
      charts: [],
      analyticsData: {
        labels: [], // For x-axis labels
        salesData: [], // Total Number of Sales
        amountData: [], // Total Amount of Sales
        aovData: [], // Average Order Value
      },
      customerId: null,
      saleType: null,
      minPrice: null,
      maxPrice: null,
      dateRange: null,
      filters: ["DIRECT_B2B", "DIRECT_B2C", "CONSIGNMENT", "MARKETING", "WHOLESALER"],
    };
  },
  async mounted() {
    await this.fetchFilteredData(); // Fetch initial data
    this.createCharts(); // Generate initial charts
  },
  methods: {
    async fetchFilteredData() {
      const token = localStorage.getItem("jwt_token");

      let processedStartDate = null;
      let processedEndDate = null;

      if (Array.isArray(this.dateRange) && this.dateRange.length > 0) {
        const start = new Date(this.dateRange[0]);
        const end = new Date(this.dateRange[this.dateRange.length - 1]);

        processedStartDate = `${start.getFullYear()}-${String(
          start.getMonth() + 1
        ).padStart(2, "0")}-${String(start.getDate()).padStart(2, "0")}`;
        processedEndDate = `${end.getFullYear()}-${String(
          end.getMonth() + 1
        ).padStart(2, "0")}-${String(end.getDate()).padStart(2, "0")}`;
      }

      const payload = {
        customerId: this.customerId ? Number(this.customerId) : null,
        saleType: this.saleType ? this.saleType : null,
        minValue: this.minPrice !== null ? Number(this.minPrice) : null,
        maxValue: this.maxPrice !== null ? Number(this.maxPrice) : null,
        startDate: processedStartDate,
        endDate: processedEndDate,
      };

      try {
        const response = await fetch(
          "http://localhost:8080/purchase-history/filter",
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(payload),
          }
        );

        if (response.ok) {
          const filteredData = await response.json();

          this.analyticsData = this.transformDataForCharts(filteredData);

          console.log(this.analyticsData)
          this.destroyCharts(); // Destroy old charts
          this.createCharts(); // Create updated charts
        } else {
          console.error("Failed to fetch filtered data.");
        }
      } catch (error) {
        console.error("Error fetching filtered data:", error);
      }
    },
    transformDataForCharts(data) {
      const totalNumberOfSales = {};
      const totalAmountOfSales = {};
      const labels = [];
      const salesData = [];
      const amountData = [];
      const aovData = [];
      const productCounts = {};

      data.forEach((sale) => {
        const { saleDate, quantity, originalPrice, product } = sale;

        const dateKey = new Date(saleDate).toISOString().split("T")[0];

        totalNumberOfSales[dateKey] =
          (totalNumberOfSales[dateKey] || 0) + quantity;

        totalAmountOfSales[dateKey] =
          (totalAmountOfSales[dateKey] || 0) + quantity * originalPrice;

        if (product && product.productName) {
          productCounts[product.productName] =
            (productCounts[product.productName] || 0) + quantity;
        }
      });

      Object.keys(totalNumberOfSales)
        .concat(Object.keys(totalAmountOfSales))
        .sort()
        .forEach((date) => {
          labels.push(date);
          salesData.push(totalNumberOfSales[date] || 0);
          amountData.push(totalAmountOfSales[date] || 0);

          // Calculate Average Order Value (AOV)
          if (totalNumberOfSales[date]) {
            aovData.push(totalAmountOfSales[date] / totalNumberOfSales[date]);
          } else {
            aovData.push(0);
          }
        });

      return {
        labels,
        salesData,
        amountData,
        aovData,
        productCounts,
      };
    },


    createCharts() {
      const { labels, salesData, amountData, aovData, productCounts } = this.analyticsData;

      const chart1 = this.createChart(
        "chartCanvas1",
        labels,
        salesData,
        "Total Sales by Date",
        "#36A2EB"
      );
      this.charts.push(chart1);

      const chart2 = this.createChart(
        "chartCanvas2",
        labels,
        amountData,
        "Total Amount of Sales by Date",
        "#FF6384"
      );
      this.charts.push(chart2);

      const chart3 = this.createChart(
        "chartCanvas3",
        labels,
        aovData,
        "Average Order Value (AOV) by Date",
        "#4BC0C0"
      );
      this.charts.push(chart3);

      const sortedProducts = Object.entries(productCounts)
        .sort(([, qtyA], [, qtyB]) => qtyB - qtyA) // Sort by quantity descending
        .slice(0, 8); //TODO:  Take top 5 products

      const productNames = sortedProducts.map(([productName]) => productName);
      const productQuantities = sortedProducts.map(([, quantity]) => quantity);

      const chart4 = this.createPieChart(
        "chartCanvas4",
        productNames,
        productQuantities,
        "Product Distribution"
      );
      this.charts.push(chart4);
    },

    createChart(refName, labels, data, label, borderColor) {
      const ctx = this.$refs[refName].getContext("2d");
      const canvas = this.$refs[refName];
  canvas.style.height = "250px"; // Fixed height (adjust as needed)
  canvas.style.width = "100%"; // Maintain width responsiveness
      return new Chart(ctx, {
        type: "line",
        data: {
          labels: labels,
          datasets: [
            {
              label: label,
              borderColor: borderColor,
              data: data,
              fill: false,
              tension: 0,
              spanGaps: true,
            },
          ],
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              display: false,
            },
          },
          scales: {
            x: {
              type: "time",
              time: {
                parser: "yyyy-MM-dd",
                tooltipFormat: "MMM dd, yyyy",
                displayFormats: {
                  day: "MMM dd",
                },
              },
            },
            y: {
              beginAtZero: true,
              title: {
                display: true,
                text: label,
              },
            },
          },
        },
      });
    },

    createPieChart(refName, labels, data, label) {
      const canvas = this.$refs[refName];
  canvas.style.height = "250px"; // Fixed height (adjust as needed)
  canvas.style.width = "100%"; // Maintain width responsiveness
      const ctx = canvas.getContext("2d");

      return new Chart(ctx, {
        type: "pie",
        data: {
          labels: labels, // Product names
          datasets: [
            {
              label: label,
              data: data,
              backgroundColor: this.generateColors(data.length),
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,

          plugins: {
            legend: {
              display: true,
              position: "right",
            },
            tooltip: {
              callbacks: {
                label: (tooltipItem) => {
                  const value = data[tooltipItem.dataIndex];
                  const total = data.reduce((a, b) => a + b, 0);
                  const percentage = ((value / total) * 100).toFixed(2) + "%";
                  return `${labels[tooltipItem.dataIndex]}: ${percentage}`;
                },
              },
            },
          },
          animation: {
            animateRotate: true,
            animateScale: true,
          },
        },
        plugins: [
          {
            id: "customLabels",
            afterDraw(chart) {
              const { width } = chart;
              const ctx = chart.ctx;
              const dataset = chart.data.datasets[0];
              const meta = chart.getDatasetMeta(0);

              ctx.save();
              dataset.data.forEach((value, index) => {
                const total = dataset.data.reduce((a, b) => a + b, 0);
                const percentage = ((value / total) * 100).toFixed(1);
                const productName = chart.data.labels[index];

                const { x, y } = meta.data[index].tooltipPosition();

                ctx.font = `${Math.round(width / 30)}px Arial`;
                ctx.fillStyle = "#fff"; // Text color
                ctx.textAlign = "center";
                ctx.textBaseline = "middle";

                // // Draw percentage inside the slice
                // ctx.fillText(productName, x, y - 10); // Offset by -10 for percentage
                // ctx.fillText(`${percentage}%`, x, y + 10); // Offset by +10 for spacing

              });
              ctx.restore();
            },
          },
        ],
      });
    },


    generateColors(length) {
      const colors = [
        "#4A90E2", // Strong Blue
        "#50E3C2", // Aqua
        "#B8E986", // Lime Green
        "#7B92FF", // Soft Blue
        "#3E64FF", // Royal Blue
        "#1C8FD0", // Vibrant Blue
        "#A4D4FF", // Pale Sky
        "#194D89", // Navy
      ];

      return Array.from({ length }, (_, i) => colors[i % colors.length]);
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
