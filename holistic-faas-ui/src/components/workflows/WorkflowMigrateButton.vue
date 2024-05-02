<template>
  <div class="text-center">

    <WorkflowDeploymentDialog
        :dialog.sync="deploymentDialogVisible"
        @dialog-closed="close"
        :workflow-deployment="workflowDeploymentForMigration"
    />

    <v-menu
        :disabled="workflowDeployment.functionDefinitions?.length === 0"
        offset-y
    >

      <template v-slot:activator="{ on, attrs }">
        <v-btn
            color="primary"
            v-bind="attrs"
            class="mx-2"
            v-on="on"
            :disabled="workflowDeployment.functionDefinitions?.length === 0"
        >

          <v-icon left>
            mdi-swap-horizontal
          </v-icon>


          Migrate ({{ workflowDeployment.functionDefinitions?.length }})


        </v-btn>
      </template>

      <v-list dense>
        <v-list-item-group>

          <v-list-item>
            <v-list-item-title @click="migrateToMyAccount()">
              <v-list-item-icon>
                <v-icon>mdi-account-arrow-left</v-icon>
              </v-list-item-icon>
              to my account
            </v-list-item-title>
          </v-list-item>

          <v-list-item>
            <v-list-item-title @click="migrateToProvider('AWS')">
              <v-list-item-icon>
                <v-icon>mdi-aws</v-icon>
              </v-list-item-icon>
              to AWS
            </v-list-item-title>
          </v-list-item>

          <v-list-item>
            <v-list-item-title @click="migrateToProvider('GCP')">
              <v-list-item-icon>
                <v-icon>mdi-google-cloud</v-icon>
              </v-list-item-icon>
              to GCP
            </v-list-item-title>
          </v-list-item>

          <v-list-item>
            <v-list-item-title @click="openDeploymentDialog()">
              <v-list-item-icon>
                <v-icon>mdi-dots-horizontal</v-icon>
              </v-list-item-icon>
              Custom
            </v-list-item-title>
          </v-list-item>

        </v-list-item-group>
      </v-list>

    </v-menu>
  </div>
</template>

<script>

import HfApi from "@/utils/hf-api";
import WorkflowDeploymentDialog from "@/components/workflows/dialogs/WorkflowDeploymentDialog.vue";

export default {

  components: {WorkflowDeploymentDialog},

  props: {
    workflowDeployment: {
      type: Object,
      required: true
    },
  },

  data: () => ({

    workflowDeploymentForMigration: {},
    deploymentDialogVisible: false,

  }),

  methods: {

    close(workflowDeployment, target) {

      // this.$emit('menu-closed', workflowDeployment)

      if (workflowDeployment?.workflow?.id && workflowDeployment?.id) {

        this.$root.snackbar.showSuccess({message: 'Workflow migrated to ' + target})

        this.$router.push({
          name: 'deployments',
          params: {id: workflowDeployment.workflow.id, deploymentId: workflowDeployment.id}
        });

      }

    },

    migrateToMyAccount() {

      let migration = {
        migrationType: 'FUNCTION_USER',
        target: this.$store.state.userId,
        workflowDeployment: this.workflowDeployment
      }

      migration.workflowDeployment.user.id = this.$store.state.userId

      HfApi.migrateWorkflowDeployment(migration)
          .then((response) => {

            if (!response.data.valid) {
              this.workflowDeploymentForMigration = response.data
              this.deploymentDialogVisible = true
              this.$root.snackbar.showWarning({message: 'Could not auto migrate, please manually select deployment details'})
            } else {
              this.close(response.data, this.$store.state.username)
            }

          })

    },

    migrateToProvider(provider) {

      let migration = {
        migrationType: 'FUNCTION_PROVIDER',
        target: provider,
        workflowDeployment: this.workflowDeployment
      }

      HfApi.migrateWorkflowDeployment(migration).then((response) => {

        if (!response.data.valid) {
          this.workflowDeploymentForMigration = response.data
          this.deploymentDialogVisible = true
          this.$root.snackbar.showWarning({message: 'Could not auto migrate, please manually select deployment details'})
        } else {
          this.close(response.data, migration.target)
        }

      })

    },

    openDeploymentDialog() {

      this.workflowDeploymentForMigration = this.workflowDeployment
      this.deploymentDialogVisible = true

    }

  }

}
</script>