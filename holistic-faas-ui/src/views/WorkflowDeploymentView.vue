<template>

  <v-card>

    <WorkflowBreadCrumbs></WorkflowBreadCrumbs>

    <WorkflowDeploymentHeader
        :workflow-deployment="workflowDeployment"
    ></WorkflowDeploymentHeader>

    <WorkflowDeploymentTable
        :workflow-deployment="workflowDeployment"
    ></WorkflowDeploymentTable>

  </v-card>

</template>

<script>

import WorkflowBreadCrumbs from "@/components/workflows/WorkflowBreadCrumbs.vue";
import HfApi from "@/utils/hf-api";
import WorkflowDeploymentTable from "@/components/workflows/tables/WorkflowDeploymentTable.vue";
import WorkflowDeploymentHeader from "@/components/workflows/headers/WorkflowDeploymentHeader.vue";

export default {
  components: {WorkflowDeploymentHeader, WorkflowDeploymentTable, WorkflowBreadCrumbs},

  data: () => ({
    workflowDeployment: {},
  }),

  created() {
    this.loadWorkflowDeployment()
  },

  watch: {
    '$route'() {
      this.loadWorkflowDeployment()
    }
  },

  computed: {},

  methods: {

    loadWorkflowDeployment() {

      HfApi.getWorkflowDeployment(this.$route.params.deploymentId)
          .then((response) => {

            this.workflowDeployment = response.data

          })
          .catch(() => {
            this.error = 'Could not load functions'
          })

    }

  }

}

</script>