<template>

  <v-snackbar
      :color="color"
      :timeout="timer"
      v-model="showSnackbar"
      bottom
      right
  >

    <v-icon left>{{icon}}</v-icon>{{message}}

    <template v-slot:action="{ attrs }">

      <v-btn
          text
          v-bind="attrs"
          @click="showSnackbar = false"
      >
        Close
      </v-btn>

      <v-btn
          v-if="route"
          text
          v-bind="attrs"
          @click="$router.push(route)"
      >
        Show
      </v-btn>

    </template>

  </v-snackbar>

</template>

<script>
export default {
  data() {
    return{
      showSnackbar: false,
      message: '',
      color: '',
      icon: '',
      timer: 5000,
      route: ''
    }
  },
  methods:{

    showError(data) {
      this.message = data.message
      this.color = 'error'
      this.icon = 'mdi-alert'
      this.showSnackbar = true
    },

    showSuccess(data) {
      this.message = data.message
      this.color = 'success'
      this.icon = 'mdi-check'
      this.showSnackbar = true
      this.route = data.route
    },

    showWarning(data) {
      this.message = data.message
      this.color = 'warning'
      this.icon = 'mdi-alert'
      this.showSnackbar = true
    },

    show(data) {
      this.message = data.message
      this.color = data.color
      this.icon = data.icon
      this.showSnackbar = true
    }

  }

}
</script>