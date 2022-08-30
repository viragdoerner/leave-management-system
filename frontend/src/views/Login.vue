<template>
  <div>
    <v-row class="mt-5 ml-5">
      <v-btn x-large icon color="white" to="/">
        <v-icon x-large> mdi-arrow-left </v-icon>
      </v-btn>
    </v-row>
    <v-container class="d-flex align-center justify-center mt-10 ptb-5">
      <v-card persistent class="col-12 col-sm-8 col-md-6 mt-5 pa-0">
        <div>
          <v-card class="px-4">
            <v-card-text>
              <v-form ref="loginForm" v-model="valid" lazy-validation>
                <v-row>
                  <v-col cols="12">
                    <v-text-field
                      v-model="loginEmail"
                      :rules="[rules.email, rules.required]"
                      label="E-mail"
                      autocomplete="email"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12">
                    <v-text-field
                      v-model="loginPassword"
                      :append-icon="show1 ? 'eye' : 'eye-off'"
                      :rules="[rules.required, rules.min8]"
                      :type="show1 ? 'text' : 'password'"
                      name="password"
                      autocomplete="password"
                      label="Jelszó"
                      hint="Minimum 8 karakter hosszú"
                      counter
                      @click:append="show1 = !show1"
                    ></v-text-field>
                  </v-col>
                  <v-col class="d-flex" cols="12" sm="6" xsm="12"> </v-col>
                  <v-spacer></v-spacer>
                  <v-col class="d-flex" cols="12" sm="3" xsm="12" align-end>
                    <v-btn
                      x-large
                      block
                      :disabled="!valid"
                      color="success"
                      @click="validateLogin"
                    >
                      Login
                    </v-btn>
                  </v-col>
                </v-row>
              </v-form>
            </v-card-text>
          </v-card>
        </div>
        <v-overlay :absolute="absolute" :value="loading" color="cgreen">
          <v-progress-circular indeterminate size="64"></v-progress-circular>
        </v-overlay>
      </v-card>
    </v-container>
  </div>
</template>

<script>
export default {
  name: "Login",
  computed: {
  },
  methods: {
    validateLogin() {
      if (this.$refs.loginForm.validate()) {
        this.loading = true;
        var loginForm = {
          email: this.loginEmail,
          password: this.loginPassword,
        };
        this.$store
          .dispatch("auth/login", loginForm)
          .then(() => {
            this.loading = false;
            this.$router.push("/");
            this.showAlert = false;
          })
          .catch((error) => {
            this.loading = false;
            this.$store.commit("showMessage", {
              active: true,
              color: "error",
              message: "Sikertelen bejelentkezés",
            });
          });
      }
    }
  },
  data: () => ({
    absolute: true,
    loading: false,
    showAlert: false,
    valid: true,
    loginPassword: "",
    loginEmail: "",
    show1: false,
    rules: {
      email: (value) => /.+@.+\..+/.test(value) || "Érvénytelen e-mail",
      required: (value) => !!value || "Kötelező mező.",
      min8: (value) => (value && value.length >= 8) || "Minimum 8 karakter",
    },
  }),
};
</script>
