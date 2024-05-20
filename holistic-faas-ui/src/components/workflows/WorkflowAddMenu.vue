<template>

  <div>

    <!-- Dialogs -->
    <FunctionImplementationDialogExtended
        :dialog.sync="functionImplementationDialogVisible"
        :workflow="workflow"
        @dialog-closed="dialogClosed()"
    />

    <FunctionWithTypeDialog
        :dialog.sync="functionDialogVisible"
        :workflow="workflow"
        @dialog-closed="dialogClosed()"
    />

    <WorkflowDeploymentDialog
        :dialog.sync="deploymentDialogVisible"
        :workflow-deployment="workflowDeployment"
        @dialog-closed="dialogClosed()"
    />

    <v-menu offset-y>
      <template v-slot:activator="{ on, attrs }">
        <v-btn
            color="primary"
            class="mx-2"
            v-bind="attrs"
            v-on="on"
        >
          <v-icon left>mdi-plus</v-icon>

          Add

        </v-btn>

      </template>

      <v-list dense>
        <v-list-item @click="openDeploymentDialog">
          <v-list-item-icon>
            <v-icon>mdi-rocket</v-icon>
          </v-list-item-icon>
          <v-list-item-title>Add Deployment</v-list-item-title>
        </v-list-item>
        <v-list-item
            :disabled="deployments?.length > 0"
            @click="openFunctionDialog">
          <v-list-item-icon>
            <v-icon>mdi-lambda</v-icon>
          </v-list-item-icon>
          <v-list-item-title>Add Function</v-list-item-title>
        </v-list-item>
        <v-list-item @click="openFunctionImplementationDialog">
          <v-list-item-icon>
            <v-icon>mdi-folder-zip-outline</v-icon>
          </v-list-item-icon>
          <v-list-item-title>Add Implementation</v-list-item-title>
        </v-list-item>
      </v-list>

    </v-menu>

  </div>

</template>

<script>
import FunctionWithTypeDialog from "@/components/function/dialogs/FunctionWithTypeDialog.vue";
import WorkflowDeploymentDialog from "@/components/workflows/dialogs/WorkflowDeploymentDialog.vue";
import FunctionImplementationDialogExtended
  from "@/components/function/dialogs/FunctionImplementationDialogExtended.vue";

export default {

  components: {FunctionImplementationDialogExtended, WorkflowDeploymentDialog, FunctionWithTypeDialog},

  props: {
    workflow: {
      type: Object,
      required: true,
    },
    workflowDeployment: {
      type: Object,
      required: true,
    },
    deployments: {
      type: Array,
      required: false
    }
  },

  data() {
    return {
      functionImplementationDialogVisible: false,
      deploymentDialogVisible: false,
      functionDialogVisible: false,
    };
  },

  methods: {
    openDeploymentDialog() {
      this.deploymentDialogVisible = true;
    },
    openFunctionDialog() {
      this.functionDialogVisible = true;
    },
    openFunctionImplementationDialog() {
      this.functionImplementationDialogVisible = true;
    },
    dialogClosed() {
      this.$emit('workflow-updated');
    },
  },
};
</script>