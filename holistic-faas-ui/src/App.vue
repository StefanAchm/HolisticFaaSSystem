<template>

  <v-app>

    <!--    <v-app-bar-->
    <!--        app-->
    <!--    >-->

    <!--&lt;!&ndash;      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>&ndash;&gt;-->

    <!--&lt;!&ndash;      <v-toolbar-title></v-toolbar-title>&ndash;&gt;-->
    <!--      -->
    <!--    </v-app-bar>-->

    <v-navigation-drawer
        app
        permanent
        v-if="store.getters.isAuthenticated"
    >

<!--      <template v-slot:append>-->
<!--        <v-list-item class="px-2">-->

<!--          <v-btn-->
<!--              icon-->
<!--              @click.stop="mini = !mini"-->
<!--          >-->
<!--            <v-icon v-if="!mini">mdi-chevron-left</v-icon>-->
<!--            <v-icon v-if="mini">mdi-chevron-right</v-icon>-->

<!--          </v-btn>-->

<!--        </v-list-item>-->

<!--      </template>-->

      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="text-h6">
            Holistic FaaS
          </v-list-item-title>
          <v-list-item-subtitle>
            Logged in as {{ store.state.username }}
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-divider></v-divider>

      <v-list
          dense
          nav
      >
        <v-list-item
            v-for="item in items"
            :key="item.title"
            link
            @click="navigateTo(item.route)"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>

      <template v-slot:append>
        <div class="pa-2">
          <v-btn block @click="logout()">
            Logout
          </v-btn>
        </div>
      </template>

    </v-navigation-drawer>


    <!-- Sizes your content based upon application components -->
    <v-main>

      <!-- Provides the application the proper gutter -->
      <v-container fluid>
        <router-view></router-view>
      </v-container>

    </v-main>

    <v-footer app>
      <!-- -->
    </v-footer>

  </v-app>

</template>

<script>

export default {

  name: 'App',

  data() {
    return {
      items: [
        {title: 'Home', icon: 'mdi-bank', route: '/'},
        {title: 'Functions', icon: 'mdi-lambda', route: '/functionsV3'},
        {title: 'Users', icon: 'mdi-account-multiple', route: '/users'},
      ],
      right: null,
      mini: true,
    }
  },

  methods: {
    navigateTo(route) {

      this.$router.push({path: route}).catch(() => {})

    },

    logout() {
      this.$store.dispatch('logout')
          .then(() => {
            this.$router.push({name: 'login'}).catch(() => {})
          });
    }

  },

  computed: {

    store() {
      return this.$store;
    },

    // username() {
    //   return this.$store.state.username;
    // },

  }

};
</script>
