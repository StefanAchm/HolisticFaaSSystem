<template>

  <v-data-table :headers="headers" :items="functions">

    <template v-slot:top>

      <v-toolbar flat>

        <v-toolbar-title>Functions</v-toolbar-title>
        <v-divider class="mx-4" inset vertical></v-divider>
        <v-spacer></v-spacer>

        <v-dialog v-model="dialog" max-width="500px">

          <template v-slot:activator="{ on, attrs }">
            <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">Add Function</v-btn>
          </template>

          <v-card>
            <v-card-title>
              <span class="text-h5">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row>

                  <v-col cols="12" sm="6" md="4">
                    <v-text-field v-model="editedItem.name" label="Name"></v-text-field>
                  </v-col>

                </v-row>

                <v-row>

                  <v-file-input
                      truncate-length="15"
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
            <v-card-title class="text-h5">Are you sure, that you want to delete this function?</v-card-title>
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

import axios from "axios";
import {Properties} from "@/config";

export default {

  data() {
    return {
      dialog: false,
      dialogDelete: false,
      editedIndex: -1,
      currentFile: null,
      editedItem: {
        name: '',
      },
      defaultItem: {
        name: '',
      },
      menu: false,
      activePicker: null,
      headers: [
        {text: 'Id', value: 'id', width: '150px'},
        {text: 'Name', value: 'name', width: '150px'},
        {text: 'FilePath', value: 'filePath', width: '400px'}
      ],
      functions: []
    }
  },

  methods: {

    init() {
      axios
          .get(Properties.API_IP + '/function/getAll')
          .then(response => {

            this.functions = response.data;

            console.log(this.functions)

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

      if (!this.currentFile) {
        console.log("Please select a file!");
        return;
      } else {
        console.log("Uploading file: " + this.currentFile.name);
        console.log(this.currentFile)
      }

      if(!this.editedItem) {
        console.log("Please enter a name!");
        return;
      } else {
        console.log("Uploading function: " + this.editedItem);
        console.log(this.editedItem)
      }

      let formData = new FormData();

      formData.append('file', this.currentFile);

      // Have to use Blob to send JSON as a file
      formData.append('apiFunction', new Blob([JSON.stringify(this.editedItem)], {type : 'application/json'}));

      console.log(this.currentFile, event => {

        let progress = Math.round((100 * event.loaded) / event.total);

        console.log(progress)

      });

      axios.post(Properties.API_IP + "/function/upload", formData)
          .then(response => {
            console.log(response)
          })
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

  // mounted() {
  //   this.init();
  // },


}


</script>