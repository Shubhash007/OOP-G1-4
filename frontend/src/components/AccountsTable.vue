<template>
  <v-data-table
    :items="items"
    item-value="name"
    show-select
    items-per-page="5"
    @click:row="selectUser"
  >
    <template v-slot:item.role="{ item }">
      <v-chip :color="getRoleColor(item.role)" dark>{{ item.role }}</v-chip>
    </template>
  </v-data-table>
</template>

<script>
export default {
  data() {
    return {
      items: [
        { name: "John Doe", email: "john@example.com", location: "New York", role: "Admin" },
        { name: "Jane Smith", email: "jane@example.com", location: "London", role: "User" },
        { name: "Michael Green", email: "michael@example.com", location: "Sydney", role: "Moderator" },
        // Other sample data
      ],
    };
  },
  methods: {
    getRoleColor(role) {
      return role === "Admin" ? "red" : role === "Moderator" ? "blue" : "green";
    },
    selectUser(user) {
      console.log("Selected user:", user); // Verify user data here
      this.$emit("user-selected", user); // Emit selected user data to parent
    },
  },
};
</script>
