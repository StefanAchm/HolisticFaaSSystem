<template>
  <div>

    <!-- Header -->
    <v-toolbar flat fluid>

      <v-toolbar-title>{{ workflow.name }}</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn color="primary" :disabled="!workflow.filePath" class="mx-2" @click="downloadWorkflow">

        <v-icon
            left>
          mdi-download
        </v-icon>

        Download Definition

      </v-btn>


      <v-btn
          color="primary"
          class="mx-2"
          :disabled="deployments?.length > 0"
          @click="openFunctionDialog">

        <v-icon
            left>
          mdi-plus
        </v-icon>


        Add Function
      </v-btn>


      <v-btn
          color="primary"
          class="mx-2"
          @click="openFunctionImplementationDialog">

        <v-icon
            left>
          mdi-plus
        </v-icon>

        Add Implementation

      </v-btn>


      <v-btn
          color="primary"
          class="mx-2"
          @click="openDeploymentDialog">

        <v-icon
            left>
          mdi-plus
        </v-icon>

        Add Deployment

      </v-btn>


    </v-toolbar>

    <!-- Add a v-card here to display the workflow details -->
    <v-card elevation="0" class="pb-6">
      <!--      <v-card-title> Details: </v-card-title>-->

      <v-card-text>
        <strong> Created by: </strong>
        {{ workflow.createdBy }}
      </v-card-text>

      <v-card-text>
        <strong> Description: </strong>
        {{ workflow.description }}
      </v-card-text>

    </v-card>

    <!-- Dialogs -->
    <FunctionImplementationDialogExtended
        :dialog.sync="functionImplementationDialogVisible"
        :workflow="workflow"
        @dialog-closed="dialogClosed()"
    />

    <FunctionWithTypeDialog
        :dialog.sync="functionDialogVisible"
        :workflow="workflow"
        @dialog-closed="dialogClosed()"
    />

    <WorkflowDeploymentDialog
        :dialog.sync="deploymentDialogVisible"
        :workflow-deployment="workflowDeployment"
        @dialog-closed="dialogClosed()"
    />
  </div>
</template>

<script>

import HfApi from "@/utils/hf-api";
import FunctionImplementationDialogExtended
  from "@/components/function/dialogs/FunctionImplementationDialogExtended.vue";
import WorkflowDeploymentDialog from "@/components/workflows/dialogs/WorkflowDeploymentDialog.vue";
import FunctionWithTypeDialog from "@/components/function/dialogs/FunctionWithTypeDialog.vue";
import download from "@/utils/download";

export default {

  props: {
    workflow: {
      type: Object,
      required: true
    },
    deployments: {
      type: Array,
      required: false
    }
  },

  components: {
    FunctionWithTypeDialog,
    WorkflowDeploymentDialog,
    FunctionImplementationDialogExtended
  },

  data() {

    return {

      workflowDeployment: {}, // This will hold the fetched workflow deployment

      functionImplementationDialogVisible: false,
      deploymentDialogVisible: false,
      functionDialogVisible: false

    };

  },

  created() {
    this.getWorkflowDeployment();
  },

  computed: {},

  methods: {

    dialogClosed() {
      this.getWorkflowDeployment();
      this.$emit('workflow-updated');
    },

    openFunctionDialog() {
      this.functionDialogVisible = true;
    },

    openFunctionImplementationDialog() {
      this.functionImplementationDialogVisible = true;
    },

    openDeploymentDialog() {
      this.deploymentDialogVisible = true;
    },

    downloadWorkflow() {

      HfApi.downloadWorkflow(this.workflow.id)
          .then((response) => {
                download.downloadFile(response.data, this.workflow.name + '.yaml')
              }
          )

    },

    getWorkflowDeployment() {

      HfApi.prepareWorkflowDeployment(this.$route.params.id)
          .then((response) => {

            this.workflowDeployment = response.data

          })

    },

  },

}

</script>