<template>
  <v-dialog
    v-model="dialogData.isOpen"
    :max-width="dialogData.hasForm ? 600 : 290"
    :retain-focus="false"
  >
    <v-card>
      <v-card-title> {{ dialogData.title }} </v-card-title>
      <v-card-text v-if="dialogData.hasForm || dialogData.text">
        <p>{{ dialogData.text }}</p>
        <v-combobox
          v-if="dialogData.form && dialogData.form.dropdownLabel"
          class="my-0"
          v-model="form.newStatus"
          :items="dialogData.form.dropdownItems"
          :label="dialogData.form.dropdownLabel"
          solo
          item-text="name"
          color="cgreen"
        ></v-combobox>
        <v-textarea
          v-if="dialogData.hasForm"
          v-model="form.comment"
          :label="dialogData.form.textfieldLabel"
          solo
        ></v-textarea>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="cgreen darken-1" text @click="cancel()"> Mégse </v-btn>
        <v-btn
          :color="dialogData.confirmButtonColor"
          text
          @click="confirm()"
          :disabled="isButtonDisabled"
        >
          {{ dialogData.confirmButton }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "CConfirmDialog",
  props: [],
  data: () => ({
    form: {
      comment: "",
      newStatus: "",
    }
  }),
  computed: {
    ...mapState('dialog',["dialogData"]),
    isButtonDisabled() {
      return (
        (!!this.dialogData.hasForm &&
          this.dialogData.form.textfieldRequired &&
          !this.form.comment) ||
        (!!this.dialogData.hasForm &&
          this.dialogData.form.dropdownItems.length > 0 &&
          !this.form.newStatus)
      );
    },
  },
  mounted() {
  },
  methods: {
    confirm() {
      this.$store.commit("dialog/closeDialog");
      if (!!this.dialogData.form) {
        this.form.newStatus = this.form.newStatus.status;
        this.$emit("confirm", this.form);
      } else {
        this.$emit("confirm");
      }
      this.form.comment = "";
      this.form.newStatus = "";
    },
    cancel() {
      this.form.comment = "";
      this.form.newStatus = "";
      this.$store.commit("dialog/closeDialog");
      this.$emit("cancel");
    },
  },
};
</script>

<style>
</style>
