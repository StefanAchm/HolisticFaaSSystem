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

                  <v-col>

                    <v-select
                        v-model="editedItem.userName"
                        :items="users"
                        item-text="title"
                        item-value="value"
                        label="User"
                    ></v-select>


                  </v-col>

                </v-row>

                <v-row>

                  <v-col>

                    <v-select
                        v-model="editedItem.functionId"
                        :items="functions"
                        item-text="title"
                        item-value="value"
                        label="Function"
                    ></v-select>

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

                  <v-select
                      v-model="editedItem.region"
                      :items="regions"
                      item-text="title"
                      item-value="value"
                      label="Region"
                  ></v-select>


                  </v-col>

                </v-row>

                <v-row>

                  <v-col>

                    <v-select
                        v-model="editedItem.runtime"
                        :items="runtimes"
                        item-text="title"
                        item-value="value"
                        label="Runtime"
                    ></v-select>

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

    <template v-slot:[`item.deploy`]="{ item }">
      <v-icon small class="mr-2" @click="deployItem(item)">mdi-rocket-launch</v-icon>
    </template>

    <template v-slot:[`item.copy`]="{ item }">
      <v-icon small @click="copyItem(item)">mdi-content-duplicate</v-icon>
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
        // {text: 'Id', value: 'id', width: '150px'},
        // {text: 'Name', value: 'name', width: '150px'},
        {text: 'Provider', value: 'provider', width: '100px'},
        {text: 'Memory', value: 'memory', width: '100px'},
        {text: 'TimeoutSecs', value: 'timeoutSecs', width: '100px'},
        {text: 'Handler', value: 'handler', width: '100px'},
        {text: 'Region', value: 'region', width: '100px'},
        {text: 'Runtime', value: 'runtime', width: '100px'},

        // Linked:
        {text: 'UserName', value: 'userName', width: '100px'},
        {text: 'FunctionId', value: 'functionId', width: '100px'},

        {text: 'Deploy', value: 'deploy', width: '50px', sortable: false},
        {text: 'Copy', value: 'copy', width: '50px', sortable: false}

      ],
      functionDeployments: [],
      users: [],
      allUsers: [],
      functions: [],
      allFunctions: [],
      providerOptions: [],
      runtimes: [],
      regions: []
    }
  },

  watch: {

    editedItem: {
      handler(newValue) {

        console.log(newValue)

        // Filter users by provider
        // Create a new array with the filtered users for select
        this.users = this.allUsers
            .filter(user => user.provider === newValue.provider)
            .map(user => {
              return {
                title: user.username,
                value: user.username
              }

            })

        this.functions = this.allFunctions
            // .filter(func => func.provider === newValue.provider)
            .map(func => {
              return {
                title: func.id,
                value: func.id
              }

            })

        this.runtimes = this.providerOptions
            .filter(provider => provider.provider === newValue.provider)
            .map(provider => provider.runtimes)
            .flat()
            .map(runtime => {
              return {
                title: runtime,
                value: runtime
              }
            })

        this.regions = this.providerOptions
            .filter(provider => provider.provider === newValue.provider)
            .map(provider => provider.regions)
            .flat()
            .map(region => {
              return {
                title: region,
                value: region
              }
            })



      },
      deep: true

    }

  },

  methods: {

    init() {

      axios
          .get(Properties.API_IP + '/deploy/getAll')
          .then(response => {
            this.functionDeployments = response.data;
          })

      axios
          .get(Properties.API_IP + '/user/getAll')
          .then(response => {
            this.allUsers = response.data;
          })

      axios
          .get(Properties.API_IP + '/function/getAll')
          .then(response => {
            this.allFunctions = response.data;
          })

      axios
          .get(Properties.API_IP + '/provider/getProviderOptions')
          .then(response => {
            this.providerOptions = response.data;
          })

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

    deployItem(item) {
      axios.post(Properties.API_IP + "/deploy/deploy", null, {headers: {'Content-Type': 'application/json'}, params: {functionId: item.id}})
          .then(response => {
            console.log(response)
          })
          .finally(() => {
            this.close();
          });
    },

    copyItem(item) {
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },

    upload() {

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