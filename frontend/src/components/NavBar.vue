<template>
  <div><v-app-bar
      color="deep-purple"
      dark
    >
      <v-app-bar-nav-icon @click="drawer = true"></v-app-bar-nav-icon>

      <v-toolbar-title>Szabadságkezelő rendszerjjj {{$store.getters.loggedIn}}</v-toolbar-title>
      <v-spacer></v-spacer>
      

      <v-menu bottom left v-if="$store.getters.loggedIn">
        <template v-slot:activator="{ on, attrs }">
          <v-btn icon v-bind="attrs" v-on="on">
            <v-icon>mdi-account</v-icon>
          </v-btn>
        </template>
        <v-list>
          <v-list-item >
            <v-btn text @click="logout"> Kijelentkezés </v-btn>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-app-bar>

    <v-navigation-drawer
      v-model="drawer"
      absolute
      temporary
    >
      <v-list
        nav
        dense
      >
        <v-list-item-group
          v-model="group"
          active-class="deep-purple--text text--accent-4"
        >
          <v-list-item to="/">
            <v-list-item-icon>
              <v-icon>mdi-home</v-icon>
            </v-list-item-icon>
            <v-list-item-title>Dashboard</v-list-item-title>
          </v-list-item>

           <v-list-item v-if="!$store.getters.loggedIn" to="/login">
            <v-list-item-icon>
              <v-icon>mdi-account</v-icon>
            </v-list-item-icon>
            <v-list-item-title>Bejelentkezés</v-list-item-title>
          </v-list-item>
          <v-list-item v-if="$store.getters.loggedIn" @click="logout">
            <v-list-item-icon>
              <v-icon>mdi-account-outline</v-icon>
            </v-list-item-icon>
            <v-list-item-title>Kijelentkezés</v-list-item-title>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </v-navigation-drawer></div>
</template>

<script>
  export default {
    name: 'NavBar',

    data: () => ({
      drawer: false,
      group: null,
      
    }),
  }
</script>
