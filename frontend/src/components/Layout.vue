<template>
  <v-app>
    <v-app-bar app>
      <v-row class="align-center">
        <v-col cols="auto">
          <v-img src="@/assets/logo.png" alt="Logo" max-width="80"></v-img>
        </v-col>

        <v-col cols="auto">
          <v-btn icon="mdi-menu" variant="text" @click.stop="drawer = !drawer"></v-btn>
        </v-col>
      </v-row>
    </v-app-bar>

    <v-navigation-drawer v-model="drawer" app permanent class="pt-1">
      <v-list nav dense>
        <v-list-item to="/home" prepend-icon="mdi-home-outline" link class="p-6">
          <v-list-item-title>Home</v-list-item-title>
        </v-list-item>

        <v-list-item to="/purchaseHistory" prepend-icon="mdi-history" link class="p-6">
          <v-list-item-title>Purchase History</v-list-item-title>
        </v-list-item>

        <v-list-item to="/admin" prepend-icon="mdi-cog-outline" link class="p-6">
          <v-list-item-title>Admin</v-list-item-title>
        </v-list-item>

        <v-list-item to="/analytics" prepend-icon="mdi-chart-bar" link class="p-6">
          <v-list-item-title>Analytics</v-list-item-title>
        </v-list-item>

        <v-list-item to="/marketing" prepend-icon="mdi-account-multiple" link class="p-6">
          <v-list-item-title>Marketing</v-list-item-title>
        </v-list-item>

        <!-- Logout Button -->
        <v-list-item to="" link @click="logout" prepend-icon="mdi-logout" class="p-6">
          <v-list-item-title>Logout</v-list-item-title>
        </v-list-item>

      </v-list>
    </v-navigation-drawer>

    <!-- Main Content Area -->
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
      drawer: true,
    };
  },
  methods: {
    logout() {
      // Remove JWT token from localStorage
      localStorage.removeItem("jwt_token");

      // Redirect to login page
      this.$router.push("/");
    }
  }
};
</script>
