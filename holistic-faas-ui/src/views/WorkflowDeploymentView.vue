<template>

  <v-card>

    <WorkflowBreadCrumps></WorkflowBreadCrumps>

    <WorkflowDeploymentTable
        :workflow-deployment-from-props="workflowDeployment"
    ></WorkflowDeploymentTable>


  </v-card>

</template>

<script>

import WorkflowBreadCrumps from "@/components/workflows/WorkflowBreadCrumps.vue";
import HfApi from "@/utils/hf-api";
import WorkflowDeploymentTable from "@/components/workflows/tables/WorkflowDeploymentTable.vue";

export default {
  components: {WorkflowDeploymentTable, WorkflowBreadCrumps},

  data: () => ({
    selected: [],
    workflowDeployment: {},
  }),

  created() {
    this.loadWorkflowDeployment()
  },

  watch: {
    '$route': function () {
      this.loadWorkflowDeployment()
    }
  },

  computed: {

  },

  methods: {

    loadWorkflowDeployment() {

      HfApi.getWorkflowDeployment(this.$route.params.deploymentId)
          .then((response) => {

            this.workflowDeployment = response.data
            this.selected = this.workflowDeployment.functionDefinitions

          })
          .catch(() => {
            this.error = 'Could not load functions'
          })

    }

  }

}

</script>