<template>
  <v-dialog
    v-model="updatePasswordDialogData.isOpen"
    max-width="600"
    :retain-focus="false"
  >
    <v-card>
      <v-card-title> Jelszó csere </v-card-title>
      <v-card-text>
        <v-form ref="loginForm" v-model="valid" lazy-validation>
          <v-text-field
            v-model="form.oldPassword"
            label="Jelenlegi jelszavad"
            name="password"
            :rules="[rules.required]"
          ></v-text-field>
          <v-text-field
            v-model="form.newPassword"
            label="Új jelszó"
            name="password"
            :rules="[rules.required, rules.min8]"
          ></v-text-field>
          <v-text-field
            v-model="form.newPassword2"
            label="Új jelszó még egyszer"
            name="password"
            :rules="[rules.required, rules.min8, rules.match]"
          ></v-text-field>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="cgreen darken-1" text @click="cancel()"> Mégse </v-btn>
        <v-btn color="success" text :disabled="!valid" @click="confirm()">
          OK
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "UpdatePasswordDialog",
  props: [],
  data: () => ({
    form: {
      oldPassword: "",
      newPassword: "",
      newPassword2: "",
    },
    valid: true,
  }),
  computed: {
    ...mapState("dialog", ["updatePasswordDialogData"]),
    rules() {
      var that = this;
      var rules = {
        required: (value) => !!value || "Kötelező mező.",
        min8: (value) => (value && value.length >= 8) || "Minimum 8 karakter",
        match: (value) =>
          value === that.form.newPassword || "A két jelszó nem egyezik.",
      };
      return rules;
    },
  },
  mounted() {},
  methods: {
    confirm() {
      this.$store.commit("dialog/closeDialog");
      this.$emit("confirm", {
        old: this.form.oldPassword,
        new: this.form.newPassword,
      });

      this.form.oldPassword = "";
      this.form.newPassword = "";
      this.form.newPassword2 = "";
    },
    cancel() {
      this.form.oldPassword = "";
      this.form.newPassword = "";
      this.form.newPassword2 = "";
      this.$store.commit("dialog/closeDialog");
      this.$emit("cancel");
    },
  },
};
</script>
</script>

<style>
</style>
