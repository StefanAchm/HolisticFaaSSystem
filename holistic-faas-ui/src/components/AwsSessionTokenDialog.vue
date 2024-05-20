<template>

  <v-dialog
      v-model="awsSessionTokenDialogOpen"
      max-width="800px">

    <v-card>
      <v-card-title>
        <span class="headline">AWS Session token</span>
      </v-card-title>

      <v-card-text>

        <v-row>
          <v-col cols="12" v-if="store.state.awsSessionToken !== ''">
            <strong>Current Token:</strong> {{ store.state.awsSessionToken }}
          </v-col>
        </v-row>

        <v-row>
          <v-col cols="12">
            <v-text-field
                v-model="awsSessionToken"
                label="New AWS Session token"
                outlined
                clearable
                hide-details
            />
          </v-col>
        </v-row>
      </v-card-text>

      <v-card-actions>
        <v-spacer></v-spacer>

        <v-btn color="neutral" @click="close">Close</v-btn>

        <v-btn color="primary" @click="save" v-if="store.state.awsSessionToken === ''">
          Save
        </v-btn>

        <v-btn color="primary" @click="save" v-else>
          Update
        </v-btn>

        <v-btn
            v-if="store.state.awsSessionToken !== ''"
            color="error"
            @click="deleteToken">Delete</v-btn>

      </v-card-actions>

    </v-card>

  </v-dialog>

</template>

<script>

export default {
  props: {
    dialog: Boolean
  },
  data() {
    return {
      awsSessionToken: ''
    }
  },
  computed: {
    awsSessionTokenDialogOpen: {
      get() {
        return this.dialog
      },
      set(value) {
        this.$emit('update:dialog', value)
      }
    },
    store() {
      return this.$store;
    },
  },
  methods: {
    close() {
      this.awsSessionToken = ''
      this.awsSessionTokenDialogOpen = false
      this.$emit('dialog-closed')
    },
    save() {
      this.$store.commit('setAwsSessionToken', this.awsSessionToken)
    },
    deleteToken() {
      this.$store.commit('setAwsSessionToken', '')
    }
  }
}

</script>
