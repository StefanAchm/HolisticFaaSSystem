<template>

  <v-container fluid>

    <v-row>

      <v-col>

        <v-card>

          <v-card-title>
            <span class="text-h5">Welcome to Holistic FaaS</span>
          </v-card-title>

          <v-card-subtitle>
            A web-based platform for management of serverless applications in Federated FaaS
          </v-card-subtitle>

          <v-spacer></v-spacer>

          <v-card-text>
            Holistic FaaS is a serverless platform that allows you to deploy and manage your functions in a simple and
            efficient way.
          </v-card-text>



        </v-card>

      </v-col>


    </v-row>

    <v-row>

      <v-col>

        <v-card>

          <v-card-title>
            <span class="text-h5">Functions</span>
          </v-card-title>

          <v-card-text>
            Add and deploy Functions
          </v-card-text>

          <v-card-actions>
            <v-btn color="primary" text @click="$router.push({ path: '/functions'})">Explore</v-btn>
          </v-card-actions>

        </v-card>

      </v-col>


      <v-col>

        <v-card>

          <v-card-title>
            <span class="text-h5">Users</span>
          </v-card-title>

          <v-card-text>
            Add user credentials
          </v-card-text>

          <v-card-actions>
            <v-btn color="primary" text @click="$router.push({ path: '/users'})">Explore</v-btn>
          </v-card-actions>

        </v-card>

      </v-col>


    </v-row>

  </v-container>

</template>

<script>

export default {
  name: "HomeView.vue",
}
</script>

<style scoped>

</style>