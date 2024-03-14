<template>

  <v-data-table
      group-by="functionId"
      :headers="headers"
      :items="functionDeployments"
  >

    <template v-slot:top>

      <v-toolbar flat>

        <v-toolbar-title>Function Deployments</v-toolbar-title>
        <v-divider class="mx-4" inset vertical></v-divider>
        <v-spacer></v-spacer>

        <FunctionDialog/>
        <FunctionDeploymentDialog/>

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
import FunctionDialog from "@/components/FunctionDialog";
import FunctionDeploymentDialog from "@/components/FunctionDeploymentDialog";

export default {

  components: {
    FunctionDialog,
    FunctionDeploymentDialog
  },

  mixins: [common],

  data() {
    return {
      dialog: false,
      dialogDelete: false,
      editedIndex: -1,
      currentFile: null,
      editedItem: new Function(),
      defaultItem: new Function(),
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
      axios.post(Properties.API_IP + "/deploy/deploy", null, {
        headers: {'Content-Type': 'application/json'},
        params: {functionId: item.id}
      })
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


  },

  computed: {},

  created() {
    this.init();
  },

}


</script>