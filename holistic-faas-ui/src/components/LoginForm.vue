<template>


  <v-container style="width: 400px">

    <v-layout
        align-center
        justify-center
    >

      <v-flex>

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
                  v-if="error"
                  type="error"
                  class="my-5"
              >
                {{ error }}
              </v-alert>


              <v-btn
                  width="100%"
                  color="primary"
                  type="submit"
                  class="my-5"
              >
                Login
              </v-btn>

              <div class="line-separator">
                <hr>
                <span>or</span>
                <hr>
              </div>

              <v-btn
                  width="100%"
                  color="neutral"
                  class="my-5"
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

              <v-alert
                  dense
                  v-if="error"
                  type="error"
                  class="my-5"
              >
                {{ error }}
              </v-alert>

              <v-btn
                  width="100%"
                  color="primary"
                  type="submit"
                  class="my-5"
              >
                Register
              </v-btn>

              <div class="line-separator">
                <hr>
                <span>or</span>
                <hr>
              </div>

              <v-btn
                  width="100%"
                  color="neutral"
                  @click="changeForm()"
                  class="my-5"
              >
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
      this.error = null;
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
            this.$root.snackbar.showSuccess({message: 'User registered successfully'});
            this.changeForm();
          })
          .catch((err) => {
            this.error = err.response.data;
          })
    },

    getCardTitle() {
      return this.registerForm ? 'Register' : 'Sign in';
    },

    changeForm() {
      this.init();
      this.registerForm = !this.registerForm;
    },

  },


};
</script>

<style scoped>
.line-separator {
  display: flex;
  align-items: center;
  text-align: center;
  color: grey;
}

.line-separator hr {
  flex-grow: 1;
  border: none;
  border-top: 1px solid grey;
}

.line-separator span {
  padding: 0 10px;
  background: white;
}
</style>