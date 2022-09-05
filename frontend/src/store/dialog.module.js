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
        }
    },
    mutations: {
        closeDialog(state) {
            state.confirmDialogData.isOpen = false;
            state.updatePasswordDialogData.isOpen = false;
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
    }

};