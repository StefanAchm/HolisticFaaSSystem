<template>

  <v-card>

    <WorkflowBreadCrumbs></WorkflowBreadCrumbs>

    <WorkflowHeader
        @workflow-added="loadWorkflows"
    ></WorkflowHeader>

    <WorkflowTable
        ref="workflowTable"
        :workflows-from-props="workflows"
    ></WorkflowTable>

  </v-card>

</template>

<script>

import WorkflowTable from "@/components/workflows/tables/WorkflowsTable.vue";
import WorkflowHeader from "@/components/workflows/headers/WorkflowsHeader.vue";
import HfApi from "@/utils/hf-api";
import WorkflowBreadCrumbs from "@/components/workflows/WorkflowBreadCrumbs.vue";

export default {
  components: {WorkflowBreadCrumbs, WorkflowHeader, WorkflowTable},

  data: () => ({
    workflows: [],
  }),

  created() {
    this.loadWorkflows();
  },

  watch: {},

  computed: {},

  methods: {

    loadWorkflows() {

      HfApi.getAllWorkflows()
          .then((response) => {
            this.workflows = response.data
          })
          .catch(() => {
            this.error = 'Could not load workflows'
          })

    }

  }

}

</script>