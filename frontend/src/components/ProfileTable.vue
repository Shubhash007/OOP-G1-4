// src/pages/Profile.vue
<template>
  <v-container fluid>
    <v-card class="ma-4 pa-8" elevation="0" max-width="600px">
      <v-card-title>User Profile</v-card-title>
      <v-card-text>
        <v-list dense>
          <v-list-item>
            <v-list-item-content>
              <v-list-item-title>Username: {{ username }}</v-list-item-title>
              <v-list-item-subtitle>Role: {{ role }}</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </v-list>
        <v-divider class="my-4"></v-divider>
        <v-form @submit.prevent="submitPasswordUpdate">
          <v-text-field
            v-model="oldPassword"
            label="Current Password"
            type="password"
            required
          ></v-text-field>
          <v-text-field
            v-model="newPassword"
            label="New Password"
            type="password"
            required
          ></v-text-field>
          <v-text-field
            v-model="confirmPassword"
            label="Confirm New Password"
            type="password"
            required
          ></v-text-field>
          <v-btn color="primary" type="submit" block>Update Password</v-btn>
        </v-form>
        <v-alert v-if="errorMessage" type="error" class="mt-4">
          {{ errorMessage }}
        </v-alert>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      role: '',
      oldPassword: '',
      newPassword: '',
      confirmPassword: '',
      errorMessage: ''
    };
  },
  async created() {
    await this.fetchUserProfile();
  },
  methods: {
    async fetchUserProfile() {
      const token = localStorage.getItem("jwt_token");

      try {
        const response = await fetch("http://localhost:8080/auth/profile", {
          method: "POST",
          headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });
        if (response.ok) {
          const profile = await response.json();
          this.username = profile.username;
          this.role = profile.role;
        } else {
          console.error("Failed to fetch profile data");
        }
      } catch (error) {
        console.error("An error occurred while fetching the profile:", error);
      }
    },

    
    async submitPasswordUpdate() {
      if (this.newPassword !== this.confirmPassword) {
        this.errorMessage = "Passwords do not match";
        return;
      }

      const token = localStorage.getItem("jwt_token");

      try {
        const response = await fetch("http://localhost:8080/auth/users/update-password", {
          method: "POST",
          headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ newPassword: this.newPassword }),
        });

        if (response.ok) {
          alert("Password updated successfully!");
          this.oldPassword = this.newPassword = this.confirmPassword = '';
          this.errorMessage = '';
        } else {
          const errorData = await response.json();
          this.errorMessage = errorData.message || "Error updating password";
        }
      } catch (error) {
        console.error("Error updating password:", error);
        this.errorMessage = "An unexpected error occurred. Please try again.";
      }
    },
  },
};
</script>
