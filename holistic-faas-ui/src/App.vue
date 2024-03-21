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
            Management System
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
            link @click="$router.push({ path: item.route})"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>

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

// import TransactionsView from './components/TransactionsView.vue'

export default {
  name: 'App',

  // components: {
  //   TransactionsView
  // },

  data() {
    return {
      items: [
        {title: 'Home', icon: 'mdi-bank', route: '/'},
        // {title: 'Functions', icon: 'mdi-lambda', route: '/functions'},
        {title: 'Functions', icon: 'mdi-lambda', route: '/functionsV2'},
        // {title: 'Function Deployments', icon: 'mdi-rocket', route: '/function-deployments'},
        {title: 'Users', icon: 'mdi-account-multiple', route: '/users'},
      ],
      right: null,
      mini: true,
    }
  },

};
</script>
