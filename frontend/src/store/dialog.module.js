export const dialog = {
    namespaced: true,
    state: {
        dialogData: {
            isOpen: false,
            title: "",
            text: "",
            confirmButton: "OK",
            confirmButtonColor: "success",
            hasForm: false,
            form: {
                textfieldLabel: "",
                textfieldRequired: true,
                dropdownLabel: "",
                dropdownItems: [],
            },
        }
    },
    mutations: {
        closeDialog(state) {
            state.dialogData.isOpen = false;
        },
        openSimpleDialog(state, payload) {
            state.dialogData.title = payload.title || "";
            state.dialogData.text = payload.text || "";
            state.dialogData.confirmButtonColor = payload.confirmButtonColor || "success";
            state.dialogData.confirmButton = payload.confirmButton || "OK";
            state.dialogData.hasForm = false;
            state.dialogData.confirmButton = payload.confirmButton;
            state.dialogData.isOpen = true;
        },
        openDialogWithForm(state, payload) {
            state.dialogData.title = payload.title || "";
            state.dialogData.text = payload.text || "";
            state.dialogData.confirmButtonColor = payload.confirmButtonColor || "success";
            state.dialogData.confirmButton = payload.confirmButton || "OK";
            state.dialogData.confirmButton = payload.confirmButton;

            state.dialogData.hasForm = true;
            state.dialogData.form.textfieldLabel = payload.form.textfieldLabel || "";
            state.dialogData.form.textfieldRequired = payload.form.textfieldRequired || "";
            state.dialogData.form.dropdownLabel = payload.form.dropdownLabel || "";
            state.dialogData.form.dropdownItems = payload.form.dropdownItems || "";
            state.dialogData.isOpen = true;
        },
    }

};