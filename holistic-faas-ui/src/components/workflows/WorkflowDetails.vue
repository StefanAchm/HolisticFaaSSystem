<template>
  <div>
    <!-- Header -->
    <v-toolbar flat fluid>
      <v-toolbar-title>Workflow: {{ workflow.name }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn color="primary" class="mx-2" @click="openFunctionImplementationDialog">Add Implementation</v-btn>
      <v-btn color="primary" class="mx-2" @click="openDeploymentDialog">Add Deployment</v-btn>
    </v-toolbar>

    <!-- Breadcrumbs -->
<!--    <WorkflowBreadCrumps />-->

    <!-- Tabs -->
    <v-tabs v-model="tab" class="pl-5">

      <v-tab key="abstract">Functions</v-tab>
      <v-tab key="implementations">Implementations</v-tab>
      <v-tab key="deployments">Deployments</v-tab>

    </v-tabs>

    <!-- Tab content -->
    <v-card tile>
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
      workflowDeployment: {}, // This will hold the fetched workflow deployment
      tab: "abstract", // The currently selected tab
      functionImplementationDialogVisible: false,
      deploymentDialogVisible: false,
    };
  },
  created() {
    this.loadWorkflow();
    this.getWorkflowDeployment();
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

      // HfApi.getWorkflowFunctionImplementations(this.workflow.id).then((response) => {
      //
      //   let functionImplementations = response.data
      //
      //   HfApi.prepareWorkflowDeployment(this.workflow.id).then((response) => {
      //
      //     this.workflowDeployment = response.data
      //
      //     for(let i = 0; i < this.workflowDeployment.functionDefinitions.length; i++) {
      //
      //       this.workflowDeployment.functionDefinitions[i].functionImplementations = functionImplementations
      //           .filter((impl) => impl.functionTypeId === this.workflowDeployment.functionDefinitions[i].functionType.id)
      //           .map((item) => {
      //             return {
      //               title: item.fileName,
      //               value: item.id
      //             }
      //           })
      //
      //       this.workflowDeployment.functionDefinitions[i].functionImplementation.id = this.workflowDeployment.functionDefinitions[i].functionImplementations[0].value
      //
      //     }
      //
      //   })
      //
      //
      // })

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