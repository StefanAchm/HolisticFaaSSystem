<template>

  <v-data-table
      :headers="headers"
      :items="functions"
  >

    <template v-slot:top>

      <v-toolbar flat>

        <v-toolbar-title>Functions</v-toolbar-title>

        <!--        <v-divider class="mx-4" inset vertical></v-divider>-->

        <v-spacer></v-spacer>

        <v-btn
            color="primary"
            class="mx-2"
            @click="functionDialogVisible = true">

          Add Function

        </v-btn>

        <FunctionTypeDialog
            :dialog.sync="functionDialogVisible"
            @dialog-closed="loadFunctions"
            :edit-item="{}"
        />

        <FunctionDeploymentDialog
            :dialog.sync="functionDeploymentDialogVisible"
            @dialog-closed="loadFunctions"
            :edit-item="{}"
            :function-implementation="editItem.functionImplementation"/>

        <FunctionImplementationDialog
            :dialog.sync="functionImplementationDialogVisible"
            @dialog-closed="loadFunctions"
            :edit-item="{}"
            :function-type="editItem.functionType"
        />

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
          <strong> UserName: </strong> {{ item.functionDeployment.userName }} <br>

        </span>

<!--        <v-icon @click="functionDeploymentDialogVisible = true;">mdi-plus</v-icon>-->


      </div>

    </template>

    <template v-slot:[`item.actionsI`]="{ item }">

      <v-icon @click="openImplementationDialog(item)">mdi-cogs</v-icon>

    </template>

    <template v-slot:[`item.actionsD`]="{ item }">

      <v-icon @click="openDeploymentDialog(item)">mdi-rocket</v-icon>

    </template>



  </v-data-table>


</template>

<script>


import HfApi from "@/utils/hf-api";
import FunctionTypeDialog from "@/components/FunctionTypeDialog.vue";
import FunctionImplementationDialog from "@/components/FunctionImplementationDialog.vue";
import FunctionDeploymentDialog from "@/components/FunctionDeploymentDialog.vue";

export default {

  components: {FunctionDeploymentDialog, FunctionImplementationDialog, FunctionTypeDialog},

  data: () => ({

    functions: [],
    headers: [

      {text: 'Name', value: 'type', sortable: true},
      {text: 'Implementation', value: 'implementation', sortable: true},
      {text: 'Deployment', value: 'deployment', sortable: true},
      {text: 'Add Implementation', value: 'actionsI', sortable: true},
      {text: 'Add Deployment', value: 'actionsD', sortable: true},

    ],

    functionDialogVisible: false,
    functionImplementationDialogVisible: false,
    functionDeploymentDialogVisible: false,

    editItem : {
      functionType: {},
      functionImplementation: {},
      functionDeployment: {}
    },


  }),

  created() {
    this.loadFunctions();
  },

  methods: {

    loadFunctions() {
      console.log('loadFunctions')
      HfApi.getAllFunctions()
          .then(response => {
            this.functions = response.data;
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

  },


}

</script>