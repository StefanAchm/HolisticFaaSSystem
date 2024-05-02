<template>

  <div>

    <!-- Header -->
    <v-toolbar flat fluid>
      <v-toolbar-title>
        {{ workflowDeployment.name }}
      </v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn color="primary" class="mx-2" @click="downloadWorkflow">

        <v-icon
            left>
          mdi-download
        </v-icon>

        Download

      </v-btn>

      <v-btn
          :disabled="undeployedFunctions?.length === 0"
          color="primary"
          class="mx-2"
          @click="deployAll">

        <v-icon
            left>
          mdi-rocket-launch
        </v-icon>

        Deploy ({{ undeployedFunctions?.length }})

      </v-btn>

      <WorkflowMigrateButton
          :workflow-deployment="workflowDeployment"
      ></WorkflowMigrateButton>

      <!--      <v-btn color="primary" class="mx-2" @click="openDeploymentDialog">Migrate</v-btn>-->

    </v-toolbar>

    <!-- Add a v-card here to display the workflow details -->
    <v-card elevation="0" class="pb-6">
      <!--      <v-card-title> Details: </v-card-title>-->
      <v-card-text>
        <strong> Created by: </strong>
        {{ workflowDeployment.user?.username }}
      </v-card-text>

    </v-card>

  </div>

</template>

<script>

import WorkflowMigrateButton from "@/components/workflows/WorkflowMigrateButton.vue";
import HfApi from "@/utils/hf-api";
import download from "@/utils/download";

export default {

  components: {WorkflowMigrateButton},

  props: {
    search: String,
    workflowDeployment: {
      type: Object,
      required: true
    },
  },

  computed: {
    undeployedFunctions() {
      return this.workflowDeployment.functionDefinitions?.filter(item => item.functionDeployment.status !== 'DEPLOYED')
    }
  },

  methods: {

    downloadWorkflow() {
      HfApi.downloadWorkflowDeployment(this.workflowDeployment.id).then(response => {
        download.downloadFile(response.data, this.workflowDeployment.name + '.zip')
      })
    },

    deployAll() {

      this.workflowDeployment.functionDefinitions.forEach(item => {
        HfApi.deployFunctionDeployment(item.functionDeployment.id)
      })

    },

  }

}

</script>