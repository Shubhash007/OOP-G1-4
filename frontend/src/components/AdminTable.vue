<template>
  <v-row class="pa-4 align-center">
    <v-col cols="12" md="5">
      <v-text-field variant="outlined" v-model="searchQuery" label="Search user name" prepend-inner-icon="mdi-magnify"
        hide-details full-width></v-text-field>
    </v-col>

    <v-col cols="4" md="2">
      <v-btn color="secondary" class="ml-0" block @click="openUpdateDialog" :disabled="!selected">
        <v-icon class="mr-2" left>mdi-update</v-icon>
        Update
      </v-btn>
    </v-col>
    <v-col cols="4" md="2">
      <v-btn color="disabled" class="ml-0" block @click="deleteUser" :disabled="!selected">
        <v-icon class="mr-2" left>mdi-delete</v-icon>
        Delete
      </v-btn>
    </v-col>
    <v-col cols="4" md="3">
      <v-btn color="primary" class="ml-0" block @click="showCreateAccountDialog = true">
        <v-icon class="mr-2">mdi-account-plus</v-icon>
        Create Account
      </v-btn>
    </v-col>

  </v-row>


  <v-alert v-if="successMessage" type="success" class="mt-4" dense>
    {{ successMessage }}
  </v-alert>

  <v-data-table :items="filteredItems" item-value="username" show-select items-per-page="10" select-strategy="single"
    v-model="selected">
    <template v-slot:item.role="{ item }">
      <v-chip :color="getRoleColor(item.role)" dark>{{ item.role }}</v-chip>
    </template>
  </v-data-table>

  <v-dialog v-model="showUpdateDialog" max-width="500px">
    <v-card>
      <v-card-title><span class="headline">Update Account</span></v-card-title>
      <v-card-text>
        <v-form>
          <v-text-field v-model="newUserName" label="Username" required></v-text-field>

          <v-select v-model="newUserRole" :items="userRoles" label="Role" required></v-select>

          <v-text-field v-model="newUserPassword" label="New Password" type="password"></v-text-field>

          <v-alert v-if="errorMessage" type="error" class="mt-4" dense>
            {{ errorMessage }}
          </v-alert>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="updateUser">Update</v-btn>
        <v-spacer></v-spacer>
        <v-btn color="error" @click="showUpdateDialog = false">Cancel</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>


  <v-dialog v-model="showCreateAccountDialog" max-width="500px">
    <v-card>
      <v-card-title><span class="headline">Create Account</span></v-card-title>
      <v-card-text>
        <v-form>
          <v-text-field v-model="newUserName" label="Username" required></v-text-field>
          <v-select v-model="newUserRole" :items="userRoles" label="Role" required></v-select>
          <v-text-field v-model="newUserPassword" label="Password" type="password" required></v-text-field>
          <v-alert v-if="errorMessage" type="error" class="mt-4" dense>
            {{ errorMessage }}
          </v-alert>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="createAccount">Create</v-btn>
        <v-spacer></v-spacer>
        <v-btn color="error" @click="showCreateAccountDialog = false">Cancel</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data() {
    return {
      items: [],
      selected: null,
      searchQuery: '',
      showCreateAccountDialog: false,
      showUpdateDialog: false,

      newUserName: '',
      newUserRole: '',
      newUserPassword: '',

      userRoles: ['ROLE_ADMIN', 'ROLE_MARKETING', 'ROLE_SALES'],
      successMessage: '',
      errorMessage: '',
    };
  },
  
  computed: {
    selectedUserDto() {
      if (Array.isArray(this.selected) && this.selected.length === 1) {
        const selectedUsername = this.selected[0];
        return this.items.find((item) => item.username === selectedUsername) || null;
      }
      return null;
    },

    filteredItems() {
      return this.searchQuery
        ? this.items
          .filter((item) =>
            item.username.toLowerCase().includes(this.searchQuery.toLowerCase())
          )
          .map(({ password, ...rest }) => rest)
        : this.items.map(({ password, ...rest }) => rest);
    }
  },
  async created() {
    await this.fetchUsers();
  },
  watch: {
    selected(newVal) {
      if (!newVal || newVal.length === 0) {
        this.selected = null;
      }
    },
  },
  methods: {
    async fetchUsers() {
      const token = localStorage.getItem("jwt_token");

      try {
        const response = await fetch("http://localhost:8080/auth/admin/getAllUsers", {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

        if (response.ok) {
          const users = await response.json();
          this.items = users;

        } else {
          console.error("Failed to fetch users.");
        }
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    },

    getRoleColor(role) {
      return role === "ROLE_ADMIN" ? "red" : role === "ROLE_SALES" ? "blue" : "green";
    },

    openUpdateDialog() {
      if (this.selectedUserDto) {
        this.newUserName = this.selectedUserDto.username;
        this.newUserRole = this.selectedUserDto.role;
        this.showUpdateDialog = true;
      } else {
        console.log("No user selected");
      }
    },



    async updateUser() {
      const token = localStorage.getItem("jwt_token");

      const payload = {
        username: this.selected[0] || null,         // Always include as the identifier
        newUsername: this.newUserName || null,      // Set to null if no new username is provided
        newPassword: this.newUserPassword || null,  // Set to null if no new password is provided
        newRole: this.newUserRole || null           // Set to null if no new role is provided
      };

      console.log("Payload:", payload);

      try {
        const response = await fetch("http://localhost:8080/auth/admin/updateUser", {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify(payload),
        });

        if (response.ok) {
          this.successMessage = "User updated successfully!";
          this.errorMessage = '';
          this.showUpdateDialog = false;

          // Refresh user list after update
          await this.fetchUsers();
        } else {
          const errorData = await response.json();
          this.errorMessage = errorData.message || "Error updating user.";
        }
      } catch (error) {
        this.errorMessage = "An unexpected error occurred. Please try again.";
      }
    },


    async createAccount() {
      const token = localStorage.getItem("jwt_token");

      try {
        const response = await fetch("http://localhost:8080/auth/admin/createUser", {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            username: this.newUserName,
            role: this.newUserRole,
            password: this.newUserPassword,
          }),
        });

        if (response.ok) {
          this.successMessage = "Account created successfully!";
          this.errorMessage = '';

          this.newUserName = '';
          this.newUserRole = '';
          this.newUserPassword = '';
          this.showCreateAccountDialog = false;

          await this.fetchUsers();
        } else {
          const errorData = await response.json();
          this.errorMessage = errorData.message || "Error creating account.";
        }
      } catch (error) {
        this.errorMessage = "An unexpected error occurred. Please try again.";
      }
    },


    async deleteUser() {
      const token = localStorage.getItem("jwt_token");

      const payload = {
        username: this.selected[0] || null, // Use the selected username as the identifier
      };

      if (!payload.username) {
        this.errorMessage = "No user selected for deletion.";
        return;
      }

      try {
        const response = await fetch(`http://localhost:8080/auth/admin/deleteUser?username=${payload.username}`, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

        if (response.ok) {
          this.successMessage = "User deleted successfully!";
          this.errorMessage = '';
          this.selected = null; // Clear the selected user after deletion
          await this.fetchUsers(); // Refresh the user list
        } else {
          const errorData = await response.json();
          this.errorMessage = errorData.message || "Error deleting user.";
        }
      } catch (error) {
        this.errorMessage = "An unexpected error occurred. Please try again.";
      }
    },

  },
};
</script>
