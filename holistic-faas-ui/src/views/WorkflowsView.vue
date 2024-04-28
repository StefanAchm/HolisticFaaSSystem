<template>

  <v-card>

    <WorkflowBreadCrumps></WorkflowBreadCrumps>

    <WorkflowHeader
        @workflow-added="loadWorkflows"
    ></WorkflowHeader>

    <WorkflowTable
        ref="workflowTable"
        :workflowsFromProps="workflows"
    ></WorkflowTable>



  </v-card>

</template>

<script>

import WorkflowTable from "@/components/workflows/tables/WorkflowsTable.vue";
import WorkflowHeader from "@/components/workflows/headers/WorkflowsHeader.vue";
import HfApi from "@/utils/hf-api";
import WorkflowBreadCrumps from "@/components/workflows/WorkflowBreadCrumps.vue";

export default {
  components: {WorkflowBreadCrumps, WorkflowHeader, WorkflowTable},

  data: () => ({
    // selected: [],
    // search: '',
    workflows: [],
    // withFileDrop: false, // Because this does not work atm!
  }),

  created() {
    this.loadWorkflows()
  },

  watch: {

  },

  computed: {

  },

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