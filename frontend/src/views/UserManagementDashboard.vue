<template>
  <v-container>
    <v-card>
      <v-card-title>Felhasználók kezelése</v-card-title>
      <v-card-text>
        <v-container class="d-flex justify-center pb-10">
          <v-data-table
            :headers="headers"
            :items="users"
            class="elevation-1 col-12"
          >
            <template v-slot:top>
              <v-toolbar flat>
                <v-toolbar-title>Felhasználók</v-toolbar-title>
                <v-divider class="mx-4" inset vertical></v-divider>
                <v-spacer></v-spacer>
                <confirm-dialog v-on:confirm="deleteUser"></confirm-dialog>
              </v-toolbar>
            </template>
            <template v-slot:[`item.actions`]="{ item }">
              <v-icon small @click="confirmDeleteDialog(item)">
                mdi-delete
              </v-icon>
            </template>
            <template v-slot:no-data>
              <v-btn color="primary" @click="initialize"> Reset </v-btn>
            </template>
          </v-data-table>
        </v-container>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import ApiService from "../services/api.service";
import ConfirmDialog from "../components/ConfirmDialog.vue";
export default {
  name: "UserManagementDashboard",

  components: { ConfirmDialog },
  data: () => ({
    headers: [
      { text: "Email", value: "email" },
      {
        text: "Név",
        align: "start",
        value: "fullname",
      },
      { text: "Szabadnapok száma egy évben", value: "holidays" },
      { text: "Szombat", sortable: false, value: "saturday" },
      { text: "", value: "actions", sortable: false },
    ],
    users: [],
    userToDelete: {
      id: null,
      email: "",
      fullname: 0,
      holidays: 0,
      saturday: 0,
    },
  }),
  methods: {
    initialize() {
      ApiService.GET("user")
        .then((response) => {
          this.users = response.data;
        })
        .catch((error) => {
          this.$store.commit("showMessage", {
            active: true,
            color: "error", // You can create another actions for diferent color.
            message: "Hiba történt a felhasználók adatainak lekérésénél",
          });
        });
    },
    confirmDeleteDialog(item) {
      this.userToDelete = Object.assign({}, item);
      this.$store.commit("dialog/openSimpleDialog", {
        title: "Biztosan törölni szeretnéd?",
        confirmButton: "Törlés",
      });
    },

    deleteUser() {
      var that = this;
      ApiService.DELETE("user/" + this.userToDelete.id)
        .then((response) => {
          this.users = this.users.filter(function (u) {
            return u.id !== that.userToDelete.id;
          });
          this.$store.commit("showMessage", {
            active: true,
            color: "success", 
            message: "Sikeres törlés",
          });
        })
        .catch((error) => {
          this.$store.commit("showMessage", {
            active: true,
            color: "error",
            message: "Nem sikerült törölni a felhasználót",
          });
        });
    },
  },
  mounted() {
    this.initialize();
  },
};
</script>
