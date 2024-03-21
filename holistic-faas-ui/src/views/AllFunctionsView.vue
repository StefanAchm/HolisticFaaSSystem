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


    <!--        <template v-slot:[`item`]="{ item }">-->
    <!--          <tr v-if="!item.id">-->
    <!--            <td colspan="100%">No data</td> &lt;!&ndash; Adjust the colspan as needed &ndash;&gt;-->
    <!--          </tr>-->
    <!--      -->
    <!--        </template>-->

    <template v-slot:[`item.status`]="{ item }">

      <v-tooltip bottom>

        <template v-slot:activator="{ on, attrs }">

          <v-icon
              :color="getColor(item.status)"
              v-on="on"
              v-bind="attrs"
              v-if="item.id"
          >
            {{ getIcon(item.status) }}
          </v-icon>

        </template>

        <span>{{ item.statusMessage }}</span>

      </v-tooltip>

    </template>

    <template v-slot:[`item.deploy`]="{ item }">


      <v-progress-circular
          :value="item.isLoadingValue"
          v-bind="props"
          :rotate="-90"
          v-if="item.isLoadingValue"
          color="primary"
          size="24"
      />

      <v-icon
          :disabled="item.status && item.status==='DEPLOYED'"
          v-if="item.id && !item.isLoadingValue"
          class="mr-2"
          @click="deployItem(item)"
      >
        mdi-rocket-launch
      </v-icon>

    </template>

    <template v-slot:[`item.copy`]="{ item }">
      <v-icon v-if="item.id" @click="copyItem(item)">mdi-content-duplicate</v-icon>
    </template>


    <template v-slot:[`item.info`]="{ item }">

      <v-tooltip bottom>
        <template v-slot:activator="{ on, attrs }">
          <v-icon v-if="item.id" v-bind="attrs" v-on="on">mdi-information-outline</v-icon>
        </template>
        <span>{{ item.id }}</span>
      </v-tooltip>

    </template>

    <template v-slot:[`group.header`]="{items, isOpen, toggle}">

      <td :colspan="headers.length">

        <v-btn text @click="toggle">
          <v-icon>{{ isOpen ? 'mdi-menu-down' : 'mdi-menu-up' }}</v-icon>
          Function:<span style="font-weight: normal "> {{ items[0].functionName }} </span>
        </v-btn>

        <!--        <v-spacer></v-spacer>-->

        <v-btn
            color="primary"
            class="mx-2"
            v-if="!items[0].id"
            @click="createItem(items[0])"
        >
          Add Function deployment
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

      socket: null,

      functionDeployments: [],

      editItem: new CloudFunction(),

      dialogVisible: false,
      dialogDelete: false,
      menu: false,
      activePicker: null,
      headers: [

        // {text: 'Id', value: 'id', width: '150px'},
        // {text: 'Name', value: 'name', width: '150px'},


        {text: 'Status', value: 'status', width: '20px'},

        {text: 'Deploy', value: 'deploy', width: '20px', sortable: false},

        {text: 'Provider', value: 'provider', width: '100px'},
        {text: 'Memory', value: 'memory', width: '100px'},
        {text: 'TimeoutSecs', value: 'timeoutSecs', width: '100px'},
        {text: 'Handler', value: 'handler', width: '100px'},
        {text: 'Region', value: 'region', width: '100px'},
        {text: 'Runtime', value: 'runtime', width: '100px'},

        // Linked:
        {text: 'UserName', value: 'userName', width: '100px'},
        {text: 'FunctionId', value: 'functionId', width: '100px'},

        {text: 'Copy', value: 'copy', width: '50px', sortable: false},

        {text: 'Info', value: 'info', width: '50px', sortable: false},

      ],

    }
  },

  methods: {

    connectToWebSocket() {

      this.socket = new WebSocket('ws://localhost:8080/hf/websocket');

      this.socket.onopen = function (event) {
        console.log('WebSocket is open now, event: ', event);
      };

      this.socket.onmessage = (event) => {
        this.updateProgress(event.data)

      };

      this.socket.onerror = function (event) {
        console.log('WebSocket error: ', event);
      };

      this.socket.onclose = function (event) {
        console.log('WebSocket is closed now, event: ', event);
      };

    },

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

          })

    },

    deployItem(item) {

      axios.post(
          Properties.API_IP + "/deploy/deploy", null,
          {
            headers: {'Content-Type': 'application/json'},
            params: {functionId: item.id, localOnly: false} // TODO: set localOnly to false
          })

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

    getIcon(status) {

      if (status === 'DEPLOYED') {
        return 'mdi-check-circle-outline'
      } else if (status === 'FAILED') {
        // return 'mdi-alert-octagon-outline'
        return 'mdi-alert-circle-outline'
      } else if (status === 'STARTED') {
        return 'mdi-progress-upload'
      } else {
        return ''
      }
    },

    getColor(status) {

      if (status === 'DEPLOYED') {
        return 'green'
      } else if (status === 'FAILED') {
        return 'red'
      } else if (status === 'STARTED') {
        return 'blue'
      } else {
        return ''
      }
    },


    updateProgress(data) {

      // Update progress of process component, depending on the message

      let message = null
      try {
        message = JSON.parse(data);
      } catch (e) {
        return
      }

      let id = message.id;
      let step = message.step;
      let steps = message.steps;
      let status = message.status;
      let statusMessage = message.statusMessage;
      let text = message.text;

      let value = step / steps * 100

      console.log(message)

      this.functionDeployments
          .filter(fd => fd.id === id)
          .forEach(fd => {
            this.$set(fd, 'isLoadingValue', value)
            if (value === 100) {
              // this.init()
              this.$set(fd, 'isLoadingValue', null)
            }

            if (status) {
              this.$set(fd, 'status', status)

              if (status === 'STARTED') {
                this.$set(fd, 'statusMessage', text)
              } else {
                this.$set(fd, 'statusMessage', statusMessage)
              }
            }

          })

    }

  },

  computed: {},

  created() {
    this.init();
    this.connectToWebSocket()
  },

  beforeDestroy() {
    if (this.socket) {
      this.socket.close();
    }

  }

}


</script>