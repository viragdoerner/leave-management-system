<template>
  <v-dialog
    v-model="userEditDialogData.isOpen"
    max-width="600"
    :retain-focus="false"
  >
    <v-card>
      <v-card-title> {{ userEditDialogData.title }} </v-card-title>
      <v-card-text>
        <v-text-field
          v-model="form.email"
          label="Email"
          name="email"
          :rules="[rules.email, rules.required]"
        ></v-text-field>
        <v-text-field
          v-model="form.fullname"
          label="Név"
          name="name"
          :rules="[rules.min6, rules.required]"
        ></v-text-field>
        <v-text-field
          v-model="form.holidays"
          label="Szabadnapok száma egy évben"
        ></v-text-field>
        <v-checkbox
          v-model="form.saturday"
          label="Dolgozik-e szombaton"
        ></v-checkbox>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="cgreen darken-1" text @click="cancel()"> Mégse </v-btn>
        <v-btn color="success" text @click="confirm()"> Mentés </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "EditUserDialog",
  props: ["user"],
  data: () => ({
    form: { email: "", fullname: "", holidays: 0, saturday: false },
    rules: {
      email: (value) => /.+@.+\..+/.test(value) || "Érvénytelen e-mail",
      required: (value) => !!value || "Kötelező mező.",
      min6: (value) => (value && value.length >= 6) || "Minimum 6 karakter",
    },
  }),
  computed: {
    ...mapState("dialog", ["userEditDialogData"]),

  },
  mounted() {},
  watch: {
    user(newUser) {
      this.form = Object.assign({}, newUser);
    }
  },
  methods: {
    confirm() {
      this.$store.commit("dialog/closeDialog");
      this.$emit("confirm", this.form);
      this.form = Object.assign({}, this.user);
    },
    cancel() {
      this.form = Object.assign({}, this.user);
      this.$store.commit("dialog/closeDialog");
      this.$emit("cancel");
    },
  },
};
</script>

<style>
</style>
