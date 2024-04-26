<template>
  <v-card>

    <v-toolbar>

      <v-toolbar-title> {{ workflow.name }}</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn color="primary" class="mx-2" @click="openFunctionImplementationDialog">Add Implementation</v-btn>
      <v-btn color="primary" class="mx-2" @click="openDeploymentDialog">Add Deployment</v-btn>

      <FunctionImplementationDialogExtended
          :dialog.sync="functionImplementationDialogVisible"
          :editItem="{}"
          :function-types="getFunctionTypes()"
          @dialog-closed="loadWorkflow"
      />

      <WorkflowDeploymentDialog
          :dialog.sync="deploymentDialogVisible"
          :workflow="workflow"
          @dialog-closed="loadWorkflow"
      />


    </v-toolbar>

    <v-card-text>{{ workflow.description }}</v-card-text>

    <v-tabs vertical v-model="tab">

      <v-tab key="abstract">Functions</v-tab>
      <v-tab key="implementations">Implementations</v-tab>
      <v-tab key="deployments">Deployments</v-tab>

      <v-tabs-items v-model="tab">
        <v-tab-item key="abstract">
          <WorkflowAbstract :workflow="workflow"/>
        </v-tab-item>
        <v-tab-item key="implementations">
          <FunctionImplementations :workflow="workflow"/>
        </v-tab-item>
        <v-tab-item key="deployments">
          <WorkflowDeployment :workflow="workflow"/>
        </v-tab-item>

      </v-tabs-items>
    </v-tabs>

  </v-card>
</template>

<script>

import HfApi from "@/utils/hf-api";
import WorkflowAbstract from "@/components/workflows/WorkflowAbstract.vue";
import WorkflowDeployment from "@/components/workflows/WorkflowDeployment.vue";
import FunctionImplementations from "@/components/workflows/FunctionImplementations.vue";
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
      tab: "abstract", // The currently selected tab
      functionImplementationDialogVisible: false,
      deploymentDialogVisible: false,
    };
  },
  created() {
    this.loadWorkflow();
  },
  methods: {

    openFunctionImplementationDialog() {
      this.functionImplementationDialogVisible = true;
    },

    openDeploymentDialog() {
      this.deploymentDialogVisible = true;
    },

    getFunctionTypes() {
      return this.workflow.functions?.map(f => f.functionType);
    },

    loadWorkflow() {
      HfApi.getWorkflow(this.$route.params.id)
          .then(response => {
            this.workflow = response.data;
          })
          .catch(error => {
            console.error("Failed to load workflows:", error);
          });
    },
  },

}

</script>