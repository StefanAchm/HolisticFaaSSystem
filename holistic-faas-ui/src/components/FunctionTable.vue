<template>

  <v-data-table
      :headers="headers"
      :items="functions"
      v-model="selected"
      :single-select="false"
      item-key="id"
      show-select
  >

    <template v-slot:top>

      <v-toolbar flat>

        <v-toolbar-title>Functions</v-toolbar-title>

        <v-spacer></v-spacer>

        <v-btn
            color="primary"
            class="mx-2"
            @click="functionDialogVisible = true">

          Add Function

        </v-btn>

<!--        <v-btn-->
<!--            color="primary"-->
<!--            class="mx-2"-->
<!--            :disabled="selected.length === 0"-->
<!--            @click="functionMigrationDialogVisible = true">-->

<!--          Migrate-->

<!--        </v-btn>-->
<!--        -->
<!--        <FunctionMigrationDialog-->
<!--            :dialog.sync="functionMigrationDialogVisible"-->
<!--            :items="selected"-->
<!--            @dialog-closed="init"-->
<!--        />-->

        <FunctionsEditMenu
            :items="selected"
            :disabled="selected.length === 0"
            @menu-closed="init"
        />

        <FunctionTypeDialog
            :dialog.sync="functionDialogVisible"
            @dialog-closed="init"
            :edit-item="{}"/>

        <FunctionDeploymentDialog
            :dialog.sync="functionDeploymentDialogVisible"
            @dialog-closed="init"
            :edit-item="{}"
            :function-implementation="editItem.functionImplementation"/>

        <FunctionImplementationDialog
            :dialog.sync="functionImplementationDialogVisible"
            @dialog-closed="init"
            :edit-item="{}"
            :function-type="editItem.functionType"/>

      </v-toolbar>

    </template>

    <template v-slot:[`item.type`]="{ item }">

      <span>{{ item.functionType.name }}</span>

    </template>

    <template v-slot:[`item.implementation`]="{ item }">

      <div v-if="item.functionImplementation === null">

        <span>

          No Implementation

          <!--          <v-icon @click="functionImplementationDialogVisible = true;">mdi-plus</v-icon>-->

        </span>

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
          <strong> Username: </strong> {{ item.functionDeployment.userName }} <br>

        </span>

        <!--        <v-icon @click="functionDeploymentDialogVisible = true;">mdi-plus</v-icon>-->


      </div>

    </template>

    <template v-slot:[`item.edit`]="{ item }">


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

            <v-list-item>
              <v-list-item-title @click="openImplementationDialog(item)">Add Implementation</v-list-item-title>
            </v-list-item>

            <v-list-item v-if="item.functionImplementation">
              <v-list-item-title @click="openDeploymentDialog(item)">Add Deployment</v-list-item-title>
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
          >
            {{ getIcon(item) }}
          </v-icon>

        </template>

        <span>{{ getToolTipMessage(item) }}</span>

      </v-tooltip>

    </template>

    <!--    <template v-slot:[`item.deploy`]="{ item }">-->

    <!--      <v-progress-circular-->
    <!--          :value="item.isLoadingValue"-->
    <!--          v-bind="props"-->
    <!--          :rotate="-90"-->
    <!--          v-if="item.isLoadingValue"-->
    <!--          color="primary"-->
    <!--          size="24"-->
    <!--      />-->

    <!--      <v-icon-->
    <!--          :disabled="item.status && item.status==='DEPLOYED'"-->
    <!--          v-if="item.functionDeployment?.id && !item.isLoadingValue"-->
    <!--          class="mr-2"-->
    <!--          @click="deployItem(item)"-->
    <!--      >-->
    <!--        mdi-rocket-launch-->
    <!--      </v-icon>-->

    <!--    </template>-->

  </v-data-table>


</template>

<script>


import HfApi from "@/utils/hf-api";
import FunctionTypeDialog from "@/components/FunctionTypeDialog.vue";
import FunctionImplementationDialog from "@/components/FunctionImplementationDialog.vue";
import FunctionDeploymentDialog from "@/components/FunctionDeploymentDialog.vue";
import FunctionsEditMenu from "@/components/FunctionsEditMenu.vue";

export default {

  components: {
    FunctionsEditMenu,
    FunctionDeploymentDialog,
    FunctionImplementationDialog,
    FunctionTypeDialog
  },

  data: () => ({

    selectedMenuItem: {},

    functions: [],
    selected: [],
    headers: [

      {text: 'Name', value: 'type', sortable: true},
      {text: 'Implementation', value: 'implementation', sortable: true},
      {text: 'Deployment', value: 'deployment', sortable: true},
      {text: 'Edit', value: 'edit', sortable: true},
      // {text: 'Add Deployment', value: 'actionsD', sortable: true},
      {text: 'Status', value: 'status', sortable: false},
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
    this.loadFunctions();
  },

  methods: {

    init() {
      this.loadFunctions();
      this.selectedMenuItem = {};
    },

    loadFunctions() {
      HfApi.getAllFunctions()
          .then(response => {
            this.functions = response.data;

            for (let i = 0; i < this.functions.length; i++) {
              this.functions[i].id = i;
            }

          })
    },

    openImplementationDialog(item) {
      this.functionImplementationDialogVisible = true;
      this.editItem = item;
    },

    openDeploymentDialog(item) {
      this.functionDeploymentDialogVisible = true;
      this.editItem = item;
    },

    getIcon(item) {

      if(item.functionDeployment === null) {
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
      } else {
        return ''
      }
    },

    getColor(item) {

      if(item.functionDeployment === null) {
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
      } else {
        return ''
      }

    },

    deployFunction(item) {

      HfApi.deployFunctionDeploy(item.functionDeployment.id)

    },

    getToolTipMessage(item) {

      if(item.functionDeployment === null) {
        return 'No Deployment'
      }

      let status = item.functionDeployment.status

      if (status === 'CREATED') {
        return 'Deployment not started'
      } else {
        return item.functionDeployment?.statusMessage
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


}

</script>