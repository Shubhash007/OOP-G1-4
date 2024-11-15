<template>
  <v-container fluid>
    <v-card class="ma-4 pa-8" elevation="0">
      <AdminTable :selectedUser="selectedUser" @updateSelectedUser="updateSelectedUser" />
    </v-card>
  </v-container>
</template>

<script>
import AdminTable from "@/components/AdminTable.vue";

export default {
  components: { AdminTable },
  data() {
    return {
      selectedUserDto: null, // Holds the selected user
    };
  },
  methods: {
    updateSelectedUser(user) {
      console.log("Updating selectedUser:", user);
      this.selectedUserDto = user;
    },
  },
};
</script>
