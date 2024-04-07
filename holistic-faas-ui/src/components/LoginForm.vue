<template>
  <v-container>
    <v-form @submit.prevent="login">
      <v-text-field v-model="user.username" label="Username" required></v-text-field>
      <v-text-field v-model="user.password" label="Password" required></v-text-field>
      <v-btn type="submit">Login</v-btn>
    </v-form>
  </v-container>
</template>

<script>

import HfApi from "@/utils/hf-api";

export default {
  data() {
    return {
      user: {}
    };
  },
  methods: {

    login() {

      HfApi.login(this.user)
          .then((response) => {

            this.$store.dispatch('login', response.data.token)
                .then(() => {
                  this.$router.push({name: 'home'});
                });

          });

    }

  },

};
</script>
