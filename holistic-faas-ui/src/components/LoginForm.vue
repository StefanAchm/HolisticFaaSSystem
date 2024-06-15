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

              <span class="d-flex justify-center" style="font-size: small">
                Version: {{ store.state.systemInfo?.systemVersion }}
              </span>

            </v-form>


            <v-form
                v-if="registerForm"
                @submit.prevent="register"
            >

              <v-text-field
                  v-model="user.username"
                  label="Username"
                  required
                  :rules="userRules"
              ></v-text-field>

              <v-text-field
                  :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                  :type="showPassword ? 'text' : 'password'"
                  v-model="user.password"
                  label="Password"
                  required
                  :rules="passwordRules"
                  @click:append="showPassword = !showPassword"
              ></v-text-field>

              <v-text-field
                  :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                  :type="showPassword ? 'text' : 'password'"
                  v-model="user.passwordConfirm"
                  label="Confirm Password"
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
                  :disabled="!isRegisterFormValid()"
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

              <span class="d-flex justify-center" style="font-size: small">
                Version: {{ store.state.systemInfo?.systemVersion }}
              </span>

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
      showPasswordConfirm: false,
      error: null,
      passwordRules: [],
      userRules: [],
    };
  },

  computed: {

    store() {
      return this.$store;
    },

  },

  methods: {

    init() {
      this.user = {
        username: null,
        password: null,
        passwordConfirm: null,
      };

      this.passwordRules = []
      this.userRules = []

      this.error = null;

    },

    isRegisterFormValid() {
      return this.user.username && this.user.password && this.user.passwordConfirm
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

      this.passwordRules = [
        v => !!v || 'Password is required',
        v => (v && v.length >= 8) || 'Min 8 characters',
        v => (v && v.length <= 20) || 'Max 20 characters',
        v => /[A-Z]/.test(v) || 'At least one uppercase letter',
        v => /[a-z]/.test(v) || 'At least one lowercase letter',
        v => /[0-9]/.test(v) || 'At least one number',
      ]

      this.userRules = [
        v => !!v || 'Username is required',
        v => (v && v.length >= 4) || 'Min 4 characters',
      ]

      if(this.user.password !== this.user.passwordConfirm) {
        this.error = 'Passwords do not match';
        return;
      }

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