<template>
    <v-row>
        <v-col cols="12" sm="6" md="4">
            <v-text-field
                v-model="search"
                variant="outlined"
                label="Search based on Customer ID"
                prepend-inner-icon="mdi-magnify"
                @input="$emit('update:search', search)"
            ></v-text-field>
        </v-col>

        <v-col cols="12" sm="6" md="2">
            <v-select
                label="Sale Type"
                v-model="selectedFilter"
                variant="outlined"
                :items="filters"
                multiple
                chips
                @change="$emit('update:filter', selectedFilter)"
            ></v-select>
        </v-col>

        <v-col cols="12" sm="6" md="2">
            <v-menu
                ref="menu"
                v-model="menu"
                :close-on-content-click="false"
                :nudge-right="40"
                transition="scale-transition"
                offset-y
                min-width="auto"
            >
                <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                        variant="outlined"
                        v-model="dateRangeText"
                        label="Date Range"
                        prepend-inner-icon="mdi-calendar"
                        readonly
                        v-bind="attrs"
                        v-on="on"
                    ></v-text-field>
                </template>
                <v-date-picker
                v-model="dateRange"
                range
                @input="updateDateRange"
                ></v-date-picker>
            </v-menu>
        </v-col>

        <v-col cols="12" sm="6" md="4">
            <v-row>
                <v-col cols="12" sm="6" md="5">
                <v-text-field
                v-model="minPrice"
                variant="outlined"
                label="Min Price"
                prepend-inner-icon="mdi-currency-usd"
                @input="$emit('update:minPrice', minPrice)"
                ></v-text-field>
                </v-col>
                <v-col cols="12" sm="6" md="1" class="d-flex justify-center"><p class="mt-4 text-center">—</p></v-col>
                <v-col cols="12" sm="6" md="5">
                <v-text-field
                    v-model="maxPrice"
                    variant="outlined"
                    label="Max Price"
                    prepend-inner-icon="mdi-currency-usd"
                    @input="$emit('update:maxPrice', maxPrice)"
                ></v-text-field>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
</template>


<script>
export default {
    props: {
        filters: Array,
        minPrice: {
        type: Number,
        default: 0,
        },
        maxPrice: {
        type: Number,
        default: 1000,
        },
    },
    data() {
        return {
        search: '',
        selectedFilter: '',
        dateRange: [],
        menu: false,
        minPrice: 0,
        maxPrice: 100,
        };
    },
    computed: {
        dateRangeText() {
        return this.dateRange.length
            ? `${this.dateRange[0]} - ${this.dateRange[1]}`
            : '';
        },
    },
    methods: {
        updateDateRange(val) {
            this.dateRange = val;
            this.$emit('update:dateRange', val);
        },
        updatePriceRange() {
            this.$emit('update:priceRange', this.priceRange);
        },
    },
};
</script>