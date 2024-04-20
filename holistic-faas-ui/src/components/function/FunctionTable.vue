<template>

  <v-data-table
      :headers="headers"
      :items="functions"
      v-model="selected"
      :single-select="false"
      item-key="id"
      show-select
      :items-per-page="10"
      :search="search"
  >

    <template v-slot:top>

      <FunctionDeploymentDialog
          :dialog.sync="functionDeploymentDialogVisible"
          @dialog-closed="init"
          :edit-item="editItem"/>

      <FunctionImplementationDialog
          :dialog.sync="functionImplementationDialogVisible"
          @dialog-closed="init"
          :edit-item="editItem"/>

    </template>

    <template v-slot:[`item.implementation`]="{ item }">

      <div v-if="item.functionImplementation === null || !item.functionImplementation.fileName">
        No Implementation
        <v-icon color="error" small>mdi-alert-circle-outline</v-icon>
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

      </div>

    </template>

    <template v-slot:[`item.functionDeployment.handler`]="{ item }">

      <div v-if="item.functionDeployment === null">
        No Deployment
        <v-icon color="error" small>mdi-alert-circle-outline</v-icon>
      </div>

      <div v-else>

        <v-tooltip bottom>
          <template v-slot:activator="{ on, attrs }">
        <span
            v-bind="attrs"
            v-on="on"
        >{{ item.functionDeployment.handler }}</span>
          </template>
          <span>{{ item.functionDeployment?.handler }}</span>
        </v-tooltip>

      </div>

    </template>

    <template v-slot:[`item.actions`]="{ item }">

      <v-menu offset-y>
        <template v-slot:activator="{ on, attrs }">
          <v-btn
              icon
              color="primary"
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

    </template>

    <template v-slot:[`item.status`]="{ item }">

      <DeploymentIcon
          :item="item"
      ></DeploymentIcon>

    </template>

    <template v-slot:[`item.link`]="{ item }">

      <LinkIcon
          :item="item"
      ></LinkIcon>

    </template>

  </v-data-table>


</template>

<script>

import FunctionImplementationDialog from "@/components/function/dialogs/FunctionImplementationDialog.vue";
import FunctionDeploymentDialog from "@/components/function/dialogs/FunctionDeploymentDialog.vue";
import DeploymentIcon from "@/components/function/icons/DeploymentIcon.vue";
import LinkIcon from "@/components/function/icons/LinkIcon.vue";

export default {

  components: {LinkIcon, DeploymentIcon, FunctionDeploymentDialog, FunctionImplementationDialog},

  props: {
    search: String,
    functionsFromProps: []
  },

  data: () => ({

    selectedMenuItem: {},

    functions: [],
    selected: [],
    headers: [

      {text: 'Status', value: 'status', sortable: false},
      {text: 'Name', value: 'functionType.name', sortable: true},
      {text: 'Implementation', value: 'implementation', sortable: true},

      {text: 'Handler', value: 'functionDeployment.handler', sortable: true},
      {text: 'Provider', value: 'functionDeployment.provider', sortable: true},
      {text: 'Region', value: 'functionDeployment.region', sortable: true},
      {text: 'Timeout', value: 'functionDeployment.timeoutSecs', sortable: true},
      {text: 'Memory', value: 'functionDeployment.memory', sortable: true},
      {text: 'Runtime', value: 'functionDeployment.runtime', sortable: true},
      {text: 'User', value: 'functionDeployment.userName', sortable: true},

      {text: '', value: 'actions', sortable: false},

      {text: '', value: 'link', sortable: false},

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

  watch: {
    selected() {
      this.$emit('update-selected', this.selected)
    },
    functionsFromProps() {
      this.init()
    }
  },

  methods: {

    init() {
      this.functions = this.functionsFromProps
      this.selectedMenuItem = {};
      this.selected = [];
      this.editItem = {
        functionType: {},
        functionImplementation: {},
        functionDeployment: {}
      };

    },

    addImplementation(item) {
      this.editItem = Object.assign({}, item);
      this.editItem.functionImplementation = {functionTypeId: this.editItem.functionType.id};
      this.functionImplementationDialogVisible = true;
    },

    editImplementation(item) {
      this.editItem = Object.assign({}, item);
      this.functionImplementationDialogVisible = true;
    },

    addDeployment(item) {
      this.editItem = Object.assign({}, item);
      this.editItem.functionDeployment = {functionImplementationId: this.editItem.functionImplementation.id}
      this.functionDeploymentDialogVisible = true;
    },

    editDeployment(item) {
      this.editItem = Object.assign({}, item);
      this.functionDeploymentDialogVisible = true;
    },



  },

}

</script>