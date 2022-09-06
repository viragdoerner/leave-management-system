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
        userEditDialogData: {
            isOpen: false,
            title: ""
        },
        userNewDialogData: {
            isOpen: false,
            title: ""
        },
    },
    mutations: {
        closeDialog(state) {
            state.confirmDialogData.isOpen = false;
            state.updatePasswordDialogData.isOpen = false;
            state.userNewDialogData.isOpen = false;
            state.userEditDialogData.isOpen = false;
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
        openEditUserDialog(state, payload) {
            state.userEditDialogData.title = payload.title || "";
            state.userEditDialogData.isOpen = true;
        },
        openNewUserDialog(state, payload) {
            state.userNewDialogData.title = payload.title || "";
            state.userNewDialogData.isOpen = true;
        },
    }

};