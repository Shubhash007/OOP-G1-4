<template>
    <v-container>
      <v-btn color="primary" @click="goToPage">
        Go to Home Page
      </v-btn>
    </v-container>
  </template>
  
  <script>
  export default {
    methods: {
      goToPage() {
        this.$router.push('/home');
      },
    },
  };
  </script>
  