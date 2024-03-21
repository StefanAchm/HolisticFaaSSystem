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
            <v-btn color="blue darken-1" text @click="$router.push({ path: '/functionsV2'})">Explore</v-btn>
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
            <v-btn color="blue darken-1" text @click="$router.push({ path: '/users'})">Explore</v-btn>
          </v-card-actions>

        </v-card>

      </v-col>


    </v-row>

  </v-container>

  <!--    <v-card-->
  <!--        class="mx-auto"-->
  <!--        max-width="1200"-->
  <!--    >-->

  <!--      <v-card-title>-->
  <!--        <span class="text-h5">Welcome to Holistic FaaS</span>-->
  <!--      </v-card-title>-->

  <!--      <v-card-text>-->
  <!--        <v-container>-->
  <!--          <v-row>-->
  <!--            <v-col>-->
  <!--              <v-img-->
  <!--                  src="https://cdn.pixabay.com/photo/2017/08/30/01/05/milky-way-2695569_960_720.jpg"-->
  <!--                  aspect-ratio="1"-->
  <!--              ></v-img>-->
  <!--            </v-col>-->
  <!--            <v-col>-->
  <!--              <v-card-title>-->
  <!--                <span class="text-h6">What is Holistic FaaS?</span>-->
  <!--              </v-card-title>-->
  <!--              <v-card-text>-->
  <!--                <span class="text-body-1">-->
  <!--                  Holistic FaaS is a serverless platform that allows you to deploy and manage your functions in a simple and efficient way.-->
  <!--                </span>-->
  <!--              </v-card-text>-->
  <!--            </v-col>-->
  <!--          </v-row>-->
  <!--        </v-container>-->
  <!--      </v-card-text>-->


</template>

<script>
export default {
  name: "HomeView.vue"
}
</script>

<style scoped>

</style>