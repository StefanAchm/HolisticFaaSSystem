<template>

  <v-data-table :headers="headers" :items="functionDeployments">

    <template v-slot:top>

      <v-toolbar flat>

        <v-toolbar-title>Function Deployments</v-toolbar-title>
        <v-divider class="mx-4" inset vertical></v-divider>
        <v-spacer></v-spacer>

        <v-dialog v-model="dialog" max-width="500px">

          <template v-slot:activator="{ on, attrs }">
            <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">Add Function Deployment</v-btn>
          </template>

          <v-card>
            <v-card-title>
              <span class="text-h5">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>

                <v-row>

                  <v-col>

                      <v-text-field v-model="editedItem.provider" label="Provider"></v-text-field>



                    <!--                    TODO -->
<!--                    <v-select-->
<!--                        v-model="editedItem.provider"-->
<!--                        :items="providers"-->
<!--                        item-title="title"-->
<!--                        item-value="value"-->
<!--                        label="Provider"-->
<!--                        persistent-hint-->
<!--                        return-object-->
<!--                        single-line-->
<!--                        ></v-select>-->

                  </v-col>

                </v-row>

                <v-row>

                  <v-col>
                    <v-text-field v-model="editedItem.memory" label="Memory"></v-text-field>
                  </v-col>

                </v-row>

                <v-row>

                  <v-col>
                    <v-text-field v-model="editedItem.timeoutSecs" label="TimeoutSecs"></v-text-field>
                  </v-col>

                </v-row>

                <v-row>

                  <v-col>
                    <v-text-field v-model="editedItem.handler" label="Handler"></v-text-field>
                  </v-col>

                </v-row>

                <v-row>

                  <v-col>
                    <v-text-field v-model="editedItem.region" label="Region"></v-text-field>
                  </v-col>

                </v-row>

                <v-row>

                  <v-col>
                    <v-text-field v-model="editedItem.runtime" label="Runtime"></v-text-field>
                  </v-col>

                </v-row>

                <v-row>

                  <v-col>
                    <v-text-field v-model="editedItem.userName" label="UserName"></v-text-field>
                  </v-col>

                </v-row>

                <v-row>

                  <v-col>
                    <v-text-field v-model="editedItem.functionId" label="FunctionId"></v-text-field>
                  </v-col>

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

import common from '../common'

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
        memory: '',
        timeoutSecs: '',
        handler: '',
        region: '',
        runtime: '',
        userName: '',
        functionId: ''
      },
      defaultItem: {
        provider: '',
        memory: '',
        timeoutSecs: '',
        handler: '',
        region: '',
        runtime: '',
        userName: '',
        functionId: ''
      },
      menu: false,
      activePicker: null,
      headers: [
        {text: 'Id', value: 'id', width: '150px'},
        // {text: 'Name', value: 'name', width: '150px'},
        {text: 'Provider', value: 'provider', width: '100px'},
        {text: 'Memory', value: 'memory', width: '100px'},
        {text: 'TimeoutSecs', value: 'timeoutSecs', width: '100px'},
        {text: 'Handler', value: 'handler', width: '100px'},
        {text: 'Region', value: 'region', width: '100px'},
        {text: 'Runtime', value: 'runtime', width: '100px'},

          // Linked:
        {text: 'UserName', value: 'userName', width: '100px'},
        {text: 'FunctionId', value: 'functionId', width: '100px'}

      ],
      functionDeployments: []
    }
  },

  methods: {

    init() {
      axios
          .get(Properties.API_IP + '/deploy/getAll')
          .then(response => {
            this.functionDeployments = response.data;
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
      //
      // let formData = new FormData();
      //
      // formData.append('apiFunction', JSON.stringify(this.editedItem));

      axios.post(Properties.API_IP + "/deploy/add", JSON.stringify(this.editedItem), {headers: {'Content-Type': 'application/json'}})
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

}


</script>