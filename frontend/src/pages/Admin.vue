<template>
    <v-container fluid>
      <v-card class="ma-4 pa-8" elevation="0">
        <AccountsBar :selectedUser="selectedUser" />
        <AccountsTable @user-selected="updateSelectedUser" />
      </v-card>
    </v-container>
  </template>
  
  <script>
  import AccountsBar from "@/components/AccountsBar.vue";
  import AccountsTable from "@/components/AccountsTable.vue";
  
  export default {
    components: { AccountsBar, AccountsTable },
    data() {
      return {
        selectedUser: null,
      };
    },
    methods: {
      updateSelectedUser(user) {
        console.log("Updating selectedUser:", user); // Verify the received user data here
        this.selectedUser = user;
      },
    },
  };
  </script>
  