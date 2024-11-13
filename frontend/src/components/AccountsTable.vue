<template>
    <v-data-table
      v-model="selected"
      :items="items"
      item-value="name"
      show-select
      items-per-page="5"
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
        selected: [],
        items: [
          {
            name: "John Doe",
            email: "john@example.com",
            location: "New York",
            role: "Admin",
          },
          {
            name: "Jane Smith",
            email: "jane@example.com",
            location: "London",
            role: "User",
          },
          {
            name: "Michael Green",
            email: "michael@example.com",
            location: "Sydney",
            role: "Moderator",
          },
          {
            name: "Emily Johnson",
            email: "emily@example.com",
            location: "Toronto",
            role: "Admin",
          },
          {
            name: "David Brown",
            email: "david@example.com",
            location: "San Francisco",
            role: "User",
          },
          {
            name: "Lucy White",
            email: "lucy@example.com",
            location: "Berlin",
            role: "User",
          },
          {
            name: "Sophia Black",
            email: "sophia@example.com",
            location: "Paris",
            role: "Moderator",
          },
          {
            name: "James Blue",
            email: "james@example.com",
            location: "Tokyo",
            role: "User",
          },
          {
            name: "Lily Green",
            email: "lily@example.com",
            location: "Rome",
            role: "User",
          },
          {
            name: "Daniel Gray",
            email: "daniel@example.com",
            location: "Moscow",
            role: "Admin",
          },
        ],
      };
    },
    methods: {
      getRoleColor(role) {
        switch (role) {
          case "Admin":
            return "red";
          case "Moderator":
            return "blue";
          case "User":
          default:
            return "green";
        }
      },
    },
  };
  </script>
  