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

        <v-list-item v-if="mini" class="mb-2 px-0">

<!--          <v-list-item-icon>-->
<!--            <v-icon>mdi-chevron-right</v-icon>-->
<!--          </v-list-item-icon>-->

          <v-list-item-action>
            <v-btn icon @click.stop="mini = !mini">
              <v-icon>mdi-chevron-right</v-icon>
            </v-btn>
          </v-list-item-action>

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

        <v-list-item @click="awsSessionTokenDialogOpen = true">
          <v-list-item-icon>
            <v-icon>mdi-key-variant</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>AWS Session token</v-list-item-title>
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
      <v-sheet class="ma-0 pa-3" color="neutral" min-height="100%">
        <router-view></router-view>
      </v-sheet>

    </v-main>

    <snackbar ref="snackbar"></snackbar>

    <awsSessionTokenDialog
        :dialog.sync="awsSessionTokenDialogOpen"
        @close="awsSessionTokenDialogOpen = false"
    ></awsSessionTokenDialog>

  </v-app>

</template>

<script>

import snackbar from "@/components/SnackBar.vue";
import awsSessionTokenDialog from "@/components/AwsSessionTokenDialog.vue";

export default {

  components: {snackbar, awsSessionTokenDialog},

  name: 'App',

  data() {
    return {
      items: [
        {title: 'Home', icon: 'mdi-bank', route: '/'},
        {title: 'Workflows', icon: 'mdi-sitemap-outline', route: '/workflows'},
        // {title: 'Functions', icon: 'mdi-lambda', route: '/functions'},
        // {title: 'Users', icon: 'mdi-account-multiple', route: '/users'}, // TODO: Should only be a admin view?
        {title: 'Profile', icon: 'mdi-card-account-details-outline', route: '/profile'},
      ],
      right: null,
      awsSessionTokenDialogOpen : false
    }
  },

  mounted() {
    this.$root.snackbar = this.$refs.snackbar;
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
