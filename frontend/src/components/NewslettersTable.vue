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
            <v-text-field
              variant="outlined"
              v-model="searchQuery"
              label="Search template name"
              prepend-inner-icon="mdi-magnify"
              hide-details
              full-width
            ></v-text-field>
          </v-col>
          <v-col cols="4" md="2">
            <v-btn color="secondary" class="ml-0" block @click="openEditDialog('Template')" :disabled="!selectedTemplate">
              <v-icon class="mr-2" left>mdi-update</v-icon>
              Update
            </v-btn>
          </v-col>
          <v-col cols="4" md="2">
            <v-btn color="disabled" class="ml-0" block @click="deleteTemplate" :disabled="!selectedTemplate">
              <v-icon class="mr-2" left>mdi-delete</v-icon>
              Delete
            </v-btn>
          </v-col>
          <v-col cols="4" md="3">
            <v-btn color="primary" class="ml-0" block @click="openCreateDialog('Template')">
              <v-icon class="mr-2">mdi-plus-box</v-icon>
              Create Template
            </v-btn>
          </v-col>
        </v-row>

        <v-data-table
          :items="filteredTemplates"
          :headers="templateHeaders"
          item-value="id"
          show-select
          items-per-page="8"
          select-strategy="single"
          v-model="selectedTemplate"
        ></v-data-table>
      </v-tab-item>

      <!-- Newsletters Tab -->
      <v-tab-item v-if="activeTab === 1">
        <v-row class="pa-4 align-center">
          <v-col cols="12" md="5">
            <v-text-field
              variant="outlined"
              v-model="searchQuery"
              label="Search newsletter name"
              prepend-inner-icon="mdi-magnify"
              hide-details
              full-width
            ></v-text-field>
          </v-col>
          <v-col cols="4" md="2">
            <v-btn color="secondary" class="ml-0" block @click="openEditNewsletterDialog('Newsletter')" :disabled="!selectedNewsletter">
              <v-icon class="mr-2" left>mdi-update</v-icon>
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

        <v-data-table
          :items="filteredNewsletters"
          :headers="newsletterHeaders"
          item-value="id"
          show-select
          items-per-page="8"
          select-strategy="single"
          v-model="selectedNewsletter"
        ></v-data-table>
      </v-tab-item>

      <!-- Customer Newsletters Tab -->
      <v-tab-item v-if="activeTab === 2">
        <v-row class="pa-4 align-center">
          <v-col cols="12" md="5">
            <v-text-field
              variant="outlined"
              v-model="searchQuery"
              label="Search log"
              prepend-inner-icon="mdi-magnify"
              hide-details
              full-width
            ></v-text-field>
          </v-col>
        </v-row>

        <v-data-table
          :items="filteredCustomerNewsletters"
          :headers="customerNewsletterHeaders"
          item-value="id"
          show-select
          items-per-page="8"
        ></v-data-table>
      </v-tab-item>
    </v-tabs-items>

    <!-- Create/Edit Dialog -->
    <v-dialog v-model="showDialog" max-width="500px">
      <v-card>
        <v-card-title><span class="headline">{{ dialogTitle }}</span></v-card-title>
        <v-card-text>
          <v-form>
            <v-text-field v-model="currentItem.name" label="Name" required />
            <v-text-field v-model="currentItem.description" label="Description" required />
            <v-text-field v-model="currentItem.content" label="Content" required />
            <v-alert v-if="errorMessage" type="error" class="mt-4" dense>
              {{ errorMessage }}
            </v-alert>
          </v-form>
        </v-card-text>
        <v-card-actions>
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
  
</template>

<script>
export default {
  data() {
    return {
      activeTab: 0,
      showDialog: false,
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
      templateHeaders: [
        { text: "ID", value: "id" },
        { text: "Name", value: "name" },
        { text: "Description", value: "description" },
        { text: "Content", value: "content" },
        { text: "Created At", value: "createdAt" },
      ],
      newsletterHeaders: [
        { text: "ID", value: "id" },
        { text: "Name", value: "name" },
        { text: "Description", value: "description" },
        { text: "Content", value: "content" },
        { text: "Template ID", value: "templateId" },
        { text: "Created At", value: "createdAt" },
      ],
      customerNewsletterHeaders: [
        { text: "ID", value: "id" },
        { text: "Customer ID", value: "customerId" },
        { text: "Newsletter ID", value: "newsletterId" },
        { text: "Send Success", value: "sendSuccess" },
        { text: "Send Date", value: "sendDate" },
      ],
    };
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

      if (this.isEditMode) {
        // Update logic here
        console.log("Updating item", this.currentItem);
      } else {
        // Create logic here
        console.log("Creating item", this.currentItem);
      }

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
  },
  mounted() {
    this.fetchTemplates();
    this.fetchNewsletters();
  },
};
</script>

<style scoped>
.v-toolbar-title {
  font-weight: bold;
}
</style>
