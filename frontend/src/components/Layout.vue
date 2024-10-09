<template>
    <v-app>
      <v-navigation-drawer
        v-model="drawer"
        :rail="rail"
        app
        permanent
      >
      <v-list-item class="pa-4">
        <v-row>
          <v-col v-if="!rail">
            <v-img src="@/assets/logo.png" alt="Logo" max-width="80" class="pa-4"></v-img>
          </v-col>
          <v-col cols="auto">
            <v-btn icon="mdi-menu" variant="text" @click.stop="rail = !rail"></v-btn>
          </v-col>

        </v-row>
      </v-list-item>
  
        <v-list dense>
          <v-list-item
            to="/home"
            prepend-icon="mdi-home-outline"
            link
            class="pa-4"
          >
            <v-list-item-title>Home</v-list-item-title>
          </v-list-item>
  
          <v-list-item
            to="/about"
            prepend-icon="mdi-account-outline"
            link
            class="pa-4"
          >
            <v-list-item-title>About</v-list-item-title>
          </v-list-item>
  
          <v-list-item
            to="/purchaseHistory"
            prepend-icon="mdi-history"
            link
            class="pa-4"
          >
            <v-list-item-title>Purchase History</v-list-item-title>
          </v-list-item>
  
          <v-list-item
            to="/admin"
            prepend-icon="mdi-cog-outline"
            link
            class="pa-4"
          >
            <v-list-item-title>Admin</v-list-item-title>
          </v-list-item>
  
          <v-list-item
            to="/analytics"
            prepend-icon="mdi-chart-bar"
            link
            class="pa-4"
          >
            <v-list-item-title>Analytics</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-navigation-drawer>
  
      <v-main>
        <v-container fluid>
          <router-view></router-view>
        </v-container>
      </v-main>
    </v-app>
  </template>
  
  <script>
  export default {
    name: "Layout",
    data() {
      return {
        drawer: true, // Controls the visibility of the navigation drawer
        rail: true,   // Controls the collapsed state (rail mode) of the drawer
      };
    },
  };
  </script>