<template>

  <v-card>

    <WorkflowBreadCrumbs></WorkflowBreadCrumbs>

    <WorkflowDetailsHeader
        :workflow="workflow"
        @workflow-updated="loadWorkflow"
    ></WorkflowDetailsHeader>

    <WorkflowDetailsTabs
        :workflow="workflow"
        :deployments="deployments"
        :implementations="implementations"
    />

  </v-card>

</template>

<script>

import WorkflowDetailsHeader from "@/components/workflows/headers/WorkflowDetailsHeader.vue";
import WorkflowBreadCrumbs from "@/components/workflows/WorkflowBreadCrumbs.vue";
import HfApi from "@/utils/hf-api";
import WorkflowDetailsTabs from "@/components/workflows/WorkflowDetailsTabs.vue";

export default {
  components: {WorkflowDetailsTabs, WorkflowBreadCrumbs, WorkflowDetailsHeader},

  data: () => ({
    workflow: {},
    deployments: [],
    implementations: []
  }),

  created() {
    this.init()
  },

  watch: {},

  computed: {

  },

  methods: {

    init() {
      this.loadWorkflow();
    },

    setImplementations() {
      // this.functions = ; // Flatten this list!
      // Each function has a name and a type, but multiple implementations
      // We need to flatten this list to show all implementations
      this.implementations = this.workflow.functions?.map(function (functionItem) {

        if (functionItem.functionType.functionImplementations == null || functionItem.functionType.functionImplementations.length === 0) {
          return [];
        }

        return functionItem.functionType.functionImplementations.map(function (functionImplementation) {
          return {
            function: functionItem,
            functionImplementation: functionImplementation
          }
        });
      }).flat()
          .filter(item => Object.keys(item).length !== 0); // Filter out empty objects
    },

    loadDeployments() {
      HfApi.getDeployments(this.workflow.id)
          .then(response => {
            this.deployments = response.data;
          })
          .catch(error => {
            console.error("Failed to load deployments:", error);
          });
    },

    loadWorkflow() {
      HfApi.getWorkflow(this.$route.params.id)
          .then(response => {

            this.workflow = response.data;

            this.loadDeployments()
            this.setImplementations()

          })
          .catch(error => {
            console.error("Failed to load workflows:", error);
          });

    },

  }

}

</script>