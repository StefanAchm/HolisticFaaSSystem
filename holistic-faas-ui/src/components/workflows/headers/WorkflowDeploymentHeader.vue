<template>

  <!-- Header -->
  <v-toolbar flat fluid>
    <v-toolbar-title>
      Deployment: {{ workflowDeployment.name }} of {{ workflowDeployment.user?.username }}
    </v-toolbar-title>

    <v-spacer></v-spacer>

    <!--      <v-btn color="primary" class="mx-2" @click="openFunctionImplementationDialog">Download</v-btn>-->
    <v-btn
        :disabled="undeployedFunctions?.length === 0"
        color="primary"
        class="mx-2"
        @click="deployAll">

      Deploy ({{ undeployedFunctions?.length }})

    </v-btn>

    <WorkflowMigrateButton
        :workflow-deployment="workflowDeployment"
    ></WorkflowMigrateButton>

    <!--      <v-btn color="primary" class="mx-2" @click="openDeploymentDialog">Migrate</v-btn>-->

  </v-toolbar>


</template>

<script>

import WorkflowMigrateButton from "@/components/workflows/WorkflowMigrateButton.vue";
import HfApi from "@/utils/hf-api";

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

    deployAll() {

      this.workflowDeployment.functionDefinitions.forEach(item => {
        HfApi.deployFunctionDeployment(item.functionDeployment.id)
      })

    },

  }

}

</script>