<template>
  <component :is="loadComp"></component>
</template>

<script>
export default {
  name: "Dashboard",

  components: {},
  data: () => ({}),
  computed: {
    loadComp() {
      var compName = "Login";
      if (this.$store.loggedIn && this.$store.isAdmin) {
        compName = "AdminDashboard";
      }
      //if (this.$store.loggedIn && !this.$store.isAdmin) {
        compName = "UserDashboard";
      //}

      return () => import(`./${compName}`);
    },
  },
};
</script>
