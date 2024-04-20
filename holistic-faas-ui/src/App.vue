<template>

  <v-app>

    <v-navigation-drawer
        app
        permanent
        :mini-variant="mini"
        v-if="store.getters.isAuthenticated"
        color="primary"
        dark
    >

      <v-list
          dense
          nav
      >

        <v-list-item v-if="mini" @click.stop="mini = !mini" class="mb-7">

          <v-list-item-icon>
            <v-icon>mdi-menu</v-icon>
          </v-list-item-icon>

        </v-list-item>

        <v-list-item v-if="!mini" class="mb-2">

          <v-list-item-content>

            <v-list-item-title class="text-h6">
              Holistic FaaS
            </v-list-item-title>

            <v-list-item-subtitle>
              Logged in as {{ store.state.username }}
            </v-list-item-subtitle>

          </v-list-item-content>

          <v-list-item-action>
            <v-btn icon @click.stop="mini = !mini">
              <v-icon>mdi-chevron-left</v-icon>
            </v-btn>
          </v-list-item-action>


        </v-list-item>

        <v-list-item
            v-for="item in items"
            :key="item.title"
            link
            @click="navigateTo(item.route)"
            :to="item.route"
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

        <v-list dense nav>

          <v-list-item v-if="!mini">

            <v-list-item-content>

              <div class="pa-2">

                <v-btn width="100%" text @click="logout()" v-if="!mini">

                  Logout
                  <v-icon right>
                    mdi-logout
                  </v-icon>
                </v-btn>

              </div>

            </v-list-item-content>

          </v-list-item>

          <v-list-item v-if="mini" @click="logout()">


            <v-list-item-icon>
              <v-icon>mdi-logout</v-icon>
            </v-list-item-icon>

          </v-list-item>

        </v-list>

      </template>

    </v-navigation-drawer>

    <!-- Sizes your content based upon application components -->
    <v-main>

      <!-- Provides the application the proper gutter -->
      <v-container class="pt-6">
        <router-view></router-view>
      </v-container>

    </v-main>

  </v-app>

</template>

<script>

export default {

  name: 'App',

  data() {
    return {
      items: [
        {title: 'Home', icon: 'mdi-bank', route: '/'},
        {title: 'Functions', icon: 'mdi-lambda', route: '/functions'},
        {title: 'Users', icon: 'mdi-account-multiple', route: '/users'},
        {title: 'Profile', icon: 'mdi-card-account-details-outline', route: '/profile'},
      ],
      right: null,
    }
  },

  methods: {
    navigateTo(route) {
      this.$router.push({path: route}).catch(() => {
      })
    },

    logout() {
      this.$store.dispatch('logout')
          .then(() => {
            this.$router.push({name: 'login'}).catch(() => {
            })
          });
    }

  },

  computed: {

    store() {
      return this.$store;
    },

    mini: {
      get() {
        return this.$store.state.miniVariant;
      },
      set(value) {
        this.$store.commit('setMiniVariant', value);
      }
    }

  }

};
</script>
