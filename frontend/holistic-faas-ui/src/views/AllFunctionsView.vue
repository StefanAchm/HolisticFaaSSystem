<template>

  <v-data-table
      group-by="functionId"
      :headers="headers"
      :items="functionDeployments"
  >

    <template v-slot:top>

      <v-toolbar flat>

        <v-toolbar-title>Functions</v-toolbar-title>
        <v-divider class="mx-4" inset vertical></v-divider>
        <v-spacer></v-spacer>

        <FunctionDialog
            @dialog-closed="init()"
        />
        <FunctionDeploymentDialog

            :dialog.sync="dialogVisible"
            @dialog-closed="init()"
            :itemprop="editItem"/>

        <!--        <v-dialog v-model="dialogDelete" max-width="500px">-->
        <!--          <v-card>-->
        <!--            <v-card-title class="text-h5">Are you sure, that you want to delete this function?</v-card-title>-->
        <!--            <v-card-actions>-->
        <!--              <v-spacer></v-spacer>-->
        <!--              <v-btn color="blue darken-1" text @click="closeDelete">Cancel</v-btn>-->
        <!--              <v-btn color="blue darken-1" text @click="deleteItemConfirm">OK</v-btn>-->
        <!--              <v-spacer></v-spacer>-->
        <!--            </v-card-actions>-->
        <!--          </v-card>-->
        <!--        </v-dialog>-->

      </v-toolbar>

    </template>

    <template v-slot:[`item.status`]="{ item }">
      <v-chip v-if="item.id" :color="getColor(item.status)">{{ item.status }}</v-chip>
    </template>

    <template v-slot:[`item.deploy`]="{ item }">
      <v-progress-circular v-if="item.isLoading" color="primary" indeterminate size="24"></v-progress-circular>
      <v-icon v-if="item.id && !item.isLoading" small class="mr-2" @click="deployItem(item)">mdi-rocket-launch</v-icon>
    </template>

    <template v-slot:[`item.copy`]="{ item }">
      <v-icon v-if="item.id" small @click="copyItem(item)">mdi-content-duplicate</v-icon>
      <v-icon v-if="!item.id" small @click="createItem(item)">mdi-plus</v-icon>
    </template>

    <template v-slot:[`group.header`]="{items, isOpen, toggle}">
      <td :colspan="headers.length">

        <v-btn text @click="toggle">
          <v-icon>{{ isOpen ? 'mdi-menu-down' : 'mdi-menu-up' }}</v-icon>
          Function:<span style="font-weight: normal "> {{ items[0].functionName }} </span>
        </v-btn>

      </td>

    </template>

  </v-data-table>

</template>

<script>

import axios from "axios";
import {Properties} from "@/config";

import common from '../common'

import FunctionDialog from "@/components/FunctionDialog";
import FunctionDeploymentDialog from "@/components/FunctionDeploymentDialog";

import {CloudFunction} from "@/models/CloudFunction";

export default {

  components: {
    FunctionDialog,
    FunctionDeploymentDialog
  },

  mixins: [common],

  data() {
    return {

      functionDeployments: [],

      editItem: new CloudFunction(),

      dialogVisible: false,
      dialogDelete: false,
      menu: false,
      activePicker: null,
      headers: [

          // {text: 'Id', value: 'id', width: '150px'},
        // {text: 'Name', value: 'name', width: '150px'},

        {text: 'Deploy', value: 'deploy', width: '50px', sortable: false},

        {text: 'Status', value: 'status', width: '150px'},
        {text: 'Provider', value: 'provider', width: '100px'},
        {text: 'Memory', value: 'memory', width: '100px'},
        {text: 'TimeoutSecs', value: 'timeoutSecs', width: '100px'},
        {text: 'Handler', value: 'handler', width: '100px'},
        {text: 'Region', value: 'region', width: '100px'},
        {text: 'Runtime', value: 'runtime', width: '100px'},

        // Linked:
        {text: 'UserName', value: 'userName', width: '100px'},
        {text: 'FunctionId', value: 'functionId', width: '100px'},

        {text: 'Create', value: 'copy', width: '50px', sortable: false},

      ],

    }
  },

  methods: {

    init() {

      axios
          .get(Properties.API_IP + '/function/getAll')
          .then(response => {

            this.functionDeployments = []

            response.data.forEach(f => {

              if (f.functionDeployments.length === 0) {

                this.functionDeployments.push({
                  functionId: f.id,
                  functionName: f.name,
                })

              } else {

                f.functionDeployments.forEach(fd => {
                  fd.functionName = f.name
                  this.functionDeployments.push(fd)
                })

              }

            })

            console.log(this.functionDeployments)

          })

    },

    deployItem(item) {

      this.$set(item, 'isLoading', true)

      axios.post(
          Properties.API_IP + "/deploy/deploy", null,
          {
            headers: {'Content-Type': 'application/json'},
            params: {functionId: item.id, localOnly: true} // TODO: set localOnly to false
          })
          .then(response => {
            console.log(response)
          })
          .finally(() => {
            // item.loading = false
            this.init()
          });

    },

    copyItem(item) {
      this.editItem = Object.assign({}, item)
      this.dialogVisible = true
    },

    createItem(item) {
      this.editItem = Object.assign({}, new CloudFunction({
            functionId: item.functionId,
            functionName: item.functionName,
          }
      ))
      this.dialogVisible = true
    },

    getColor(status) {
      /**
       *
       *     NONE,
       *
       *     STARTED,
       *
       *     FAILED,
       *
       *     DEPLOYED,
       */

      if (status === 'DEPLOYED') {
        return 'green'
      } else if (status === 'STARTED') {
        return 'yellow'
      } else if (status === 'FAILED') {
        return 'red'
      } else {
        return 'grey'
      }
    }


  },

  computed: {},

  created() {
    this.init();
  },

}


</script>