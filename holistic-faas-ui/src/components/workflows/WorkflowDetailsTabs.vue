<template>
  <div>

    <!-- Tabs -->
    <v-tabs v-model="tab">

      <v-tab key="deployments">
        Deployments ({{ deployments?.length || 0 }})
      </v-tab>
      <v-tab key="functions">Functions ({{ workflow.functions?.length || 0 }})</v-tab>
      <v-tab key="types">Types ({{ types?.length || 0 }})
        <v-icon v-if="isAnyTypeWithoutImplementation" right color="warning">mdi-alert</v-icon>
      </v-tab>
      <v-tab key="implementations">Implementations ({{ implementations?.length || 0 }})</v-tab>

    </v-tabs>

    <!-- Tab content -->
    <v-card tile elevation="0">
      <v-tabs-items v-model="tab">

        <v-tab-item key="deployments">
          <WorkflowDeployment
              :workflow="workflow"
              :deployments="deployments"/>
        </v-tab-item>

        <v-tab-item key="functions">
          <WorkflowFunctionsTable
              @dialog-closed="$emit('workflow-updated')"
              :workflow="workflow"/>
        </v-tab-item>

        <v-tab-item key="types">
          <WorkflowFunctionTypesTable
              @dialog-closed="$emit('workflow-updated')"
              :workflow="workflow"
              :types="types"
          />
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

import WorkflowFunctionsTable from "@/components/workflows/tables/WorkflowFunctionsTable.vue";
import WorkflowDeployment from "@/components/workflows/tables/WorkflowDeploymentsTable.vue";
import FunctionImplementations from "@/components/workflows/tables/WorkflowImplementationsTable.vue";
import WorkflowFunctionTypesTable from "@/components/workflows/tables/WorkflowFunctionTypesTable.vue";

export default {

  props: {
    workflow: {},
    implementations: {},
    deployments: {},
    types: {},
  },

  components: {WorkflowFunctionTypesTable, FunctionImplementations, WorkflowDeployment, WorkflowFunctionsTable},

  data() {
    return {
      tab: "deployments", // The currently selected tab
    };
  },

  created() {

  },

  computed: {
    isAnyTypeWithoutImplementation() {
      return this.types.some(type => type.functionImplementations.length === 0);
    },
  },

  methods: {

  },

}

</script>