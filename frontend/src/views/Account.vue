<template>
  <v-container>
    <v-card>
      <v-card-title>Felhasználói fiók adatai</v-card-title>
      <v-card-text>
        <v-simple-table>
              <template v-slot:default>
                <tbody>
                  <tr>
                    <td>Teljes név</td>
                    <td>{{ user.fullname }}</td>
                  </tr>
                  <tr>
                    <td>Email</td>
                    <td>{{ user.email }}</td>
                  </tr>
                  <tr>
                    <td>Szabadnapok száma</td>
                    <td>{{ user.holidays }}</td>
                  </tr>
                  <tr>
                    <td>Szombat</td>
                    <td>{{ user.saturday }}</td>
                  </tr>
                  <tr>
                    <td>Jelszó lecserélése</td>
                    <td><v-btn color="success" small @click="updatePasswordDialog(item)">Jelszó csere</v-btn></td>
                  </tr>
                </tbody>
              </template>
            </v-simple-table>
            <update-password-dialog
            v-on:confirm="updateUserPassword"
          ></update-password-dialog>
      </v-card-text>
    </v-card>
    
  </v-container>
</template>

<script>
import ApiService from "../services/api.service";
import UpdatePasswordDialog from "../components/dialog/UpdatePasswordDialog.vue";
export default {
  name: "Account",

  components: {UpdatePasswordDialog},
  data: () => ({
    user: {}
   
  }),
  methods: {
    getCurrentUser() {
      ApiService.GET("user/current")
        .then((response) => {
          this.user = response.data;
        })
        .catch((error) => {
          this.$store.commit("showMessage", {
            active: true,
            color: "error",
            message: "Nem sikerül lekérni az adatokat",
          });
        });
    },
    updatePasswordDialog(item) {
      this.userToEdit = item;
      this.$store.commit("dialog/openUpdatePasswordDialog");
    },
    updateUserPassword(payload) {
      ApiService.PUT(
        "user/password/?old=" +
          payload.old +
          "&new=" +
          payload.new
      )
        .then((response) => {
          this.$store.commit("showMessage", {
            active: true,
            color: "success",
            message: "Sikeres jelszómódosítás",
          });
        })
        .catch((error) => {
          this.$store.commit("showMessage", {
            active: true,
            color: "error",
            message:
              "Nem sikerült lecserélni a jelszót: " + error.response.data,
          });
        });
    },
  },
  mounted() {
    this.getCurrentUser();
    
  },
};
</script>
