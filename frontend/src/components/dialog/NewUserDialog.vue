<template>
  <v-dialog
    v-model="userNewDialogData.isOpen"
    max-width="600"
    :retain-focus="false"
  >
    <v-card>
      <v-card-title> {{ userNewDialogData.title }} </v-card-title>
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
          :rules="[rules.min8, rules.required]"
        ></v-text-field>
        <v-text-field
          v-model="form.holidays"
          label="Szabadnapok száma egy évben"
        ></v-text-field>
        <v-text-field
          v-model="form.password"
          label="Jelszó"
          :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
          :type="show ? 'text' : 'password'"
          @click:append="show = !show"
          name="password"
          autocomplete="password"
          counter
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
  name: "NewUserDialog",
  props: ["user"],
  data: () => ({
    form: {},
    rules: {
      email: (value) => /.+@.+\..+/.test(value) || "Érvénytelen e-mail",
      required: (value) => !!value || "Kötelező mező.",
      min8: (value) => (value && value.length >= 8) || "Minimum 8 karakter",
    },
    show: true,
  }),
  computed: {
    ...mapState("dialog", ["userNewDialogData"]),

    emptyForm() {
      return {
        email: "",
        fullname: "",
        holidays: 0,
        saturday: false,
        password: "",
        isAdmin: false,
      };
    },
  },
  mounted() {
    this.form = Object.assign({}, this.emptyForm);
  },

  methods: {
    confirm() {
      this.$store.commit("dialog/closeDialog");
      this.$emit("confirm", this.form);
      this.form = Object.assign({}, this.emptyForm);
    },
    cancel() {
      this.form = Object.assign({}, this.emptyForm);
      this.$store.commit("dialog/closeDialog");
      this.$emit("cancel");
    },
  },
};
</script>

<style>
</style>
