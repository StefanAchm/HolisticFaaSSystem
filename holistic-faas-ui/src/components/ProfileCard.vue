<template>

  <v-card
      class="mx-auto mt-5"
      max-width="70%">

    <v-card-title>
      <span class="text-h4" >Profile: {{ username }}</span>
    </v-card-title>

    <v-divider></v-divider>

    <CredentialsEditDialog
        :dialog.sync="credentialsEditDialogVisible"
        :edit-item="{}"
        @dialog-closed="init"
        :provider="editProvider"
        ></CredentialsEditDialog>

    <v-card-text>

      <strong>AWS Account: </strong>

      {{ awsAccount.fileName }}

      <v-icon
          class="ml-2"
          small
          @click="edit('AWS')"

      >mdi-pencil</v-icon>

    </v-card-text>

    <v-card-text>
      GCP Account: {{ gcpAccount.fileName }}
    </v-card-text>

  </v-card>


</template>

<script>

import HfApi from "@/utils/hf-api";
import CredentialsEditDialog from "@/components/CredentialsEditDialog.vue";

export default {
  components: {CredentialsEditDialog},

  data() {

    return {

      username: this.$store.state.username,

      defaultAccount: {
        path: '',
        fileName: ''
      },

      awsAccount: {},
      gcpAccount: {},

      editProvider: '',
      credentialsEditDialogVisible: false



    }

  },

  created() {
    this.init()
  },

  methods: {

    init() {

      this.awsAccount = Object.assign({}, this.defaultAccount)
      this.gcpAccount = Object.assign({}, this.defaultAccount)

      HfApi.getUserCredentials(this.$store.getters.getUser)
          .then(response => {

            for(let i = 0; i < response.data.length; i++) {

              let currentData = response.data[i];

              if(currentData.provider === 'AWS') {
                this.awsAccount.path = currentData.credentialsFilePath
                this.awsAccount.fileName = currentData.fileName
              } else if(currentData.provider === 'GCP') {
                this.gcpAccount.path = currentData.credentialsFilePath
                this.gcpAccount.fileName = currentData.fileName
              }
            }

          })

    },

    edit(provider) {
      this.editProvider = provider
      this.credentialsEditDialogVisible = true
    }

  }


}
</script>