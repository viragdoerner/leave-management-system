import Vue from 'vue'
import Vuex from 'vuex'
import { auth } from './auth.module';

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    snackbar: {},
  },
  mutations: {
    showMessage(state, payload) {
      state.snackbar = payload;
    },

  },
  getters: {
    isAdmin: state => {
      if (auth.state.user && auth.state.user.authorities) {
        return auth.state.user.authorities.length > 1;
      }
      return false;
    },
    getCurrentEmail: state => {
      return state.auth.user ? state.auth.user.username : "";
    },
    loggedIn: state => {
      if (auth.state.status && auth.state.status.loggedIn) {
        return auth.state.status.loggedIn;
      }
      return false;
    },
  },
  actions: {
  },
  modules: {
    auth
  }
})
