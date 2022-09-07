<template>
  <v-dialog
    v-model="updatePasswordDialogData.isOpen"
    max-width="600"
    :retain-focus="false"
  >
    <v-card>
      <v-card-title> Jelszó csere </v-card-title>
      <v-card-text>
        <v-form ref="passwordForm" v-model="valid" lazy-validation>
          <v-text-field
            v-model="form.oldPassword"
            label="Jelenlegi jelszavad"
            :rules="[rules.required]"
            :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
            :type="show1 ? 'text' : 'password'"
            @click:append="show1 = !show1"
            name="password"
            autocomplete="password"
            counter
          ></v-text-field>
          <v-text-field
            v-model="form.newPassword"
            label="Új jelszó"
            :rules="[rules.required, rules.min8]"
            :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
            :type="show2 ? 'text' : 'password'"
            @click:append="show2 = !show2"
            name="password"
            autocomplete="password"
            counter
          ></v-text-field>
          <v-text-field
            v-model="form.newPassword2"
            label="Új jelszó még egyszer"
            :rules="[rules.required, rules.min8, rules.match]"
            :append-icon="show3 ? 'mdi-eye' : 'mdi-eye-off'"
            :type="show3 ? 'text' : 'password'"
            @click:append="show3 = !show3"
            name="password"
            autocomplete="password"
            counter
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
    show1: false,
    show2: false,
    show3: false,
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
