<template>
    <v-row class="pa-4 align-center">
        <!-- Search Box -->
        <v-col cols="12" sm="10">
            <v-text-field
            variant="outlined"
            v-model="searchQuery"
            label="Search promotion name"
            prepend-inner-icon="mdi-magnify"
            hide-details
            full-width
            ></v-text-field>
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
            { title: 'Promotion Name', key: 'promotionName'},
            { title: 'Description', key: 'promotionDescription'},
            { title: 'Type', key: 'promotionType'},
            { title: ''}
            ]"
        item-value="promotionId"
        show-select
        items-per-page="8"
        select-strategy="multiple"
        v-model="selected"
        @update:modelValue="updateSelected"
        >
        </v-data-table>
    </template>
    
    <script>
    export default {
        name: "PromotionsTable",
        data() {
        return {
            promotions: [],
            selected: [],
            searchQuery: '',
            successMessage: '',
            errorMessage: '',
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
                    const response = await fetch(`http://localhost:8080/sales/promotions`, {
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

