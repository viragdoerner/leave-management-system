<template>
  <v-container class="d-flex justify-center pb-10">
    <v-data-table :headers="headers" :items="users" class="elevation-1 col-12">
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>Munkatársak kezelése</v-toolbar-title>
          <v-divider class="mx-4" inset vertical></v-divider>
          <v-spacer></v-spacer>
          <v-btn color="green" dark class="mb-2" @click="newUserDialog()">
            Új munkatárs
          </v-btn>
          <confirm-dialog v-on:confirm="deleteUser"></confirm-dialog>
          <edit-user-dialog
            v-on:confirm="editUser"
            :user="userToEdit"
          ></edit-user-dialog>
          <new-user-dialog v-on:confirm="addUser"></new-user-dialog>
          <update-password-dialog
            v-on:confirm="updateUserPassword"
          ></update-password-dialog>
        </v-toolbar>
      </template>
      <template v-slot:[`item.actions`]="{ item }">
        <v-icon small @click="confirmDeleteDialog(item)"> mdi-delete </v-icon>
        <v-icon small @click="editUserDialog(item)"> mdi-pencil </v-icon>
        <v-icon small @click="updatePasswordDialog(item)"> mdi-lock </v-icon>
      </template>
      <template v-slot:no-data>
        <v-btn color="primary" @click="initialize"> Reset </v-btn>
      </template>
    </v-data-table>
  </v-container>
</template>

<script>
import ApiService from "../services/api.service";
import ConfirmDialog from "../components/dialog/ConfirmDialog.vue";
import UpdatePasswordDialog from "../components/dialog/UpdatePasswordDialog.vue";
import EditUserDialog from "../components/dialog/EditUserDialog.vue";
import NewUserDialog from "../components/dialog/NewUserDialog.vue";
export default {
  name: "UserManagementDashboard",

  components: {
    ConfirmDialog,
    UpdatePasswordDialog,
    EditUserDialog,
    NewUserDialog,
  },
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
    userToEdit: {},
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
      this.$store.commit("dialog/openConfirmDialog", {
        title: "Biztosan törölni szeretnéd?",
        confirmButton: "Törlés",
      });
    },
    updatePasswordDialog(item) {
      this.userToEdit = item;
      this.$store.commit("dialog/openUpdatePasswordDialog");
    },
    editUserDialog(item) {
      this.userToEdit = item;
      this.$store.commit("dialog/openEditUserDialog", {
        title: "Munkatárs adatainak módosítása",
      });
    },
    newUserDialog() {
      this.$store.commit("dialog/openNewUserDialog", {
        title: "Munkatárs adatainak módosítása",
      });
    },
    addUser(user) {
      ApiService.POST("user/create", user)
        .then((response) => {
          this.users.push(response.data);
          this.$store.commit("showMessage", {
            active: true,
            color: "success",
            message: "Sikeres mentés",
          });
        })
        .catch((error) => {
          this.$store.commit("showMessage", {
            active: true,
            color: "error",
            message: "Sikertelen mentés: " + error.response.data,
          });
        });
    },
    editUser(user) {
      ApiService.PUT("user/any", user)
        .then((response) => {
          console.log("success");
          this.userToEdit = user;
          this.$store.commit("showMessage", {
            active: true,
            color: "success",
            message: "Sikeres módosítás",
          });
        })
        .catch((error) => {
          this.$store.commit("showMessage", {
            active: true,
            color: "error",
            message: "Sikertelen mentés: " + error.message,
          });
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
    updateUserPassword(payload) {
      ApiService.PUT(
        "user/password/any?old=" +
          payload.old +
          "&new=" +
          payload.new +
          "&id=" +
          this.userToEdit.id
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
    this.initialize();
  },
};
</script>
