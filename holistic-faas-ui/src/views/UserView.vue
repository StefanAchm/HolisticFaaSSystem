<template>

  <v-data-table :headers="headers" :items="users">

    <template v-slot:top>

      <v-toolbar flat>

        <v-toolbar-title>Users</v-toolbar-title>
        <v-divider class="mx-4" inset vertical></v-divider>
        <v-spacer></v-spacer>

        <v-dialog v-model="dialog" max-width="500px">

          <template v-slot:activator="{ on, attrs }">
            <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">Add Credentials</v-btn>
          </template>

          <v-card>
            <v-card-title>
              <span class="text-h5">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>

<!--                <v-row>-->

<!--                  <v-col>-->
<!--                    <v-text-field v-model="editedItem.username" label="Username"></v-text-field>-->
<!--                  </v-col>-->

<!--                </v-row>-->

                <v-row>

                  <v-col>

                    <v-select
                        v-model="editedItem.provider"
                        :items="providers"
                        item-text="title"
                        item-value="value"
                        label="Provider"
                    ></v-select>

                  </v-col>

                </v-row>

                <v-row>

                  <v-file-input
                      truncate-length="60"
                      @change="selectFile"
                  ></v-file-input>

                </v-row>

              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
              <v-btn color="blue darken-1" text @click="upload">Save</v-btn>
            </v-card-actions>

          </v-card>
        </v-dialog>

        <v-dialog v-model="dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="text-h5">Are you sure, that you want to delete this user?</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="closeDelete">Cancel</v-btn>
              <v-btn color="blue darken-1" text @click="deleteItemConfirm">OK</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>

  </v-data-table>

</template>

<script>

import common from "@/utils/common";

import HfApi from "@/utils/hf-api";

export default {

  mixins: [common],

  data() {
    return {
      dialog: false,
      dialogDelete: false,
      editedIndex: -1,
      currentFile: null,
      editedItem: {
        provider: '',
      },
      defaultItem: {},
      menu: false,
      activePicker: null,
      headers: [
        {text: 'Username', value: 'user.username', width: '100px'},
        {text: 'Provider', value: 'provider', width: '100px'},
        {text: 'Credentials', value: 'credentialsFilePath', width: '300px'},
      ],
      users: []
    }
  },

  methods: {

    init() {

      HfApi.getAllUsers()
          .then(response => {
            this.users = response.data;
          })

    },

    selectFile(file) {

      this.currentFile = file;

    },

    close() {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
        this.init()
      })
    },

    closeDelete() {
      this.dialogDelete = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    deleteItemConfirm() {
    },

    upload() {

      this.editedItem.userId = this.$store.state.userId;

      HfApi.uploadUserCredentials(this.currentFile, this.editedItem)
          .finally(() => {
            this.close();
          });

    }


  },

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? 'New' : 'Edit'
    },
  },

  created() {
    this.init();
  },

}


</script>