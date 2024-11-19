<template>
  <v-tabs v-model="activeTab" background-color="primary" dark>
    <v-tab>Recency</v-tab>
    <v-tab>Frequency</v-tab>
    <v-tab>Monetary</v-tab>
  </v-tabs>

  <v-tabs-items v-model="activeTab">
    <v-tab-item>
      <v-container>
        <v-row>
          <v-col cols="12">
            <v-select v-model="selectedOption" :items="getOptionsForTab" label="Select Category"
              variant="outlined"></v-select>
          </v-col>
        </v-row>
      </v-container>
    </v-tab-item>
  </v-tabs-items>

  <v-data-table :items="filteredItems" :headers="headers" item-value="customerId" show-select v-model="selectedIds"
    items-per-page="10">
    <template v-slot:item.customerId="{ item }">
      {{ item.customerId }}
    </template>
    <template v-slot:item.zipCode="{ item }">
      <div style="display: flex; align-items: center;">
        <span>
          {{ item.zipCode.length > 15 ? item.zipCode.substring(0, 15) + "..." : item.zipCode }}
        </span>
        <v-tooltip v-if="item.zipCode.length > 15" bottom text="Full Zip Code">
          <template v-slot:activator="{ props }">
            <v-btn v-bind="props" icon small
              style="box-shadow: none; background: none; border: none; margin-left: 8px;">
              <v-icon small color="primary">mdi-information</v-icon>
            </v-btn>
          </template>
          <span>{{ item.zipCode }}</span>
        </v-tooltip>
      </div>
    </template>
    <template v-slot:item.lastPurchaseDate="{ item }">
      {{ formatDate(item.lastPurchaseDate) }}
    </template>
    <template v-slot:item.acceptNewsletter="{ item }">
      <v-chip :color="item.acceptNewsletter ? 'green' : 'red'" dark>
        {{ item.acceptNewsletter ? "Yes" : "No" }}
      </v-chip>
    </template>
    <template v-slot:item.email="{ item }">
      {{ item.email || "N/A" }}
    </template>
    <template v-slot:item.returningCustomer="{ item }">
      <v-chip :color="item.returningCustomer ? 'blue' : 'grey'" dark>
        {{ item.returningCustomer ? "Yes" : "No" }}
      </v-chip>
    </template>
    <template v-slot:item.purchaseCount="{ item }">
      {{ item.purchaseCount || 0 }}
    </template>
    <template v-slot:item.totalExpenditure="{ item }">
      {{ formatCurrency(item.totalExpenditure) }}
    </template>
  </v-data-table>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 0,
      selectedOption: null,
      selectedIds: [],
      recencyOptions: ["Active Customers", "Dormant Customers", "Returning Customers"],
      frequencyOptions: ["Frequent Shoppers", "Occasional Shoppers", "One-Time Buyers"],
      monetaryOptions: ["High-Value Customers", "Mid-Tier Customers", "Low-Spend Customers"],
      allData: {},
      filteredItems: [],

    };
  },
  computed: {
    getOptionsForTab() {
      switch (this.activeTab) {
        case 0:
          return this.recencyOptions;
        case 1:
          return this.frequencyOptions;
        case 2:
          return this.monetaryOptions;
        default:
          return [];
      }
    }, selectedRows() {
      // TODO: Pass this to newsleter function this is list of the DTOs
      return this.filteredItems.filter((item) =>
        this.selectedIds.includes(item.customerId)
      );
    },
  },
  watch: {
    activeTab(newTab) {
      const segmentationType = ["recency", "frequency", "spending"][newTab];
      this.fetchCustomerData(segmentationType).then(() => {
        // Automatically select the first option after data is fetched
        const firstOption = this.getOptionsForTab[0];
        if (firstOption) {
          this.selectedOption = firstOption;
          this.filteredItems = this.processCustomerData(this.allData[firstOption] || []);
        }
      });
    },
    selectedOption(newValue) {
      if (newValue) {
        console.log("Selected option:", newValue);
        console.log("Data for selected option:", this.allData[newValue]);
        this.filteredItems = this.processCustomerData(this.allData[newValue] || []);
      }
    },
  },
  created() {
    // Initialize data for the first tab
    const initialTab = "recency";
    this.fetchCustomerData(initialTab).then(() => {
      const firstOption = this.recencyOptions[0];
      if (firstOption) {
        this.selectedOption = firstOption;
        this.filteredItems = this.processCustomerData(this.allData[firstOption] || []);
      }
    });
  },
  methods: {
    async fetchCustomerData(segmentationType) {
      const endpointMap = {
        recency: "segmentation-recency",
        frequency: "segmentation-frequency",
        spending: "segmentation-spending",
      };
      const endpoint = endpointMap[segmentationType];

      const token = localStorage.getItem("jwt_token");
      if (!token) {
        console.error("JWT token not found.");
        return;
      }

      try {
        const response = await fetch(`http://localhost:8080/customer/${endpoint}`, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

        if (response.ok) {
          const data = await response.json();
          console.log("API response data:", data);
          this.allData = data;
          console.log(data)
        } else {
          console.error("Error fetching data:", await response.text());
        }
      } catch (error) {
        console.error("Error during API call:", error);
      }
    },
    processCustomerData(customers) {
      return customers.map((customer) => ({
        customerId: customer.customerId,
        zipCode: customer.zipCode.join(", "),
        lastPurchaseDate: customer.lastPurchaseDate,
        acceptNewsletter: customer.acceptNewsletter ? "Yes" : "No",
        email: customer.email || "N/A",
        returningCustomer: customer.returningCustomer ? "Yes" : "No",
        purchaseCount: customer.purchaseCount,
        totalExpenditure: customer.totalExpenditure.toFixed(2),
      }));
    },
    formatCurrency(value) {
      return new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "USD",
      }).format(value);
    },
    formatDate(date) {
      if (!date) return "N/A";
      const [year, month, day] = date.split("-");
      return `${day}-${month}-${year}`;
    },
  },
};


</script>