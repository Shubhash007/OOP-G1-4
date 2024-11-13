<template>
    <v-container fluid class="fill-height">
      <v-row justify="center" align="center">
        <v-col cols="12" sm="6" md="8" class="d-flex justify-center align-center">
          <v-img :src="logo" max-width="50%"/>
        </v-col>
        <v-col cols="12" sm="6" md="4" class="d-flex justify-center align-center">
          <Login></Login>
        </v-col>
      </v-row>
    </v-container>
  </template>
  
<script>
import timperioLogo from '@/assets/logo.png'; // Adjust the path as needed

export default {
  data() {
    return {
      logo: timperioLogo,
    };
  },
};
</script>
  