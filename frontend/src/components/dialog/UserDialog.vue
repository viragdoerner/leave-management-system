<template>
  <v-dialog
    v-model="userDialogData.isOpen"
    max-width="600"
    :retain-focus="false"
  >
    <v-card>
      <v-card-title> {{ userDialogData.title }} </v-card-title>
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
        <v-text-field
          v-show="!userDialogData.edit"
          v-model="form.password"
          label="Jelszó"
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
  name: "UserDialog",
  props: ["user"],
  data: () => ({
    form: {},
    rules: {
      email: (value) => /.+@.+\..+/.test(value) || "Érvénytelen e-mail",
      required: (value) => !!value || "Kötelező mező.",
      min6: (value) => (value && value.length >= 6) || "Minimum 6 karakter",
    },
  }),
  computed: {
    ...mapState("dialog", ["userDialogData"]),
    opened() {
      return this.userDialogData.isOpen;
    },
    emptyForm() {
      console.log("computed:");
      var form = {
        email: "",
        fullname: "",
        holidays: 0,
        saturday: false,
        password: "",
        isAdmin: false,
      };
      console.log(form);
      return form;
    },
  },
  mounted() {
    this.form = this.emptyForm;
  },
  watch: {
    // whenever question changes, this function will run
    user(newUser) {
      this.form = newUser;
    },
    opened() {
      if (this.userDialogData.edit) {
        this.form = this.user;
      } else {
        console.log("reset form");
        console.log(this.emptyForm);
        this.form = this.emptyForm;
      }
    },
  },
  methods: {
    confirm() {
      this.$store.commit("dialog/closeDialog");
      this.$emit("confirm", {
        edit: this.userDialogData.edit,
        user: this.form,
      });

      this.form = this.user;
    },
    cancel() {
      this.form = this.user;
      this.$store.commit("dialog/closeDialog");
      this.$emit("cancel");
    },
  },
};
</script>

<style>
</style>
