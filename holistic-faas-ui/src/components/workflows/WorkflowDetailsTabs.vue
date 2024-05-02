<template>
  <div>

    <!-- Tabs -->
    <v-tabs v-model="tab" class="pl-5">

      <v-tab key="deployments">
        Deployments ({{ deployments?.length || 0 }})
      </v-tab>
      <v-tab key="abstract">Functions ({{ workflow.functions?.length || 0 }})</v-tab>
      <v-tab key="implementations">Implementations ({{ implementations?.length || 0 }})</v-tab>

    </v-tabs>

    <!-- Tab content -->
    <v-card tile>
      <v-tabs-items v-model="tab">

        <v-tab-item key="deployments">
          <WorkflowDeployment
              :workflow="workflow"
              :deployments="deployments"/>
        </v-tab-item>

        <v-tab-item key="abstract">
          <WorkflowAbstract
              @dialog-closed="this.$emit('workflow-updated')"
              :workflow="workflow"/>
        </v-tab-item>

        <v-tab-item key="implementations">
          <FunctionImplementations
              :functions="implementations"/>
        </v-tab-item>

      </v-tabs-items>
    </v-card>


  </div>
</template>

<script>

import WorkflowAbstract from "@/components/workflows/tables/WorkflowFunctionsTable.vue";
import WorkflowDeployment from "@/components/workflows/tables/WorkflowDeploymentsTable.vue";
import FunctionImplementations from "@/components/workflows/tables/WorkflowImplementationsTable.vue";

export default {

  props: {
    workflow: {},
    implementations: {},
    deployments: {}
  },

  components: {FunctionImplementations, WorkflowDeployment, WorkflowAbstract},

  data() {
    return {
      tab: "deployments", // The currently selected tab
    };
  },

  created() {

  },

  methods: {

  },

}

</script>