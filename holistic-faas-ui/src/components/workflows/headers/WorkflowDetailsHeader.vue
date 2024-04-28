<template>
  <div>

    <!-- Header -->
    <v-toolbar flat fluid>
      <v-toolbar-title>Workflow: {{ workflow.name }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn color="primary" class="mx-2" @click="openFunctionImplementationDialog">Add Implementation</v-btn>
      <v-btn color="primary" class="mx-2" @click="openDeploymentDialog">Add Deployment</v-btn>
    </v-toolbar>

    <!-- Tabs -->
    <v-tabs v-model="tab" class="pl-5">

      <v-tab key="abstract">Functions ({{ workflow.functions?.length || 0 }})</v-tab>
      <v-tab key="implementations">Implementations ({{ implementations?.length || 0 }})</v-tab>
      <v-tab key="deployments">Deployments ({{ deployments?.length || 0 }})</v-tab>

    </v-tabs>

    <!-- Tab content -->
    <v-card tile>
      <v-tabs-items v-model="tab">
        <v-tab-item key="abstract">
          <WorkflowAbstract :workflow="workflow"/>
        </v-tab-item>
        <v-tab-item key="implementations">
          <FunctionImplementations :functions="implementations"/>
        </v-tab-item>
        <v-tab-item key="deployments">
          <WorkflowDeployment :workflow="workflow" :deployments="deployments"/>
        </v-tab-item>
      </v-tabs-items>
    </v-card>

    <!-- Dialogs -->
    <FunctionImplementationDialogExtended
        :dialog.sync="functionImplementationDialogVisible"
        :editItem="{}"
        :function-types="getFunctionTypes()"
        @dialog-closed="loadWorkflow"
    />

    <WorkflowDeploymentDialog
        :dialog.sync="deploymentDialogVisible"
        :workflow-deployment="workflowDeployment"
        @dialog-closed="loadWorkflow"
    />
  </div>
</template>

<script>

import HfApi from "@/utils/hf-api";
import WorkflowAbstract from "@/components/workflows/tables/WorkflowFunctionsTable.vue";
import WorkflowDeployment from "@/components/workflows/tables/WorkflowDeploymentsTable.vue";
import FunctionImplementations from "@/components/workflows/tables/WorkflowImplementationsTable.vue";
import FunctionImplementationDialogExtended
  from "@/components/function/dialogs/FunctionImplementationDialogExtended.vue";
import WorkflowDeploymentDialog from "@/components/workflows/WorkflowDeploymentDialog.vue";

export default {
  components: {
    WorkflowDeploymentDialog,
    FunctionImplementationDialogExtended, FunctionImplementations, WorkflowDeployment, WorkflowAbstract},
  data() {
    return {
      workflow: {}, // This will hold the fetched workflow
      workflowDeployment: {}, // This will hold the fetched workflow deployment
      tab: "abstract", // The currently selected tab
      functionImplementationDialogVisible: false,
      deploymentDialogVisible: false,
      deployments: {},

    };
  },

  created() {
    this.loadWorkflow();
    this.getWorkflowDeployment();
  },

  computed: {
    implementations() {
      // this.functions = ; // Flatten this list!
      // Each function has a name and a type, but multiple implementations
      // We need to flatten this list to show all implementations
      return this.workflow.functions?.map(function (functionItem) {

        if (functionItem.functionType.functionImplementations == null || functionItem.functionType.functionImplementations.length === 0) {
          return [];
        }

        return functionItem.functionType.functionImplementations.map(function (functionImplementation) {
          return {
            function: functionItem,
            functionImplementation: functionImplementation
          }
        });
      }).flat()
          .filter(item => Object.keys(item).length !== 0); // Filter out empty objects
    }
  },

  methods: {

    openFunctionImplementationDialog() {
      this.functionImplementationDialogVisible = true;
    },

    openDeploymentDialog() {
      this.deploymentDialogVisible = true;
    },

    getWorkflowDeployment() {

      HfApi.prepareWorkflowDeployment(this.$route.params.id).then((response) => {

        this.workflowDeployment = response.data

      })

    },

    loadDeployments() {
      HfApi.getDeployments(this.workflow.id)
          .then(response => {
            this.deployments = response.data;
          })
          .catch(error => {
            console.error("Failed to load deployments:", error);
          });
    },

    getFunctionTypes() {
      return this.workflow.functions?.map(f => f.functionType);
    },

    loadWorkflow() {
      HfApi.getWorkflow(this.$route.params.id)
          .then(response => {
            this.workflow = response.data;
            this.loadDeployments();
          })
          .catch(error => {
            console.error("Failed to load workflows:", error);
          });
    },
  },

}

</script>