<template>
  <v-container>
    <v-card>
      <v-card-title>Szabadságok</v-card-title>
      <v-card-text>
        <v-data-table
          :headers="headers"
          :items="leave_requests"
          class="elevation-1"
        ></v-data-table>
      </v-card-text>
    </v-card>
    <v-card class="mt-10">
      <v-card-title>Új hozzáadása</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" sm="6" md="3">
            <v-text-field label="Első nap" v-model="startDate"></v-text-field>
          </v-col>
          <v-col cols="12" sm="6" md="3">
            <v-text-field label="Utolsó nap" v-model="endDate"></v-text-field>
          </v-col>
          <v-btn @click="saveLeaveRequest()">OK</v-btn>
        </v-row>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import axios from "axios";
export default {
  name: "UserDashboard",

  components: {},
  data: () => ({
    headers: [
      {
        text: "Első nap",
        value: "startDate",
      },
      { text: "Utolsó nap", value: "endDate" },
    ],
    leave_requests: [],
    startDate: "",
    endDate: "",
  }),
  methods: {
    saveLeaveRequest() {
      axios
      .post("https://leave-management-backend.herokuapp.com/leave-request", {startDate: this.startDate, endDate: this.endDate})
      .then((response) => {
        this.leave_requests.push(response.data);
        this.$store.commit("showMessage", {
          active: true,
          color: "success", // You can create another actions for diferent color.
          message: "Sikeres mentés",
        });
      })
      .catch((error) => {
        this.$store.commit("showMessage", {
          active: true,
          color: "error", // You can create another actions for diferent color.
          message: "Sikertelen mentés: "+ error,
        });
      });
    },
  },
  mounted() {
    axios
      .get("https://leave-management-backend.herokuapp.com/leave-request")
      .then((response) => {
        this.leave_requests = response.data;
      })
      .catch((error) => {
        this.$store.commit("showMessage", {
          active: true,
          color: "error", // You can create another actions for diferent color.
          message: "Hiba történt: " + error,
        });
      });
  },
};
</script>
