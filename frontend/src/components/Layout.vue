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

        <!-- Conditionally render Admin menu item -->
        <v-list-item v-if="isAdmin" to="/admin" prepend-icon="mdi-cog-outline" link class="p-6">
          <v-list-item-title>Admin</v-list-item-title>
        </v-list-item>

        <v-list-item to="/analytics" prepend-icon="mdi-chart-bar" link class="p-6">
          <v-list-item-title>Analytics</v-list-item-title>
        </v-list-item>

        <v-list-item to="/marketing" prepend-icon="mdi-account-multiple" link class="p-6">
          <v-list-item-title>Marketing</v-list-item-title>
        </v-list-item>

        <v-list-item to="/profile" prepend-icon="mdi-account" link class="p-6">
          <v-list-item-title>Profile</v-list-item-title>
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
      isAdmin: false, // Initially set to false
    };
  },
  methods: {
    logout() {
      // Remove JWT token from localStorage
      localStorage.removeItem("jwt_token");

      // Redirect to login page
      this.$router.push("/");
    },
    async checkUserRole() {
      const jwtToken = localStorage.getItem("jwt_token");

      if (jwtToken) {
        try {
          // Make the API call to get the user details
          const response = await fetch("http://localhost:8080/auth/users/get-user", {
            method: "GET",
            headers: {
              "Authorization": `Bearer ${jwtToken}`,
              "Content-Type": "application/json",
            },
          });

          if (response.ok) {
            const user = await response.json();
            const userRole = user.role; // Assuming the role is in the user object
            this.isAdmin = userRole === 'ROLE_ADMIN';
          } else {
            console.error("Failed to fetch user data");
            this.isAdmin = false; // Default to false if the API call fails
          }
        } catch (error) {
          console.error("Error fetching user info:", error);
          this.isAdmin = false; // Default to false if there's an error
        }
      }
    },
  },
  created() {
    this.checkUserRole(); // Check user role when the component is created
  },
};
</script>

