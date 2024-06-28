<template>

  <v-card
      class="mx-auto"
      width="700px"
      max-width="70%">

    <v-card-title>
      <span class="text-h4">Profile</span>

      <v-spacer></v-spacer>

      <v-icon
          color="info"
          @click="openHelp">
        mdi-help-circle-outline
      </v-icon>


    </v-card-title>

    <v-spacer></v-spacer>

    <v-card-subtitle>
      <span class="text-h6"><strong>Username: </strong> {{ username }}</span>
    </v-card-subtitle>

    <v-divider></v-divider>

    <v-card-text :key="cardKey">

      <v-list v-for="(credentials) in credentialsList" :key="credentials.name">

        <v-row class="py-3">

          <v-col :cols="3" class="align-self-center">
            <strong>Credentials for {{ credentials.provider }}:</strong>
          </v-col>

          <v-col class="align-self-center" :class="{
            'info--text': credentials.fileName,
            }">
            
            <span v-if="credentials.fileName">
              {{ credentials.fileName }}
              <v-icon color="info" small>mdi-alert-circle-outline</v-icon>
            </span>
            
            <span v-else-if="credentials.configured">
              Configured
              <v-icon color="success" small>mdi-check-circle-outline</v-icon>
            </span>

            <span v-else>
              Not configured
              <v-icon color="error" small>mdi-alert-circle-outline</v-icon>
            </span>

          </v-col>

          <v-col :cols="1">

            <v-file-input
                class="pt-0 mt-0"
                hide-input
                @change="selectFile($event, credentials)"
            ></v-file-input>

          </v-col>

        </v-row>

      </v-list>

    </v-card-text>

    <v-card-actions>

      <v-spacer></v-spacer>

      <v-btn :disabled="nrOfCredentials === 0" color="error" @click="deleteUserCredentials()">Delete</v-btn>

      <v-btn :disabled="!hasChanged" color="neutral" @click="init()">Cancel</v-btn>
      <v-btn :disabled="!hasChanged" color="primary" @click="save()">Save</v-btn>

    </v-card-actions>

  </v-card>


</template>

<script>

import HfApi from "@/utils/hf-api";

export default {

  data() {

    return {

      username: this.$store.state.username,

      defaultCredentials: {
        configured: false,
        provider: null,
        fileName: null,
        inputFile: null
      },

      credentialsList: [],

      cardKey: 0,

      nrOfCredentials: 0

    }

  },

  created() {
    this.init()
  },

  computed: {
    hasChanged() {
      return this.credentialsList.some(credentials => credentials.inputFile)
    }
  },

  methods: {

    init() {

      let awsAccount = Object.assign({}, this.defaultCredentials)
      awsAccount.provider = 'AWS'

      let gcpAccount = Object.assign({}, this.defaultCredentials)
      gcpAccount.provider = 'GCP'

      this.credentialsList = [awsAccount, gcpAccount]

      HfApi.getUserCredentials(this.$store.getters.getUser)
          .then(response => {

            for (let i = 0; i < response.data.length; i++) {

              let currentData = response.data[i];
              let currentAccount = {
                provider: currentData.provider,
                configured: true,
                fileName: null,
                inputFile: null
              }

              // Update the corresponding account in the list with the new data
              this.credentialsList = this.credentialsList.map(account => {
                if (account.provider === currentAccount.provider) {
                  return currentAccount
                }
                return account
              })

            }

            this.nrOfCredentials = response.data.length

            this.cardKey++

          })

    },

    selectFile(file, credentials) {
      credentials.fileName = file.name
      credentials.inputFile = file
    },

    deleteUserCredentials() {
      HfApi.deleteUserCredentials(this.$store.state.userId)
          .then(() => {
            this.init()
            this.$root.snackbar.showWarning({message: 'Credentials deleted successfully'});
          })
    },

    save() {

      Promise.all(
          this.credentialsList
              .filter(credentials => credentials.inputFile)
              .map(credentials => this.upload(credentials))
      ).then(() => {
        this.init()
      })

    },

    upload(credentials) {

      let request = {
        provider: credentials.provider,
        userId: this.$store.state.userId
      };

      return HfApi.uploadUserCredentials(credentials.inputFile, request)
          .then(() => {
            this.$root.snackbar.showSuccess({message: 'Credentials uploaded successfully'});
          })
          .catch(() => {
            this.$root.snackbar.showError({message: 'Error uploading credentials for ' + credentials.provider});
          })

    },

    openHelp() {
      window.open('https://github.com/StefanAchm/HolisticFaaSSystem/blob/main/doc/GettingStarted.md#1-configure-your-user-account')
    }

  }


}
</script>