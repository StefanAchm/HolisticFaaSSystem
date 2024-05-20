<template>

  <v-card>

    <WorkflowBreadCrumbs
        :workflow="workflow"
    ></WorkflowBreadCrumbs>

    <WorkflowDetailsHeader
        :workflow="workflow"
        :deployments="deployments"
        @workflow-updated="loadWorkflow"
    ></WorkflowDetailsHeader>

    <WorkflowDetailsTabs
        :workflow="workflow"
        :deployments="deployments"
        :implementations="implementations"
        :types="functionTypes"
        @workflow-updated="loadWorkflow"
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
    implementations: [],
    functionTypes: []
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

      this.implementations = []
      for(let functionType of this.functionTypes) {
        for(let functionImplementation of functionType.functionImplementations) {
          this.implementations.push({
            functionType: functionType,
            functionImplementation: functionImplementation
          })
        }
      }

    },

    setTypes() {

      this.functionTypes = this.workflow.functions
          .map(function (functionItem) {
            return functionItem.functionType;
          })
          .filter((functionType, index, self) =>
                  index === self.findIndex((t) => (
                      t.id === functionType.id
                  ))
          );

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
            this.setTypes()
            this.setImplementations()

          })
          .catch(error => {
            console.error("Failed to load workflows:", error);
          });

    },

  }

}

</script>