<template>


  <v-data-table
      :headers="headers"
      :items="functions"
      v-model="selected"
      :single-select="false"
      item-key="id"
      show-select
      :items-per-page="15"
      :search="search"
  >

    <template v-slot:top>

      <v-toolbar flat>

        <v-toolbar-title>Functions</v-toolbar-title>

        <v-spacer></v-spacer>


        <!-- create a list of functionality for the user to interact with -->
        <!-- The user can add functions, either per dialog or from a yaml file -->
        <!-- The user can process the functions: edit, download, or migrate -->
        <!-- The user can deploy the functions to a provider -->

        <v-menu offset-y>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
                color="primary"
                v-bind="attrs"
                v-on="on"
                class="mx-2"
            >
              <v-icon
                  left>
                mdi-plus
              </v-icon>
              Add
            </v-btn>
          </template>
          <v-list>
            <v-list-item-group>
              <v-list-item>

                <v-list-item-icon>
                  <v-icon>mdi-lambda</v-icon>
                </v-list-item-icon>

                <v-list-item-title @click="functionDialogVisible = true;">Function</v-list-item-title>

              </v-list-item>
              <v-list-item>

                <v-list-item-icon>
                  <v-icon>mdi-file-document-outline</v-icon>
                </v-list-item-icon>

                <input
                    type="file"
                    ref="fileInput"
                    @change="onFileSelected"
                    style="display: none"
                    accept=".yaml,.yml"
                />
                <v-list-item-title @click="onButtonClick">from YAML</v-list-item-title>

              </v-list-item>
            </v-list-item-group>
          </v-list>
        </v-menu>

        <v-btn
            color="primary"
            class="mx-2"
            :disabled="selected.length === 0"
            @click="downloadYaml">

          <v-icon
              left>
            mdi-download
          </v-icon>

          Download

        </v-btn>

        <FunctionsEditMenu
            :items="selected"
            @menu-closed="init"
        />

        <v-btn
            :disabled="selected.length === 0"
            color="primary"
            class="mx-2"
            @click="deployAll">

          <v-icon
              left>
            mdi-rocket-launch
          </v-icon>

          Deploy ({{ selected.length }})

        </v-btn>


        <!--          <v-btn-->
        <!--              color="primary"-->
        <!--              class="mx-2"-->
        <!--              @click="functionDialogVisible = true">-->

        <!--            Add Functiontype-->

        <!--          </v-btn>-->

        <!--          <v-btn-->
        <!--              color="primary"-->
        <!--              class="mx-2"-->
        <!--              @click="downloadYaml">-->

        <!--            Download YAML-->

        <!--          </v-btn>-->

        <!--        <v-file-input-->
        <!--            truncate-length="60"-->
        <!--            @change="selectFile"-->
        <!--        ></v-file-input>-->

        <!--        <v-btn-->
        <!--            color="primary"-->
        <!--            class="mx-2"-->
        <!--            @click="uploadPackage">-->

        <!--          Upload Package-->

        <!--        </v-btn>-->

        <!--          <div>-->
        <!--            <input-->
        <!--                type="file"-->
        <!--                ref="fileInput"-->
        <!--                @change="onFileSelected"-->
        <!--                style="display: none"-->
        <!--                accept=".yaml,.yml"-->
        <!--            />-->
        <!--            <v-btn-->
        <!--                color="primary"-->
        <!--                @click="onButtonClick">Upload YAML-->
        <!--            </v-btn>-->
        <!--          </div>-->

        <!--          <v-spacer></v-spacer>-->

        <!--          <v-btn-->
        <!--              :disabled="selected.length === 0"-->
        <!--              color="primary"-->
        <!--              class="mx-2"-->
        <!--              @click="deployAll">-->

        <!--            Deploy ({{ selected.length }})-->

        <!--          </v-btn>-->

        <!--          <FunctionsEditMenu-->
        <!--              :items="selected"-->
        <!--              @menu-closed="init"-->
        <!--          />-->

        <FunctionTypeDialog
            :dialog.sync="functionDialogVisible"
            @dialog-closed="init"
            :edit-item="editItem"/>

        <FunctionDeploymentDialog
            :dialog.sync="functionDeploymentDialogVisible"
            @dialog-closed="init"
            :edit-item="editItem"/>

        <FunctionImplementationDialog
            :dialog.sync="functionImplementationDialogVisible"
            @dialog-closed="init"
            :edit-item="editItem"/>

      </v-toolbar>

      <v-toolbar flat>
        <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="Search"
            single-line
            hide-details
        ></v-text-field>
      </v-toolbar>

    </template>

    <template v-slot:[`item.implementation`]="{ item }">

      <div v-if="item.functionImplementation === null || !item.functionImplementation.fileName">
        No Implementation
        <v-icon color="red" small>mdi-alert-circle-outline</v-icon>
      </div>

      <div v-else>

        <v-tooltip bottom>
          <template v-slot:activator="{ on, attrs }">
        <span
            v-bind="attrs"
            v-on="on"
        >{{ item.functionImplementation.fileName }}</span>
          </template>
          <span>{{ item.functionImplementation.filePath }}</span>
        </v-tooltip>

        <!--        <v-icon @click="functionImplementationDialogVisible = true;">mdi-plus</v-icon>-->


      </div>

    </template>

    <template v-slot:[`item.handler`]="{ item }">

      <div v-if="item.functionDeployment === null">
        No Deployment
        <v-icon color="red" small>mdi-alert-circle-outline</v-icon>
      </div>

      <div v-else>

        <v-tooltip bottom>
          <template v-slot:activator="{ on, attrs }">
        <span
            v-bind="attrs"
            v-on="on"
        >{{ item.handler }}</span>
          </template>
          <span>{{ item.functionDeployment?.handler }}</span>
        </v-tooltip>

      </div>

    </template>

    <template v-slot:[`item.deployment`]="{ item }">

      <div v-if="item.functionDeployment === null">

        <span>

        No Deployment

          <!--          <v-icon @click="functionDeploymentDialogVisible = true;">mdi-plus</v-icon>-->

        </span>

      </div>

      <div v-else>

        <span>

          <strong> Handler: </strong> {{ item.functionDeployment.handler }} <br>
          <strong> Provider: </strong> {{ item.functionDeployment.provider }} <br>
          <strong> Region: </strong> {{ item.functionDeployment.region }} <br>
          <strong> TimeoutSecs: </strong> {{ item.functionDeployment.timeoutSecs }} <br>
          <strong> Memory: </strong> {{ item.functionDeployment.memory }} <br>
          <strong> Runtime: </strong> {{ item.functionDeployment.runtime }} <br>
          <strong> User: </strong> {{ item.functionDeployment.userName }} <br>

        </span>

        <!--        <v-icon @click="functionDeploymentDialogVisible = true;">mdi-plus</v-icon>-->


      </div>

    </template>

    <template v-slot:[`item.edit2`]="{ item }">

      <v-menu offset-y>
        <template v-slot:activator="{ on, attrs }">
          <v-btn
              icon
              color="primary"
              dark
              v-bind="attrs"
              v-on="on"
          >

            <v-icon>mdi-dots-vertical</v-icon>

          </v-btn>
        </template>
        <v-list>

          <v-list-item-group
              v-model="selectedMenuItem"
          >

            <v-subheader>
              Implementation
            </v-subheader>

            <v-list-item>
              <v-list-item-title @click="addImplementation(item)">Add</v-list-item-title>
            </v-list-item>

            <v-list-item>
              <v-list-item-title @click="editImplementation(item)">Edit</v-list-item-title>
            </v-list-item>

            <v-subheader>
              Deployment
            </v-subheader>

            <v-list-item v-if="item.functionImplementation">
              <v-list-item-title @click="addDeployment(item)">Add</v-list-item-title>
            </v-list-item>

            <v-list-item v-if="item.functionImplementation">
              <v-list-item-title @click="editDeployment(item)">Edit</v-list-item-title>
            </v-list-item>

          </v-list-item-group>

        </v-list>
      </v-menu>

    </template>

    <template v-slot:[`item.actions`]="{ item }">

      <v-menu offset-y>
        <template v-slot:activator="{ on, attrs }">
          <v-btn
              icon
              color="secondary"
              dark
              v-bind="attrs"
              v-on="on"
          >

            <v-icon>mdi-plus-circle</v-icon>

          </v-btn>
        </template>
        <v-list>

          <v-list-item-group
              v-model="selectedMenuItem"
          >

            <v-list-item>
              <v-list-item-title @click="addImplementation(item)">Implementation</v-list-item-title>
            </v-list-item>

            <v-list-item v-if="item.functionImplementation">
              <v-list-item-title @click="addDeployment(item)">Deployment</v-list-item-title>
            </v-list-item>

          </v-list-item-group>

        </v-list>
      </v-menu>

      <v-menu offset-y>
        <template v-slot:activator="{ on, attrs }">
          <v-btn
              icon
              color="secondary"
              dark
              v-bind="attrs"
              v-on="on"
          >

            <v-icon>mdi-pencil-circle</v-icon>

          </v-btn>
        </template>
        <v-list>

          <v-list-item-group
              v-model="selectedMenuItem"
          >

            <v-list-item>
              <v-list-item-title @click="editImplementation(item)">Implementation</v-list-item-title>
            </v-list-item>

            <v-list-item v-if="item.functionImplementation">
              <v-list-item-title @click="editDeployment(item)">Deployment</v-list-item-title>
            </v-list-item>


          </v-list-item-group>

        </v-list>
      </v-menu>

    </template>

    <template v-slot:[`item.status`]="{ item }">

      <v-tooltip bottom>

        <template v-slot:activator="{ on, attrs }">

          <v-icon
              :color="getColor(item)"
              v-on="on"
              v-bind="attrs"
              @click="deployFunction(item)"
              :class="{
                'pulsate-icon': item.functionDeployment?.status === 'STARTED',
              }"
          >
            {{ getIcon(item) }}
          </v-icon>


        </template>

        <span>{{ toolTipMessage(item) }}</span>


      </v-tooltip>

    </template>

    <template v-slot:[`item.link`]="{ item }">

      <v-tooltip
          v-if="item.functionDeployment"
          bottom>

        <template v-slot:activator="{ on, attrs }">

          <!-- open a new tab either for aws or gcp to the corresponding provider console -->
          <v-icon
              v-on="on"
              v-bind="attrs"
              @click="openNewTab(item)"
          >mdi-open-in-new
          </v-icon>

        </template>

        <span>Open in {{ item.functionDeployment?.provider }} console</span>

      </v-tooltip>

    </template>

  </v-data-table>


</template>

<script>


import HfApi from "@/utils/hf-api";
import FunctionTypeDialog from "@/components/function/dialogs/FunctionTypeDialog.vue";
import FunctionImplementationDialog from "@/components/function/dialogs/FunctionImplementationDialog.vue";
import FunctionDeploymentDialog from "@/components/function/dialogs/FunctionDeploymentDialog.vue";
import hfWebsocket from "@/utils/hf-websocket";
import FunctionsEditMenu from "@/components/function/FunctionsEditMenu.vue";

export default {

  components: {
    FunctionsEditMenu,
    FunctionDeploymentDialog,
    FunctionImplementationDialog,
    FunctionTypeDialog
  },

  data: () => ({

    search: '',

    selectedMenuItem: {},

    functions: [],
    selected: [],
    headers: [

      {text: 'Status', value: 'status', sortable: false},
      {text: 'Name', value: 'name', sortable: true},
      {text: 'Implementation', value: 'implementation', sortable: true},

      // {text: 'Deployment', value: 'deployment', sortable: true},


      {text: 'Handler', value: 'handler', sortable: true},
      {text: 'Provider', value: 'provider', sortable: true},
      {text: 'Region', value: 'region', sortable: true},
      {text: 'TimeoutSecs', value: 'timeoutSecs', sortable: true},
      {text: 'Memory', value: 'memory', sortable: true},
      {text: 'Runtime', value: 'runtime', sortable: true},
      {text: 'User', value: 'userName', sortable: true},

      {text: '', value: 'actions', sortable: false},

      {text: '', value: 'link', sortable: false},

      // {text: 'Add Deployment', value: 'actionsD', sortable: true},


      // {text: 'Deploy', value: 'deploy', sortable: false},


    ],

    functionMigrationDialogVisible: false,
    functionDialogVisible: false,
    functionImplementationDialogVisible: false,
    functionDeploymentDialogVisible: false,

    editItem: {
      functionType: {},
      functionImplementation: {},
      functionDeployment: {}
    },


  }),

  created() {
    this.init();
  },

  methods: {

    init() {
      this.loadFunctions();
      this.selectedMenuItem = {};
      this.selected = [];
      this.editItem = {
        functionType: {},
        functionImplementation: {},
        functionDeployment: {}
      };
      hfWebsocket.onMessage(this.updateProgress)

    },

    loadFunctions() {
      HfApi.getAllFunctions()
          .then(response => {
            this.functions = response.data;

            for (let i = 0; i < this.functions.length; i++) {

              if (this.functions[i].functionDeployment) {
                this.functions[i].id = this.functions[i].functionDeployment.id;
              } else if (this.functions[i].functionImplementation) {
                this.functions[i].id = this.functions[i].functionImplementation.id;
              } else {
                this.functions[i].id = this.functions[i].functionType.id;
              }


              this.functions[i].name = this.functions[i].functionType.name;
              this.functions[i].handler = this.functions[i].functionDeployment?.handlerShort;
              this.functions[i].provider = this.functions[i].functionDeployment?.provider;
              this.functions[i].region = this.functions[i].functionDeployment?.region;
              this.functions[i].timeoutSecs = this.functions[i].functionDeployment?.timeoutSecs;
              this.functions[i].memory = this.functions[i].functionDeployment?.memory;
              this.functions[i].runtime = this.functions[i].functionDeployment?.runtime;
              this.functions[i].userName = this.functions[i].functionDeployment?.userName;
            }

          })
    },

    addImplementation(item) {
      this.editItem = item;
      this.editItem.functionImplementation.id = null;
      this.functionImplementationDialogVisible = true;
    },

    editImplementation(item) {
      this.editItem = item;
      this.functionImplementationDialogVisible = true;
    },

    addDeployment(item) {
      this.editItem = item;
      if (this.editItem.functionDeployment) {
        this.editItem.functionDeployment.id = null;
      }
      this.functionDeploymentDialogVisible = true;
    },

    editDeployment(item) {
      this.editItem = item;
      this.functionDeploymentDialogVisible = true;
    },

    openNewTab(item) {
      window.open(item.functionDeployment.uri)
    },


    getIcon(item) {

      if (item.functionDeployment === null) {
        return 'mdi-progress-helper'
      }

      let status = item.functionDeployment.status

      if (status === 'DEPLOYED') {
        return 'mdi-progress-check'
      } else if (status === 'FAILED') {
        return 'mdi-progress-alert'
      } else if (status === 'STARTED') {
        return 'mdi-progress-upload'
      } else if (status === 'CREATED') {
        return 'mdi-progress-clock'
      } else if (status === 'CHANGED') {
        return 'mdi-progress-wrench'
      } else {
        return ''
      }
    },

    getColor(item) {

      if (item.functionDeployment === null) {
        return 'grey'
      }

      let status = item.functionDeployment.status

      if (status === 'DEPLOYED') {
        return 'green'
      } else if (status === 'FAILED') {
        return 'red'
      } else if (status === 'STARTED') {
        return 'blue'
      } else if (status === 'CREATED') {
        return 'orange'
      } else if (status === 'CHANGED') {
        return 'orange'
      } else {
        return ''
      }

    },

    toolTipMessage(item) {

      if (item.functionDeployment === null) {
        return 'No Deployment'
      }

      if (item.functionDeployment.text) {
        return item.functionDeployment.text
      } else {
        return item.functionDeployment?.statusMessage
      }

    },

    deployFunction(item) {

      HfApi.deployFunctionDeploy(item.functionDeployment.id)

    },

    deployAll() {

      this.selected.forEach(item => {
        this.deployFunction(item)
      })
      this.selected = []
    },

    updateProgress(message) {

      // Update progress of process component, depending on the message

      let id = message.id;
      let step = message.step;
      let steps = message.steps;
      let status = message.status;
      let statusMessage = message.statusMessage;
      let text = message.text;

      let value = step / steps * 100

      this.functions
          .filter(f => f.id === id)
          .forEach(f => {

            let fd = f.functionDeployment

            this.$set(fd, 'isLoadingValue', value)

            if (value === 100) {
              this.$set(fd, 'isLoadingValue', null)
            }

            this.$set(fd, 'status', status)
            this.$set(fd, 'statusMessage', statusMessage)
            this.$set(fd, 'text', text)

          })

    },

    downloadYaml() {
      HfApi.downloadYaml(this.selected)
          .then(response => {
            // Create a new Blob object using the response data of the file
            let blob = new Blob([response.data], {type: 'application/yaml'});

            // Create a link element
            let link = document.createElement('a');

            // Create an object URL for the file
            link.href = window.URL.createObjectURL(blob);

            // Set the file name and download attribute
            link.download = 'functions.yaml';

            // Add the link to the DOM
            document.body.appendChild(link);

            // Simulate clicking the link
            link.click();

            // Remove the link from the DOM
            document.body.removeChild(link);
          });
    },

    onButtonClick() {
      this.$refs.fileInput.click();
    },
    onFileSelected(event) {
      HfApi.uploadYaml(event.target.files[0], this.$store.state.userId)
          .then(() => {
            this.init()
          })

    },

  },

}

</script>

<style scoped>

.pulsate-icon {
  animation: pulsate 1s infinite;
}

@keyframes pulsate {
  0% {
    transform: scale(0.9);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(0.9);
  }
}

.custom-app-bar {
  position: relative;
  top: -12px;
  left: -12px !important;
}

</style>