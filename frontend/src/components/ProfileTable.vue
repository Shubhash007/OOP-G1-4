<template>
  <v-container fluid>
    <v-row justify="center">
      <v-col cols="12" md="6">
        <v-card class="ma-4 pa-8" elevation="2">
          <v-card-title>User Profile</v-card-title>
          <v-card-text>
            <v-list dense>
              <v-list-item>
                <v-list-item-content>
                  <v-list-item-title class="font-weight-bold">Username: {{ username }}</v-list-item-title>
                  <v-list-item-subtitle>Role: {{ role }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-list>
            <v-divider class="my-4"></v-divider>

            <v-btn color="primary" @click="dialog = true" block>Change Password</v-btn>

            <v-alert v-if="errorMessage" type="error" class="mt-4">
              {{ errorMessage }}
            </v-alert>

            <v-alert v-if="successMessage" type="success" class="mt-4">
              {{ successMessage }}
            </v-alert>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- Password Change Modal -->
    <v-dialog v-model="dialog" max-width="400px">
      <v-card>
        <v-card-title class="headline">Update Password</v-card-title>
        <v-card-text>
          <v-form @submit.prevent="submitPasswordUpdate">
            <v-text-field
              v-model="currentPassword"
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
        </v-card-text>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      role: '',
      currentPassword: '',
      newPassword: '',
      confirmPassword: '',
      errorMessage: '',
      successMessage: '', // Added success message
      dialog: false, // Modal visibility
    };
  },
  async created() {
    await this.fetchUserProfile();
  },
  methods: {
    async fetchUserProfile() {
      const token = localStorage.getItem("jwt_token");

      try {
        const response = await fetch("http://localhost:8080/auth/users/get-user", {
          method: "GET",
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
          this.errorMessage = "Failed to fetch profile data"; // Use Vuetify alert to show error
        }
      } catch (error) {
        this.errorMessage = "An error occurred while fetching the profile."; // Use Vuetify alert to show error
      }
    },

    async submitPasswordUpdate() {
      if (this.newPassword !== this.confirmPassword) {
        this.errorMessage = "Passwords do not match"; // Show error with Vuetify alert
        this.successMessage = ''; // Clear any success message
        this.dialog = false; // Close the modal if error occurs
        return;
      }

      const token = localStorage.getItem("jwt_token");

      // Step 1: Verify the current password via the login endpoint
      const loginResponse = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: this.username,
          password: this.currentPassword,
        }),
      });

      if (!loginResponse.ok) {
        this.errorMessage = "Current password is incorrect.";
        this.successMessage = '';
        this.dialog = false; // Close the modal if error occurs
        return;
      }

      // Step 2: If login is successful, proceed to password update
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
          this.errorMessage = ''; // Clear any error messages
          this.successMessage = "Password updated successfully!"; // Show success with Vuetify alert
          this.newPassword = this.confirmPassword = '';
          this.dialog = false; // Close modal after successful update

          // Clear the JWT token and log the user out
          localStorage.removeItem("jwt_token");

          // Optionally redirect to login page after password update
          this.$router.push("/login"); // Redirect to login page after logout
        } else {
          const errorData = await response.json();
          this.errorMessage = errorData.message || "Error updating password"; // Show error with Vuetify alert
          this.successMessage = ''; // Clear any success message
        }
      } catch (error) {
        this.errorMessage = "An unexpected error occurred. Please try again."; // Show error with Vuetify alert
        this.successMessage = ''; // Clear any success message
      }
    },
  },
};
</script>

<style scoped>

.v-btn {
  text-transform: none;
}

.v-alert {
  background-color: #ffefef;
}

.v-card-title {
  font-weight: bold;
}

.v-form {
  margin-top: 10px;
}

.v-text-field {
  margin-bottom: 10px;
}
</style>
