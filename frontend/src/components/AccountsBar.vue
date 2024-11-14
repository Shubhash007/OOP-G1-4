<template>
    <v-row class="pa-4 align-center">
      <v-col cols="12" md="6" class="pr-2">
        <v-text-field
          variant="outlined"
          v-model="searchQuery"
          label="Search user name"
          prepend-inner-icon="mdi-magnify"
          hide-details
          full-width
        ></v-text-field>
      </v-col>
  
      <v-col cols="6" md="3">
        <v-btn color="secondary" class="ml-0" block @click="showUpdateDialog = true">
          <v-icon class="mr-2" left>mdi-update</v-icon>
          Update {{ selectedUser ? selectedUser.name : '' }}
        </v-btn>
      </v-col>
  
      <v-col cols="6" md="3">
        <v-btn color="primary" class="ml-0" block @click="showCreateAccountDialog = true">
          <v-icon class="mr-2">mdi-account-plus</v-icon>
          Create Account
        </v-btn>
      </v-col>
  
      <!-- Update Dialog -->
      <v-dialog v-model="showUpdateDialog" max-width="500px">
        <v-card>
          <v-card-title><span class="headline">Update Account</span></v-card-title>
          <v-card-text>
            <v-form>
              <v-text-field v-model="newUserName" label="Username" required></v-text-field>
              <v-select v-model="newUserRole" :items="userRoles" label="Role" required></v-select>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" @click="updateUser">Update</v-btn>
            <v-spacer></v-spacer>
            <v-btn color="error" @click="showUpdateDialog = false">Cancel</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>
  </template>
  
  <script>
  export default {
    props: ["selectedUser"],
    data() {
      return {
        searchQuery: '',
        showCreateAccountDialog: false,
        showUpdateDialog: false,
        newUserName: '',
        newUserRole: '',
        userRoles: ['ROLE_ADMIN', 'ROLE_MARKETING', 'ROLE_SALES'],
      };
    },
    watch: {
      selectedUser: {
        immediate: true,
        handler(user) {
          if (user) {
            this.newUserName = user.name;
            this.newUserRole = user.role;
          }
        },
      },
    },
    methods: {
      updateUser() {
        console.log("Selected user data for update:", {
          name: this.newUserName,
          role: this.newUserRole,
        });
        this.showUpdateDialog = false;
        // Further actions for updating user can go here.
      },
    },
  };
  </script>
  