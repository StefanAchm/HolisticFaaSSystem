<template>

  <v-card>

    <WorkflowBreadCrumbs
        :workflow="workflow"
        :workflow-deployment="workflowDeployment"
    ></WorkflowBreadCrumbs>

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
    workflow: {}
  }),

  created() {
    this.loadWorkflowDeployment()
    this.loadWorkflow()
  },

  watch: {
    '$route'() {
      this.loadWorkflowDeployment()
    }
  },

  computed: {},

  methods: {

    loadWorkflow() {
      HfApi.getWorkflow(this.$route.params.id)
          .then(response => {
            this.workflow = response.data;
          })
          .catch(error => {
            console.error("Failed to load workflows:", error);
          });

    },

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