export const dialog = {
    namespaced: true,
    state: {
        confirmDialogData: {
            isOpen: false,
            title: "",
            confirmButton: "OK",
            confirmButtonColor: "success",
        },
        updatePasswordDialogData:{
            isOpen: false
        },
        userDialogData: {
            isOpen: false,
            title: "",
            edit: true
        },
    },
    mutations: {
        closeDialog(state) {
            state.confirmDialogData.isOpen = false;
            state.updatePasswordDialogData.isOpen = false;
            state.userDialogData.isOpen = false;
        },
        openConfirmDialog(state, payload) {
            state.confirmDialogData.title = payload.title || "";
            state.confirmDialogData.confirmButtonColor = payload.confirmButtonColor || "success";
            state.confirmDialogData.confirmButton = payload.confirmButton || "OK";
            state.confirmDialogData.isOpen = true;
        },
        openUpdatePasswordDialog(state) {
            state.updatePasswordDialogData.isOpen = true;
        },
        openUserDialog(state, payload) {
            state.userDialogData.title = payload.title || "";
            state.userDialogData.isOpen = true;
            state.userDialogData.edit = payload.edit;
        },
    }

};