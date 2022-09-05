<template>
  <v-dialog v-model="confirmDialogData.isOpen" max-width="290" :retain-focus="false">
    <v-card>
      <v-card-title> {{ confirmDialogData.title }} </v-card-title>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="cgreen darken-1" text @click="cancel()"> Mégse </v-btn>
        <v-btn :color="confirmDialogData.confirmButtonColor" text @click="confirm()">
          {{ confirmDialogData.confirmButton }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "ConfirmDialog",
  props: [],
  data: () => ({}),
  computed: {
    ...mapState("dialog", ["confirmDialogData"]),
  },
  mounted() {},
  methods: {
    confirm() {
      this.$store.commit("dialog/closeDialog");

      this.$emit("confirm");
    },
    cancel() {
      this.$store.commit("dialog/closeDialog");
      this.$emit("cancel");
    },
  },
};
</script>

<style>
</style>
