<template>
  <v-container fluid>
      <AdminTable :selectedUser="selectedUser" @updateSelectedUser="updateSelectedUser" />
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
