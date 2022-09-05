<template>
  <v-app>
    <nav-bar></nav-bar>

    <v-main>
      <router-view />
    </v-main>
    <snackbar-message></snackbar-message>
  </v-app>
</template>

<script>
import NavBar from "./components/NavBar.vue";
import SnackbarMessage from "./components/Snackbar.vue";
export default {
  components: { NavBar, SnackbarMessage },
  name: "App",

  data: () => ({
    //
  }),
};
</script>
