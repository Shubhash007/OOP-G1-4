<template>
    <v-row class="pa-4 align-center">
        <!-- Search Box -->
        <v-col cols="12" sm="8">
            <v-text-field
            variant="outlined"
            v-model="searchQuery"
            label="Search promotion name"
            prepend-inner-icon="mdi-magnify"
            hide-details
            full-width
            ></v-text-field>
        </v-col>

        <v-col cols="4" md="2">
            <v-btn color="secondary" class="ml-0" block @click="showCreateDialog">
                <v-icon class="mr-2">mdi-plus</v-icon>
                Create
            </v-btn>
        </v-col>
    
        <!-- Delete Button -->
        <v-col cols="4" sm="2">
            <v-btn
            color="disabled"
            class="ml-0"
            block
            @click="deletePromotion"
            :disabled="isDeleteDisabled"
            >
            <v-icon class="mr-2" left>mdi-delete</v-icon>
            Delete
            </v-btn>
        </v-col>

        </v-row>
    
        <!-- Success or Error Messages -->
        <v-alert v-if="successMessage" type="success" class="mt-4" dense>
        {{ successMessage }}
        </v-alert>
        <v-alert v-if="errorMessage" type="error" class="mt-4" dense>
        {{ errorMessage }}
        </v-alert>
    
        <!-- Data Table -->
        <v-data-table
        :items="filteredPromotions"
        :headers="[
            { title: 'Name', key: 'promotionName'},
            { title: 'Description', key: 'promotionDescription'},
            { title: 'Type', key: 'promotionType'},
            { title: 'Expiry', key: 'validUntil'},
            { title: '', key: 'actions'}
            ]"
        item-value="promotionId"
        show-select
        items-per-page="8"
        select-strategy="multiple"
        v-model="selected"
        @update:modelValue="updateSelected"
        >
        <template v-slot:[`item.promotionDescription`]="{ item }">
            <span class="truncate-text">{{ item.promotionDescription }}</span>
        </template>
        <template v-slot:[`item.promotionType`]="{ item }">
            {{ formatPromotionType(item.promotionType) }}
        </template>
        <template v-slot:[`item.actions`]="{ item }">
        <v-btn icon size="x-small" @click="showDetails(item)">
            <v-icon size="large">mdi-eye</v-icon>
            </v-btn>
        </template>
        </v-data-table>

        <v-dialog v-model="createDialogVisible" max-width="700px" max-height="500px">
            <CreatePromotions @form-open="closeCreateDialog" @refresh-table="refreshTable"></CreatePromotions>
        </v-dialog>

        <v-dialog v-model="dialogVisible" max-width="600px">
            <v-card>
            <v-card-title class="mx-2 d-flex justify-space-between align-center">
                <div class="text-h5">
                    <strong>{{ selectedPromotion.promotionName }}</strong>
                </div>

                <v-btn
                    icon="mdi-close"
                    variant="text"
                    @click="dialogVisible = false"
                ></v-btn>
            </v-card-title>
            <v-card-subtitle class="mb-4 mx-2">
                {{ formatPromotionType(selectedPromotion.promotionType) }}
            </v-card-subtitle>
            <v-divider></v-divider>
            <v-card-text>
                <v-container>
                <v-row>
                    <v-col cols="12">
                    <p><strong>Description:</strong> {{ selectedPromotion.promotionDescription }}</p>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col cols="12" md="6">
                    <p><strong>Valid Until:</strong> {{ selectedPromotion.validUntil }}</p>
                    </v-col>
                    <v-col cols="12" md="6">
                    <p><strong>Discount Rate:</strong> {{ selectedPromotion.discountRate || 'N/A' }}</p>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col cols="12" md="6">
                    <p><strong>Buy Quantity:</strong> {{ selectedPromotion.buyQuantity || 'N/A' }}</p>
                    </v-col>
                    <v-col cols="12" md="6">
                    <p><strong>Free Quantity:</strong> {{ selectedPromotion.freeQuantity || 'N/A' }}</p>
                    </v-col>
                </v-row>
                </v-container>
            </v-card-text>
            </v-card>
        </v-dialog>
</template>
    
    <script>
import CreatePromotions from './CreatePromotions.vue';

    export default {
        name: "PromotionsTable",
        components:{
            CreatePromotions
        },
        data() {
        return {
            promotions: [],
            selected: [],
            searchQuery: '',
            successMessage: '',
            errorMessage: '',
            createDialogVisible: false,
            dialogVisible: false, // Controls the visibility of the dialog
            selectedPromotion: {}, // Holds the promotion to be displayed in the dialog
        };
        },
        computed: {
        filteredPromotions() {
            return this.searchQuery
            ? this.promotions.filter((promotion) =>
                promotion.promotionName.toLowerCase().includes(this.searchQuery.toLowerCase())
                )
            : this.promotions;
        },
        isDeleteDisabled() {
            return this.selected.length == 0;
        },
        },
        async created() {
        await this.fetchPromotions();
        },
        methods: {
        refreshTable(data){
            this.fetchPromotions();
            this.createDialogVisible = data;
        },
        closeCreateDialog(data){
            this.createDialogVisible = data;
        },
        showCreateDialog(){
            this.createDialogVisible = true;
        },
        showDetails(promotion) {
            this.selectedPromotion = promotion; // Set the selected promotion details
            this.dialogVisible = true; // Show the dialog
        },
        formatPromotionType(type) {
            if (type === "GETFREE") {
                return "Get X Free";
            }
            else if (type === "DISCOUNT"){
                return "Discount %";
            }
            else if (type === "RELATED"){
                return "Related % Discount"
            }
        },
        showSuccessAlert(message) {
            this.successMessage = message;
            // Set timeout to hide the alert after 3 seconds (3000 ms)
            setTimeout(() => {
            this.successMessage = '';
            }, 3000);
        },
        showErrorAlert(message) {
            this.errorMessage = message;
            // Set timeout to hide the alert after 3 seconds (3000 ms)
            setTimeout(() => {
            this.errorMessage = '';
            }, 3000);
        },
        updateSelected(newSelection) {
            console.log(newSelection)
            this.selected = newSelection
            },
        async fetchPromotions() {
            const token = localStorage.getItem("jwt_token");
    
            try {
            const response = await fetch("http://localhost:8080/promotions/get-all", {
                method: "GET",
                headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
                },
            });
    
            if (response.ok) {
                const promotions = await response.json();
                this.promotions = promotions;
            } else {
                console.error("Failed to fetch promotions.");
            }
            } catch (error) {
            console.error("Error fetching promotions:", error);
            }
        },
        
        async deletePromotion() {
            const token = localStorage.getItem("jwt_token");
    
            if (this.selected){
                const confirmDelete = confirm("Are you sure you want to delete the selected promotions?");

                if (confirmDelete) {
                try {
                    const response = await fetch(`http://localhost:8080/promotions/delete`, {
                    method: "DELETE",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(this.selected),
                    });
                    console.log(this.selected, "yes go thru")
        
                    if (response.ok) {
                    this.showSuccessAlert("Promotion deleted successfully!");
                    this.errorMessage = '';
                    this.selected = [];
                    await this.fetchPromotions();
                    } else {
                    const errorData = await response.json();
                    this.showErrorAlert(errorData.message || "Error deleting promotion.");
                    }
                } catch (error) {
                    this.showErrorAlert("An unexpected error occurred. Please try again.");
                }
                }
            }
            else{
                this.showErrorAlert("No promotion selected.");
            }
        },
        },
    };
    </script>

<style scoped>
.truncate-text {
  white-space: nowrap;       /* Ensures the text stays on one line */
  overflow: hidden;          /* Hides any overflow text */
  text-overflow: ellipsis;   /* Adds the ellipsis to hidden text */
  display: block;            /* Required for some browsers */
  max-width: 200px;          /* Adjust this width as needed */
}
</style>

