<template>
    <v-container>
        <v-card style="display: flex; flex-direction: column; height: 400px; overflow-y: auto"   
        class="pa-4"
        >
            <v-card-text class="pa-6">
                <v-card-title class="mb-4">Create a Promotion</v-card-title>
                <!-- Form to create promotion -->
                <v-form>
                    <!-- Promotion Name -->
                    <v-text-field
                        v-model="promotion.promotionName"
                        label="Promotion Name"
                        outlined
                    ></v-text-field>

                    <!-- Promotion Description -->
                    <v-text-field
                        v-model="promotion.promotionDescription"
                        label="Promotion Description"
                        outlined
                    ></v-text-field>

                    <!-- Frequent Shopper -->
                    <v-switch 
                        v-model="promotion.frequentShopperRequired"
                        label="Only Applicable for Frequent Shoppers?"
                        inset
                    ></v-switch>

                    <!-- Promotion Type -->
                    <v-select
                        v-model="promotion.promotionType"
                        :items="promotionTypes"
                        label="Promotion Type"
                        outlined
                    ></v-select>

                    <!-- Valid Until Date -->
                    <v-text-field
                        v-model="promotion.validUntil"
                        label="Valid Until"
                        type="date"
                        outlined
                    ></v-text-field>

                    <!-- Discount Rate -->
                    <v-text-field
                        v-if="promotion.promotionType=='% Discount'"
                        v-model="promotion.discountRate"
                        label="Discount Rate"
                        type="number"
                        outlined
                    ></v-text-field>

                    <!-- Buy Quantity -->
                    <v-text-field
                        v-if="promotion.promotionType=='Get X Free'"
                        v-model="promotion.buyQuantity"
                        label="Buy Quantity"
                        type="number"
                        outlined
                    ></v-text-field>

                    <!-- Free Quantity -->
                    <v-text-field
                        v-if="promotion.promotionType=='GETFREE'"
                        v-model="promotion.freeQuantity"
                        label="Free Quantity"
                        type="number"
                        outlined
                    ></v-text-field>

                    <!-- Main Product Search Bar -->
                    <v-text-field
                        v-if="!promotion.mainProduct"  
                        v-model="mainProductSearch"
                        label="Search Main Product"
                        outlined
                        @input="filterMainProduct"
                        prepend-inner-icon="mdi-magnify"
                    />

                    <!-- Display Selected Main Product -->
                    <div class="mb-4" v-if="promotion.mainProduct">
                        <v-chip
                            v-model="promotion.mainProduct"
                            closable
                            @click:close="removeMainProduct"
                            color="primary"
                        >
                            {{ promotion.mainProduct.productName }}
                        </v-chip>
                    </div>

                    <!-- Main Product Table -->
                    <v-data-table-virtual class="mb-8"
                        v-if="!promotion.mainProduct && filteredMainProducts.length"
                        :items="filteredMainProducts"
                        :headers="headers"
                        item-value="productId"
                        item-key="productId"
                        height="200px"
                        :virtual-scroll="true"
                        dense
                        fixed-header=true
                        :loading="loading"
                    >
                        <template v-slot:item="{ item }">
                            <tr>
                                <td>{{ item.productName }}</td>
                                <td style="text-align: right;">
                                    <v-btn class="mx-8" size="small" @click="selectMainProduct(item)" color="secondary">Select</v-btn>
                                </td>
                            </tr>
                        </template>
                    </v-data-table-virtual>

                    <!-- Related Products Search Bar -->
                    <v-text-field
                        v-if="promotion.promotionType=='Related Products'"
                        v-model="relatedProductsSearch"
                        label="Search Related Products"
                        outlined
                        @input="filterRelatedProducts"
                        prepend-inner-icon="mdi-magnify"
                    />

                    <!-- Display Selected Related Products -->
                    <div class="mb-4" v-if="promotion.promotionType=='Related Products' && promotion.relatedProducts.length" style="display: flex; flex-wrap: wrap; gap: 8px;">
                        <v-chip
                            v-for="(product, index) in promotion.relatedProducts"
                            :key="product.productId"
                            closable
                            @click:close="removeRelatedProduct(index)"
                            color="secondary"
                            style="margin-bottom: 8px;"
                        >
                            {{ product.productName }}
                        </v-chip>
                    </div>

                    <!-- Related Product Table -->
                    <v-data-table-virtual class="mb-8"
                        v-if="promotion.promotionType=='Related Products'"
                        :items="filteredRelatedProducts"
                        :headers="headers"
                        item-value="productId"
                        item-key="productId"
                        height="200px"
                        :virtual-scroll="true"
                        dense
                        fixed-header=true
                        :loading="loading"
                    >
                        <template v-slot:item="{ item }">
                            <tr>
                                <td>{{ item.productName }}</td>
                                <td style="text-align: right;">
                                    <v-btn class="mx-8" size="small" @click="selectRelatedProduct(item)" color="secondary">Add</v-btn>
                                </td>
                            </tr>
                        </template>
                    </v-data-table-virtual>
                </v-form>

                <!-- Display error message if there is one -->
                <v-alert v-if="errorMessage" type="error" dismissible class="mt-4">
                    {{ errorMessage }}
                </v-alert>

                <v-alert v-if="successMessage" type="success" dismissible class="mt-4">
                    {{ successMessage }}
                </v-alert>

                <!-- Submit Button -->
                <v-row class="mt-2">
                    <v-col cols="6"></v-col>
                    <v-col cols="3">
                        <v-btn regular color="error" width="100%" variant="flat" @click="closeForm">
                            Cancel 
                        </v-btn>
                    </v-col>
                    <v-col cols="3">
                        <v-btn regular color="primary" width="100%" variant="flat" @click="createPromotion">
                            Create
                        </v-btn>
                    </v-col>
                </v-row>
            </v-card-text>
        </v-card>
    </v-container>
</template>

<script>
export default {
    name: "CreatePromotions",
    data() {
        return {
            errorMessage: '',
            successMessage: '',
            promotion: {
                promotionName: '',
                promotionDescription: '',
                promotionType: '',
                validUntil: '',
                discountRate: null,
                freeQuantity: null,
                buyQuantity: null,
                mainProduct: null,  // main product is a single product object
                relatedProducts: [], // related products is an array of product objects
                frequentShopperRequired: false,
            },
            promotionTypes: ['% Discount', 'Get X Free', 'Related Products'],
            products: [], // To hold all products for search
            mainProductSearch: '', // For filtering main product
            relatedProductsSearch: '', // For filtering related products
            headers: [
                { title: 'Product Name', key: 'productName' },  // Product Name header
                { title: '', value: 'actions' }  // Empty header for the Select button
            ],
            filteredMainProducts: [],  // Filtered list of main products
            filteredRelatedProducts: []  // Filtered list of related products
        };
    },
    async created() {
        // Fetch the list of products from your API (adjust URL to your backend)
        try {
            const token = localStorage.getItem("jwt_token");  // Retrieve the JWT token from localStorage

            const response = await fetch("http://localhost:8080/products", {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${token}`,  // Include the Authorization header with the token
                    "Content-Type": "application/json"
                }
            });

            if (!response.ok) {
                throw new Error("Failed to fetch products");
            }
            const data = await response.json();
            this.products = JSON.parse(JSON.stringify(data));
            this.filteredMainProducts = this.products;
            this.filteredRelatedProducts = this.products;
            
        } catch (error) {
            console.error("Error fetching products:", error);
            this.showErrorAlert("Failed to load products.");
        }
    },
    methods: {
        closeForm() {
            this.$emit('form-open', false)
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
        clearForm(){
            this.promotion = {
                promotionName: '',
                promotionDescription: '',
                promotionType: '',
                validUntil: '',
                discountRate: null,
                freeQuantity: null,
                buyQuantity: null,
                mainProduct: null,  // Reset the main product object
                relatedProducts: [], // Reset the related products array
                frequentShopperRequired: false, // Reset the boolean flag
                };
        },
        async createPromotion() {
            // Prepare the payload for the POST request
            var promoType = ''
            if (this.promotion.promotionType==='% Discount'){
                promoType = "DISCOUNT"
            }
            else if (this.promotion.promotionType==='Get X Free'){
                promoType = "GETFREE"
            }
            else if (this.promotion.promotionType==='Related Products'){
                promoType = "RELATED"
            }

            const payload = {
                promotionName: this.promotion.promotionName,
                promotionDescription: this.promotion.promotionDescription,
                promotionType: promoType,
                validUntil: this.promotion.validUntil,
                discountRate: this.promotion.discountRate,
                freeQuantity: this.promotion.freeQuantity,
                buyQuantity: this.promotion.buyQuantity,
                mainProductId: this.promotion.mainProduct ? this.promotion.mainProduct.productId : null, // main product id
                relatedProductIds: this.promotion.relatedProducts.map((product) => product.productId), // array of related product ids
                frequentShopperRequired: this.promotion.frequentShopperRequired
            };

            console.log(payload);

            try {
                const token = localStorage.getItem("jwt_token");  // Retrieve the JWT token from localStorage

                // Send POST request to create promotion
                const response = await fetch("http://localhost:8080/sales/promotions", {
                    method: "POST",
                    headers: {
                        Authorization: `Bearer ${token}`,  // Include the Authorization header with the token
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(payload)
                });

                console.log(response, response.status);

                if (response.status==201) {
                    // Success message
                    this.showSuccessAlert("Promotion created successfully!");
                    this.clearForm();
                    this.$emit('refresh-table');
                    this.$emit('form-open', false)
                    return;
                }
                else{  
                    // Handle different types of errors
                    if (response.status === 400) {
                        this.showErrorAlert("Invalid input data.");
                    } else if (response.status >= 500) {
                        this.showErrorAlert("Server error. Please try again later.");
                    } else {
                        this.showErrorAlert("Error creating promotion.");
                    }
                    return;
                }
                
            } catch (error) {
                // Catch network or unexpected errors
                console.error("Error creating promotion:", error);
                this.showErrorAlert("An unexpected error occurred. Please try again.");
            }
        },
        filterMainProduct() {
            // Filter the products based on the search input for the main product
            this.filteredMainProducts = this.products.filter(product => 
                product.productName.toLowerCase().includes(this.mainProductSearch.toLowerCase())
            );
        },
        filterRelatedProducts() {
            // Filter the products based on the search input for related products
            this.filteredRelatedProducts = this.products.filter(product => 
                product.productName.toLowerCase().includes(this.relatedProductsSearch.toLowerCase())
            );
        },
        selectMainProduct(product) {
            // Set the selected product as the main product
            this.promotion.mainProduct = product;
        },
        removeMainProduct() {
            // Remove the selected main product
            this.promotion.mainProduct = null;
        },
        selectRelatedProduct(product) {
            // Add the selected product to related products if not already added
            if (!this.promotion.relatedProducts.some(p => p.productId === product.productId)) {
                this.promotion.relatedProducts.push(product);
            }
        },
        removeRelatedProduct(index) {
            // Remove the product from related products by index
            this.promotion.relatedProducts.splice(index, 1);
        }
    }
};
</script>

