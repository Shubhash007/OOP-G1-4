<template>
    <v-row justify="end" class="mb-4">
        <v-col cols="auto">
            <v-btn color="primary" @click="fetchFilteredData">Search Filters</v-btn>
        </v-col>
        <v-col cols="auto">
            <v-btn color="secondary" @click="openFileDialog" prepend-icon="mdi-upload-box-outline">
                Import CSV
            </v-btn>
            <input type="file" ref="fileInput" accept=".csv" style="display: none;" @change="importCSV" />
        </v-col>
        <v-col cols="auto">
            <v-btn color="secondary" @click="exportToCSV" prepend-icon="mdi-download-box-outline">Export to CSV</v-btn>
        </v-col>
    </v-row>

    <v-row>
        <v-alert v-if="importMessage" :type="importStatus" dismissible>
            {{ importMessage }}
        </v-alert>
    </v-row>

    <v-row>
        <v-col cols="12" sm="6" md="2">
            <v-text-field v-model="customerId" variant="outlined" label="Customer ID"
                prepend-inner-icon="mdi-magnify"></v-text-field>
        </v-col>

        <v-col cols="12" sm="6" md="2">
            <v-select label="Sale Type" v-model="saleType" variant="outlined" :items="filters" chips></v-select>
        </v-col>

        <v-col cols="12" sm="6" md="4">
            <v-date-input v-model="dateRange" label="Select Date Range" variant="outlined" max-width="368"
                multiple="range" prepend-icon="" prepend-inner-icon="$calendar"></v-date-input>
        </v-col>

        <v-col cols="12" sm="6" md="2">
            <v-text-field v-model="minPrice" variant="outlined" label="Min Price"
                prepend-inner-icon="mdi-currency-usd"></v-text-field>
        </v-col>

        <v-col cols="12" sm="6" md="2">
            <v-text-field v-model="maxPrice" variant="outlined" label="Max Price"
                prepend-inner-icon="mdi-currency-usd"></v-text-field>
        </v-col>
    </v-row>
    <v-data-table :items="items" item-value="saleId" items-per-page="10">
        <template v-slot:item.saleId="{ item }">
            {{ item.saleId || "N/A" }}
        </template>
        <template v-slot:item.saleDate="{ item }">
            {{ item.saleDate ? formatDate(item.saleDate) : "N/A" }}
        </template>
        <template v-slot:item.customer="{ item }">
            {{ item.customer.customerId }}
        </template>
        <template v-slot:item.customerZipCodes="{ item }">
            {{ item.customer?.zipCodes ? item.customer.zipCodes.join(", ") : "N/A" }}
        </template>
        <template v-slot:item.product="{ item }">
            {{ item.product.productName }}
        </template>
        <template v-slot:item.productPrice="{ item }">
            {{ item.product?.price ? formatCurrency(item.product.price) : "N/A" }}
        </template>
        <template v-slot:item.originalPrice="{ item }">
            {{ item.originalPrice ? formatCurrency(item.originalPrice) : "N/A" }}
        </template>
        <template v-slot:item.discountedPrice="{ item }">
            <span v-if="item.discountedPrice !== null && item.discountedPrice !== undefined">
                {{ formatCurrency(item.discountedPrice) }}
            </span>
            <span v-else>N/A</span>
        </template>
        <template v-slot:item.saleType="{ item }">
            <v-chip :color="getSaleTypeColor(item.saleType)" dark>
                {{ item.saleType || "Unknown" }}
            </v-chip>
        </template>
        <template v-slot:item.shippingMethod="{ item }">
            {{ item.shippingMethod || "N/A" }}
        </template>
        <template v-slot:item.quantity="{ item }">
            {{ item.quantity || 0 }}
        </template>
    </v-data-table>
</template>

<script>
export default {
    data() {
        return {
            customerId: null,
            saleType: null,
            minPrice: null,
            maxPrice: null,
            dateRange: null,
            menu: false,
            items: [],
            filters: ["DIRECT_B2B", "DIRECT_B2C", "CONSIGNMENT", "MARKETING", "WHOLESALER", "NOT_APPLICABLE"],
            startDate: null,
            endDate: null,
            importMessage: "", // Store message to display
            importStatus: "info", // Status type (success, error, etc.)


        };
    },
    methods: {
        getSaleTypeColor(saleType) {
            switch (saleType) {
                case "DIRECT_B2B":
                    return "blue";
                case "CONSIGNMENT":
                    return "green";
                case "DIRECT_B2C":
                    return "purple";
                case "MARKETING":
                    return "orange";
                case "WHOLESALER":
                    return "red";
                default:
                    return "grey";
            }
        },
        async fetchFilteredData() {
            const token = localStorage.getItem("jwt_token");

            let processedStartDate = null;
            let processedEndDate = null;

            if (Array.isArray(this.dateRange) && this.dateRange.length > 0) {
                const start = new Date(this.dateRange[0]);
                const end = new Date(this.dateRange[this.dateRange.length - 1]);

                processedStartDate = `${start.getFullYear()}-${String(start.getMonth() + 1).padStart(2, '0')}-${String(start.getDate()).padStart(2, '0')}`;
                processedEndDate = `${end.getFullYear()}-${String(end.getMonth() + 1).padStart(2, '0')}-${String(end.getDate()).padStart(2, '0')}`;
            }
            const payload = {
                customerId: this.customerId ? Number(this.customerId) : null,
                saleType: this.saleType ? this.saleType : null,
                minValue: this.minPrice !== null ? Number(this.minPrice) : null,
                maxValue: this.maxPrice !== null ? Number(this.maxPrice) : null,
                startDate: processedStartDate,
                endDate: processedEndDate,
            };
            console.log(payload);
            try {
                const response = await fetch("http://localhost:8080/purchase-history/filter", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`, // Add the token to the Authorization header
                    },
                    body: JSON.stringify(payload),
                });

                if (response.ok) {
                    this.items = await response.json();
                } else {
                    console.error("Failed to fetch filtered data.");
                }
            } catch (error) {
                console.error("Error fetching data:", error);
            }
        },
        async exportToCSV() {
            const token = localStorage.getItem("jwt_token"); // Retrieve the token from localStorage

            let processedStartDate = null;
            let processedEndDate = null;

            if (Array.isArray(this.dateRange) && this.dateRange.length > 0) {
                const start = new Date(this.dateRange[0]);
                const end = new Date(this.dateRange[this.dateRange.length - 1]);

                processedStartDate = `${start.getFullYear()}-${String(start.getMonth() + 1).padStart(2, '0')}-${String(start.getDate()).padStart(2, '0')}`;
                processedEndDate = `${end.getFullYear()}-${String(end.getMonth() + 1).padStart(2, '0')}-${String(end.getDate()).padStart(2, '0')}`;
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
                const response = await fetch("http://localhost:8080/purchase-history/export-csv", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`,
                    },
                    body: JSON.stringify(payload),
                });

                if (response.ok) {
                    const blob = await response.blob();
                    const url = window.URL.createObjectURL(blob);
                    const link = document.createElement("a");
                    link.href = url;
                    link.download = "sales_data.csv";
                    document.body.appendChild(link);
                    link.click();
                    link.remove();
                } else {
                    console.error("Failed to export CSV.");
                }
            } catch (error) {
                console.error("Error exporting to CSV:", error);
            }
        },
        async getItems() {
            const token = localStorage.getItem("jwt_token");

            try {
                const response = await fetch("http://localhost:8080/purchase-history/get-all", {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`,
                    },
                });

                if (response.ok) {
                    this.items = await response.json();
                } else {
                    console.error("Failed to fetch all items.");
                }
            } catch (error) {
                console.error("Error fetching all items:", error);
            }
        },
        openFileDialog() {
            this.$refs.fileInput.click();
        },
        async importCSV(event) {
            const file = event.target.files[0];

            if (!file) {
                this.importMessage = "No file selected!";
                this.importStatus = "error"; // Set color to error
                return;
            }

            const formData = new FormData();
            formData.append("file", file);

            const token = localStorage.getItem("jwt_token");

            try {
                const response = await fetch("http://localhost:8080/sales-data/upload", {
                    method: "POST",
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                    body: formData,
                });

                if (response.ok) {
                    const contentType = response.headers.get("content-type");
                    let message;

                    if (contentType && contentType.includes("application/json")) {
                        const result = await response.json();
                        message = result.message || "CSV imported successfully!";
                    } else {
                        message = await response.text();
                    }

                    this.importMessage = message; // Set message
                    this.importStatus = "success";
                    console.log(message);
                } else {
                    const error = await response.text();
                    this.importMessage = `Failed to import CSV: ${error}`;
                    this.importStatus = "error";
                    console.error("Error importing CSV:", error);
                }
            } catch (err) {
                console.error("An unexpected error occurred:", err);
                this.importMessage = "An unexpected error occurred!";
                this.importStatus = "error";
            }
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
    mounted() {
        this.getItems();
    },
};
</script>