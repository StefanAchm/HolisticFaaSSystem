<template>

  <v-card
      class="mx-auto"
      width="700px"
      max-width="70%">

    <v-card-title>
      <span class="text-h4">Profile</span>
    </v-card-title>

    <v-spacer></v-spacer>

    <v-card-subtitle>
      <span class="text-h6"><strong>Username: </strong> {{ username }}</span>
    </v-card-subtitle>

    <v-divider></v-divider>

    <v-card-text>

      <v-list v-for="(provider) in providers" :key="provider.name">

        <v-row class="py-3">

          <v-col :cols="3" class="align-self-center">
            <strong>Credentials for {{ provider.name }}:</strong>
          </v-col>

          <v-col class="align-self-center" :class="{
            'info--text': provider.inputFile,
            }">

            {{ provider.fileName }}

            <v-icon color="info" v-if="provider.inputFile" small>mdi-alert-circle-outline</v-icon>
            <v-icon color="success" v-else-if="provider.path" small>mdi-check-circle-outline</v-icon>
            <v-icon color="error" v-else small>mdi-alert-circle-outline</v-icon>

          </v-col>

          <v-col :cols="1">

            <v-file-input
                class="pt-0 mt-0"
                hide-input
                @change="selectFile($event, provider)"
            ></v-file-input>

          </v-col>

        </v-row>

      </v-list>

    </v-card-text>

    <v-card-actions>

      <v-spacer></v-spacer>

      <v-btn :disabled="!hasChanged()" color="neutral" @click="init()">Cancel</v-btn>
      <v-btn :disabled="!hasChanged()" color="primary" @click="save">Save</v-btn>

    </v-card-actions>

  </v-card>


</template>

<script>

import HfApi from "@/utils/hf-api";

export default {

  data() {

    return {

      username: this.$store.state.username,

      defaultAccount: {
        name: '',
        path: '',
        fileName: 'No credentials file selected'
      },

      providers: [],

    }

  },

  created() {
    this.init()
  },

  methods: {

    init() {

      let awsAccount = Object.assign({}, this.defaultAccount)
      awsAccount.name = 'AWS'

      let gcpAccount = Object.assign({}, this.defaultAccount)
      gcpAccount.name = 'GCP'

      this.providers = [awsAccount, gcpAccount]

      HfApi.getUserCredentials(this.$store.getters.getUser)
          .then(response => {

            for (let i = 0; i < response.data.length; i++) {

              let currentData = response.data[i];
              let currentAccount = {
                name: currentData.provider,
                path: currentData.credentialsFilePath,
                fileName: currentData.fileName
              }

              // Update the corresponding account in the list with the new data
              this.providers = this.providers.map(account => {
                if (account.name === currentAccount.name) {
                  return currentAccount
                }
                return account
              })

            }

          })

    },

    selectFile(file, provider) {
      provider.fileName = file.name
      provider.inputFile = file
    },

    save() {

      this.providers
          .filter(provider => provider.inputFile)
          .forEach(provider => this.upload(provider))

    },

    hasChanged() {
      return this.providers.some(provider => provider.inputFile)
    },

    upload(provider) {

      let request = {
        provider: provider.name,
        userId: this.$store.state.userId
      };

      HfApi.uploadUserCredentials(provider.inputFile, request)
          .then(() => {
            this.init()
          })

    }

  }


}
</script>