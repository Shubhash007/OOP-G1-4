<template>
  <!-- Tabs for switching between sections -->
  <v-tabs v-model="activeTab">
    <v-tab>Templates</v-tab>
    <v-tab>Newsletters</v-tab>
    <v-tab>Logs</v-tab>
  </v-tabs>

  <v-tabs-items v-model="activeTab">
    <!-- Templates Tab -->
    <v-tab-item v-if="activeTab === 0">
      <v-row class="pa-4 align-center">
        <v-col cols="12" md="5">
          <v-text-field variant="outlined" v-model="searchQuery" label="Search template name"
            prepend-inner-icon="mdi-magnify" hide-details full-width></v-text-field>
        </v-col>
        <v-col cols="4" md="2">
          <v-btn color="secondary" class="ml-0" block @click="openEditDialog('Template')" :disabled="!selectedTemplate">
            <v-icon class="mr-2" left>mdi-update</v-icon>
            Preview
          </v-btn>
        </v-col>
        <!-- <v-col cols="4" md="2">
          <v-btn color="disabled" class="ml-0" block @click="deleteTemplate" :disabled="!selectedTemplate">
            <v-icon class="mr-2" left>mdi-delete</v-icon>
            Delete
          </v-btn>
        </v-col> -->
        <v-col cols="4" md="2">
          <v-btn color="secondary" class="ml-0" block @click="openSendDialog('Template')" :disabled="!selectedTemplate">
            <v-icon class="mr-2">mdi-send</v-icon>
            Compose
          </v-btn>
        </v-col>
        <v-col cols="4" md="3">
          <v-btn color="primary" class="ml-0" block @click="openCreateDialog('Template')">
            <v-icon class="mr-2">mdi-plus-box</v-icon>
            Create Template
          </v-btn>
        </v-col>
      </v-row>

      <v-data-table :items="templates" show-select items-per-page="10" select-strategy="single"
        v-model="selectedTemplate">
        <template v-slot:[`item.content`]="{ item }">
          {{ item.content.length > 100 ? item.content.substring(0, 100) + '...' : item.content }}
        </template>
      </v-data-table>
    </v-tab-item>

    <!-- Newsletters Tab -->
    <v-tab-item v-if="activeTab === 1">
      <v-row class="pa-4 align-center">
        <v-col cols="12" md="5">
          <v-text-field variant="outlined" v-model="searchQuery" label="Search newsletter name"
            prepend-inner-icon="mdi-magnify" hide-details full-width></v-text-field>
        </v-col>
        <v-col cols="4" md="2">
          <v-btn color="secondary" class="ml-0" block @click="openEditNewsletterDialog('Newsletter')"
            :disabled="!selectedNewsletter">
            <v-icon class="mr-2" left>mdi-open</v-icon>
            Update
          </v-btn>
        </v-col>
        <v-col cols="4" md="2">
          <v-btn color="disabled" class="ml-0" block @click="deleteNewsletter" :disabled="!selectedNewsletter">
            <v-icon class="mr-2" left>mdi-delete</v-icon>
            Delete
          </v-btn>
        </v-col>
        <v-col cols="4" md="3">
          <v-btn color="primary" class="ml-0" block @click="openCreateNewsletterDialog('Newsletter')">
            <v-icon class="mr-2">mdi-plus-box</v-icon>
            Create Newsletter
          </v-btn>
        </v-col>
      </v-row>

      <v-data-table :items="filteredNewsletters" :headers="newsletterHeaders" item-value="id" show-select
        items-per-page="8" select-strategy="single" v-model="selectedNewsletter"></v-data-table>
    </v-tab-item>

    <!-- Customer Newsletters Tab -->
    <v-tab-item v-if="activeTab === 2">
      <v-row class="pa-4 align-center">
        <v-col cols="12" md="5">
          <v-text-field variant="outlined" v-model="searchQuery" label="Search log" prepend-inner-icon="mdi-magnify"
            hide-details full-width></v-text-field>
        </v-col>
      </v-row>

      <v-data-table :items="filteredCustomerNewsletters" :headers="customerNewsletterHeaders" v-model="selectedTemplate"
        show-select items-per-page="8"></v-data-table>
    </v-tab-item>
  </v-tabs-items>

  <!-- Create/Edit Dialog -->
  <v-dialog v-model="showDialog" max-width="1000px" max-height="800px">
    <v-card style="display: flex; flex-direction: column; height: 100%;">
      <v-card-title>
        <span class="headline">{{ dialogTitle }}</span>
      </v-card-title>

      <v-card-text style="flex-grow: 1; overflow-y: auto;">
        <v-row>
          <!-- Form Section -->
          <v-col cols="6">
            <v-form>
              <v-text-field v-model="currentItem.name" label="Name" required />
              <v-text-field v-model="currentItem.description" label="Description" required />
              <v-textarea v-model="currentItem.content" label="HTML Content" outlined required auto-grow
                style="min-height: 150px;" />
              <v-alert v-if="errorMessage" type="error" class="mt-4" dense>
                {{ errorMessage }}
              </v-alert>
            </v-form>
          </v-col>

          <!-- Preview Section -->
          <v-col cols="6">
            <div style="position: sticky; top: 0; max-height: calc(100vh - 180px); overflow-y: auto;">
              <h3>Preview</h3>
              <div v-html="currentItem.content" style="
                border: 1px solid #ddd;
                padding: 10px;
                background: #f9f9f9;
                min-height: 200px;
              "></div>
            </div>
          </v-col>
        </v-row>
      </v-card-text>

      <!-- Sticky Action Buttons -->
      <v-card-actions style="position: sticky; bottom: 0; background: white; z-index: 10;">
        <v-btn color="primary" @click="saveItem">{{ isEditMode ? 'Save' : 'Create' }}</v-btn>
        <v-spacer></v-spacer>
        <v-btn color="error" @click="showDialog = false">Cancel</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>


  <v-dialog v-model="showNewsletterDialog" max-width="500px">
    <v-card>
      <v-card-title><span class="headline">{{ dialogTitle }}</span></v-card-title>
      <v-card-text>
        <v-form>
          <v-text-field v-model="currentItem.name" label="Name" required />
          <v-text-field v-model="currentItem.description" label="Description" required />
          <v-text-field v-model="currentItem.content" label="Content" required />
          <v-text-field v-model="currentItem.templateId" label="Template" required />
          <v-alert v-if="errorMessage" type="error" class="mt-4" dense>
            {{ errorMessage }}
          </v-alert>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="saveItem">{{ isEditMode ? 'Save' : 'Create' }}</v-btn>
        <v-spacer></v-spacer>
        <v-btn color="error" @click="showNewsletterDialog = false">Cancel</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- send dialoge -->
  <v-dialog v-model="showSendDialog" max-width="1200px">
    <v-card color="white">
      <v-card-title>Customers</v-card-title>

      <v-tabs v-model="activeCustomersTab" background-color="primary" dark>
        <v-tab>Recency</v-tab>
        <v-tab>Frequency</v-tab>
        <v-tab>Monetary</v-tab>
      </v-tabs>

      <v-tabs-items v-model="activeCustomersTab">
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

      <v-data-table :items="filteredItems" item-value="customerId" show-select v-model="selectedIds" items-per-page="5">
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

      <v-card-title>Promotions</v-card-title>
      <v-row class="pa-4 align-center">
        <!-- Search Box -->
        <v-col cols="6">
            <v-text-field
            variant="outlined"
            v-model="searchQuery"
            label="Search promotion name"
            prepend-inner-icon="mdi-magnify"
            hide-details
            full-width
            ></v-text-field>
        </v-col>
        </v-row>

      <!-- Data Table -->
      <v-data-table :items="filteredPromotions" :headers="[
        { title: 'Name', key: 'promotionName' },
        { title: 'Description', key: 'promotionDescription' },
        { title: 'Type', key: 'promotionType' },
        { title: 'Expiry', key: 'validUntil' },
        { title: '', key: 'actions' }
      ]" item-value="promotionId" show-select items-per-page="5" select-strategy="multiple" v-model="selected"
        @update:modelValue="updateSelected">
        <template v-slot:[`item.promotionDescription`]="{ item }">
          <span class="truncate-text">{{ item.promotionDescription }}</span>
        </template>
        <template v-slot:[`item.promotionType`]="{ item }">
          {{ formatPromotionType(item.promotionType) }}
        </template>
      </v-data-table>

    <v-card-actions style="position: sticky; bottom: 0; background: white; z-index: 10;">
      <v-btn color="primary" @click="sendNewsletter" :disabled="selectedIds.length === 0">
        <v-icon class="mr-1">mdi-send</v-icon>
        Send 
      </v-btn>        
      <v-spacer></v-spacer>
      <v-btn color="error" @click="showSendDialog = false">Cancel</v-btn>        
      </v-card-actions>

    </v-card>

  </v-dialog>

</template>

<script>
export default {
  data() {
    return {
      activeTab: 0,
      showDialog: false,
      showSendDialog: false,
      showNewsletterDialog: false,
      dialogTitle: "",
      isEditMode: false,
      currentItem: {},
      errorMessage: "",
      searchQuery: "",
      templates: [],
      newsletters: [],
      customerNewsletters: [],
      selectedTemplate: null,
      selectedNewsletter: null,

      activeCustomersTab: 0,
      selectedOption: null,
      selectedIds: [],
      recencyOptions: ["Active Customers", "Dormant Customers", "Returning Customers"],
      frequencyOptions: ["Frequent Shoppers", "Occasional Shoppers", "One-Time Buyers"],
      monetaryOptions: ["High-Value Customers", "Mid-Tier Customers", "Low-Spend Customers"],
      allData: {},
      filteredItems: [],


      promotions: [],
      selected: [],
      searchQuery: '',
      successMessage: '',
      errorMessage: '',
      dialogVisible: false, // Controls the visibility of the dialog
      selectedPromotion: {}, // Holds the promotion to be displayed in the dialog
    };
  },
  computed: {
    getOptionsForTab() {
      switch (this.activeCustomersTab) {
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
    filteredPromotions() {
      return this.searchQuery
        ? this.promotions.filter((promotion) =>
          promotion.promotionName.toLowerCase().includes(this.searchQuery.toLowerCase())
        )
        : this.promotions;
    },
  },
  watch: {
    activeCustomersTab(newTab) {
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
    openCreateDialog(type) {
      this.isEditMode = false;
      this.dialogTitle = `Create ${type}`;
      this.currentItem = {};
      this.showDialog = true;
    },
    openEditDialog(type) {
      this.isEditMode = true;
      this.dialogTitle = `Edit ${type}`;
      if (type === "Template") {
        this.currentItem = { ...this.selectedTemplate };
      } else if (type === "Newsletter") {
        this.currentItem = { ...this.selectedNewsletter };
      }
      this.showDialog = true;
    },
    openCreateNewsletterDialog(type) {
      this.isEditMode = false;
      this.dialogTitle = `Create ${type}`;
      this.currentItem = {};
      this.showNewsletterDialog = true;
    },
    openEditNewsletterDialog(type) {
      this.isEditMode = true;
      this.dialogTitle = `Edit ${type}`;
      if (type === "Template") {
        this.currentItem = { ...this.selectedTemplate };
      } else if (type === "Newsletter") {
        this.currentItem = { ...this.selectedNewsletter };
      }
      this.showNewsletterDialog = true;
    },
    async saveItem() {
      if (!this.currentItem.name || !this.currentItem.description || !this.currentItem.content) {
        this.errorMessage = "All fields are required.";
        return;
      }
      this.errorMessage = "";
      this.showDialog = false;
    },
    async fetchTemplates() {
      const response = await fetch("http://localhost:8080/newsletters/templates");
      this.templates = await response.json();
    },
    async fetchNewsletters() {
      const response = await fetch("http://localhost:8080/newsletters");
      this.newsletters = await response.json();
    },

    async saveItem() {
      if (!this.currentItem.name || !this.currentItem.description || !this.currentItem.content) {
        this.errorMessage = "All fields are required.";
        return;
      }
      this.errorMessage = "";

      try {
        const jwtToken = localStorage.getItem("jwt_token"); // Assuming the JWT token is stored in localStorage

        const response = this.isEditMode
          ? await fetch(`http://localhost:8080/newsletters/templates/${this.currentItem.id}`, {
            method: "PUT",
            headers: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${jwtToken}`,
            },
            body: JSON.stringify(this.currentItem),
          })
          : await fetch("http://localhost:8080/newsletters/templates", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${jwtToken}`,
            },
            body: JSON.stringify(this.currentItem),
          });

        if (response.ok) {
          const data = await response.json();
          console.log("Saved successfully", data);

          // Refresh the list of templates
          await this.fetchTemplates();
        } else {
          const errorDetails = await response.json();
          this.errorMessage = errorDetails.message || "Failed to save the template.";
        }
      } catch (error) {
        console.error("Error saving template:", error);
        this.errorMessage = "An error occurred while saving the template.";
      }

      this.showDialog = false;
    },
    openSendDialog() {
      this.currentItem = {};
      this.showSendDialog = true;
    },
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

    async fetchPromotions() {
      const token = localStorage.getItem("jwt_token");

      try {
        const response = await fetch("http://localhost:8080/sales/promotions", {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

        if (response.ok) {
          const promotions = await response.json();
          this.promotions = promotions;
          console.log(promotions)
        } else {
          console.error("Failed to fetch promotions.");
        }
      } catch (error) {
        console.error("Error fetching promotions:", error);
      }
    }, showDetails(promotion) {
      this.selectedPromotion = promotion; // Set the selected promotion details
      this.dialogVisible = true; // Show the dialog
    },
    formatPromotionType(type) {
      if (type === "GETFREE") {
        return "Get X Free";
      }
      else if (type === "DISCOUNT") {
        return "Discount %";
      }
      else if (type === "RELATED") {
        return "Related % Discount"
      }
    },

    
    async sendNewsletter() {
    if (!this.selectedTemplate || this.selectedIds.length === 0) {
      this.errorMessage = "Please select a template and at least one recipient.";
      return;
    }

    const payload = {
      newsletterTemplate: this.selectedTemplate[0],
      customers: this.selectedIds,
      promotions: this.selected,
    };

    console.log(JSON.stringify(payload))

    const token = localStorage.getItem("jwt_token");
    if (!token) {
      console.error("JWT token not found.");
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/newsletters/send", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        const result = await response.text();
        console.log(result);
        this.$emit('show-notification', 'Newsletter sent successfully');
        this.showSendDialog = false; 
      } else {
        const errorText = await response.text();
        console.error("Error sending newsletter:", errorText);
        this.errorMessage = errorText || "Failed to send the newsletter.";
      }
    } catch (error) {
      console.error("Error during API call:", error);
      this.errorMessage = "An error occurred while sending the newsletter.";
    }
  },


  },
  mounted() {
    this.fetchTemplates();
    this.fetchNewsletters();
    this.fetchPromotions();
  },
};
</script>

<style scoped>
.v-toolbar-title {
  font-weight: bold;
}
</style>
