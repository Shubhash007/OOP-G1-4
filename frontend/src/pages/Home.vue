<template>
    <v-container>
      <v-btn color="primary" @click="goToPage">
        Go to About Page
      </v-btn>
    </v-container>
  </template>
  
  <script>
  export default {
    methods: {
      goToPage() {
        this.$router.push('/about');
      },
    },
  };
  </script>
  