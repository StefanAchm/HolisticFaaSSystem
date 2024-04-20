<template>


  <v-container>

    <v-layout
        align-center
        justify-center
    >

      <v-flex xs12 sm8 md4>

        <v-card class="elevation-12">

          <v-card-title>

            <span class="text-h4">{{ getCardTitle() }}</span>

          </v-card-title>

          <v-card-subtitle>
            <span class="text-h7">Manage serverless functions</span>
          </v-card-subtitle>

          <v-card-text>

            <v-form
                v-if="!registerForm"
                @submit.prevent="login"
            >
              <v-text-field v-model="user.username" label="Username" required></v-text-field>
              <v-text-field
                  :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                  :type="showPassword ? 'text' : 'password'"
                  v-model="user.password"
                  label="Password"
                  required
                  @click:append="showPassword = !showPassword"
              ></v-text-field>


              <v-alert
                  dense
                  outlined
                  v-if="error"
                  type="error"
              >
                {{ error }}
              </v-alert>


              <v-btn
                  width="100%"
                  color="primary"
                  type="submit"
                  class="my-6"
              >
                Login
              </v-btn>


              <v-spacer></v-spacer>


              <v-btn
                  width="100%"
                  x-small
                  color="neutral"
                  @click="changeForm()">
                Register here
              </v-btn>

            </v-form>


            <v-form
                v-if="registerForm"
                @submit.prevent="register"
            >

              <v-text-field v-model="user.username" label="Username" required></v-text-field>
              <v-text-field
                  :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                  :type="showPassword ? 'text' : 'password'"
                  v-model="user.password"
                  label="Password"
                  required
                  @click:append="showPassword = !showPassword"
              ></v-text-field>

              <v-btn
                  width="100%"
                  color="primary"
                  type="submit"
                  class="my-6"
              >
                Register
              </v-btn>


              <v-btn
                  width="100%"
                  x-small
                  color="neutral"
                  @click="changeForm()">
                Go to login
              </v-btn>


            </v-form>

          </v-card-text>

        </v-card>

      </v-flex>

    </v-layout>

  </v-container>

</template>

<script>

import HfApi from "@/utils/hf-api";

export default {

  data() {
    return {
      user: {},
      dialog: true,
      registerForm: false,
      showPassword: false,
      error: null,

    };
  },

  methods: {

    init() {
      this.user = {
        username: '',
        password: '',
      };
    },

    login() {

      HfApi.login(this.user)
          .then((response) => {

            this.error = null;

            this.$store.dispatch('login', response.data)
                .then(() => {
                  this.$router.push({name: 'home'});
                });

          })
          .catch(() => {
            this.error = 'Invalid credentials';
          })

      ;

    },

    register() {
      HfApi.register(this.user)
          .then(() => {
            this.changeForm();
          })
          .catch(() => {
            this.error = 'Could not register';
          })
    },

    getCardTitle() {
      return this.registerForm ? 'Register' : 'Sign in';
    },

    getButtonTitle() {
      return this.registerForm ? 'Login' : 'Register here';
    },

    changeForm() {
      this.init();
      this.registerForm = !this.registerForm;
    },

  },


};
</script>
