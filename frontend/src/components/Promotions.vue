<template>
    <v-container>
        <v-card max-width="80%">
        <v-card-text class="pa-6">
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
                v-model="promotion.discountRate"
                label="Discount Rate"
                type="number"
                outlined
            ></v-text-field>

            <!-- Free Quantity -->
            <v-text-field
                v-model="promotion.freeQuantity"
                label="Free Quantity"
                type="number"
                outlined
            ></v-text-field>

            <!-- Main Product Search and Selection -->
            <v-text-field
                v-model="searchQuery"
                label="Search Main Product"
                outlined
                @focus="mainProductSearchActive = true"
                @blur="handleBlurMainProduct"
                @input="filterProducts"
                :dense="true"
            ></v-text-field>

            <!-- Dropdown for Main Product -->
            <v-list v-if="mainProductSearchActive && filteredProducts.length > 0">
                <v-list-item
                v-for="product in filteredProducts"
                :key="product.productId"
                @click="selectMainProduct(product)"
                >
                <v-list-item-content>
                    <v-list-item-title>{{ product.productName }}</v-list-item-title>
                </v-list-item-content>
                </v-list-item>
            </v-list>

            <!-- Related Products Search and Selection -->
            <v-text-field
                v-model="relatedProductsText"
                label="Search Related Products"
                outlined
                @focus="relatedProductSearchActive = true"
                @blur="handleBlurRelatedProduct"
                @input="filterProducts"
                :dense="true"
            ></v-text-field>

            <!-- Dropdown for Related Products -->
            <v-list v-if="relatedProductSearchActive && filteredProducts.length > 0">
                <v-list-item
                v-for="product in filteredProducts"
                :key="product.productId"
                @click="toggleRelatedProduct(product)"
                :class="{ 'v-list-item--active': isProductRelated(product) }"
                >
                <v-list-item-content>
                    <v-list-item-title>{{ product.productName }}</v-list-item-title>
                </v-list-item-content>
                </v-list-item>
            </v-list>
            </v-form>

            <!-- Display error message if there is one -->
            <v-alert v-if="errorPromotion" type="error" dismissible class="mt-4">
            {{ errorMessage }}
            </v-alert>

            <!-- Submit Button -->
            <v-btn color="primary" width="100%" size="large" variant="flat" @click="createPromotion">
            Create Promotion
            </v-btn>
        </v-card-text>
        </v-card>
    </v-container>
</template>

    

<script>
    export default {
        name: "Promotions",
        data() {
        return {
            errorPromotion: false,
            errorMessage: '',
            promotion: {
            promotionName: '',
            promotionDescription: '',
            promotionType: '',
            validUntil: '',
            discountRate: '',
            freeQuantity: '',
            mainProduct: null,  // main product is a single product object
            mainProductName: '', // for displaying in the text field
            relatedProducts: [] // related products is an array of product objects
            },
            promotionTypes: ['DISCOUNT', 'GETFREE', 'RELATED'],
            products: [], // To hold all products for search
            filteredProducts: [], // Filtered products based on search query
            searchQuery: '', // User input for filtering products
            mainProductSearchActive: false, // Flag to activate the main product search
            relatedProductSearchActive: false, // Flag to activate the related product search
            relatedProductsText: '', // Text to display for selected related products
        };
        },
        async created() {
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
            this.products = data;
    
            this.products = this.products.map((product) => ({
            productId: product.productId,
            productName: product.productName,
            }));
    
            console.log(this.products);
    
        } catch (error) {
            console.error("Error fetching products:", error);
            this.errorMessage = "Failed to load products.";
            this.errorPromotion = true;
        }
        },
        methods: {
        filterProducts() {
            // Filter products based on the search query
            this.filteredProducts = this.products.filter(product => {
            return product.productName.toLowerCase().includes(this.searchQuery.toLowerCase());
            });
        },
        selectMainProduct(product) {
            this.promotion.mainProduct = product;
            this.promotion.mainProductName = product.productName;
            this.searchQuery = product.productName;
            console.log(this.searchQuery);
            this.mainProductSearchActive = false; // Close the dropdown after selecting
        },
        updateSearchQuery(){
            this.searchQuery = this.promotion.mainProductName;
        },
        toggleRelatedProduct(product) {
            const index = this.promotion.relatedProducts.findIndex(
            (p) => p.productId === product.productId
            );
    
            if (index === -1) {
            this.promotion.relatedProducts.push(product);
            } else {
            this.promotion.relatedProducts.splice(index, 1);
            }
            this.updateRelatedProductsText();
        },
        isProductRelated(product) {
            return this.promotion.relatedProducts.some(
            (p) => p.productId === product.productId
            );
        },
        updateRelatedProductsText() {
            this.relatedProductsText = this.promotion.relatedProducts
            .map((product) => product.productName)
            .join(", ");
        },
        async createPromotion() {
            this.errorPromotion = false;
            this.errorMessage = '';
    
            // Prepare the payload for the POST request
            const payload = {
            promotionName: this.promotion.promotionName,
            promotionDescription: this.promotion.promotionDescription,
            promotionType: this.promotion.promotionType,
            validUntil: this.promotion.validUntil,
            discountRate: this.promotion.discountRate,
            freeQuantity: this.promotion.freeQuantity,
            mainProductId: this.promotion.mainProduct ? this.promotion.mainProduct.productId : null,
            relatedProductIds: this.promotion.relatedProducts.map((product) => product.productId),
            frequentShopperRequired: false // Assuming this is a boolean flag
            };
    
            console.log(payload)
    
            try {
            const token = localStorage.getItem("jwt_token");
    
            const response = await fetch("http://localhost:8080/sales/promotions", {
                method: "POST",
                headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });
    
            if (!response.ok) {
                if (response.status === 400) {
                this.errorMessage = "Invalid input data.";
                } else if (response.status >= 500) {
                this.errorMessage = "Server error. Please try again later.";
                } else {
                this.errorMessage = "Error creating promotion.";
                }
                this.errorPromotion = true;
                return;
            }
    
            this.errorMessage = "Promotion created successfully!";
            this.errorPromotion = false;
            } catch (error) {
            console.error("Error creating promotion:", error);
            this.errorMessage = "An unexpected error occurred. Please try again.";
            this.errorPromotion = true;
            }
        }
        }
    };
</script>
    