<template>
  <div class="text-center">

    <FunctionMigrationTable
        :dialog.sync="functionMigrationDialog"
        @dialog-closed="close"
        :items="items"
    />

    <WorkflowDeploymentDialog
        :dialog.sync="deploymentDialogVisible"
        @dialog-closed="close"
        :workflow="{functionDefinitions: items}"
    />

    <v-menu
        :disabled="items.length === 0"
        offset-y
    >

      <template v-slot:activator="{ on, attrs }">
        <v-btn
            color="primary"
            v-bind="attrs"
            class="mx-2"
            v-on="on"
            :disabled="items.length === 0"
        >

          <v-icon left>
            mdi-swap-horizontal
          </v-icon>


          Migrate ({{ items.length }})


        </v-btn>
      </template>

      <v-list>
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
            <v-list-item-title @click="openMigrationDialog()">
              <v-list-item-icon>
                <v-icon>mdi-dots-horizontal</v-icon>
              </v-list-item-icon>
              Custom
            </v-list-item-title>
          </v-list-item>

          <v-list-item>
            <v-list-item-title @click="openDeploymentDialog()">
              <v-list-item-icon>
                <v-icon>mdi-dots-horizontal</v-icon>
              </v-list-item-icon>
              Custom 2
            </v-list-item-title>
          </v-list-item>

        </v-list-item-group>
      </v-list>

    </v-menu>
  </div>
</template>

<script>

import HfApi from "@/utils/hf-api";
import FunctionMigrationTable from "@/components/function/dialogs/FunctionMigrationTable.vue";
import WorkflowDeploymentDialog from "@/components/workflows/dialogs/WorkflowDeploymentDialog.vue";

export default {

  components: {WorkflowDeploymentDialog, FunctionMigrationTable},

  props: {
    items: {
      type: Array,
      required: true,
    },
  },

  data: () => ({

    functionMigrationDialog: false,
    deploymentDialogVisible: false,

  }),

  methods: {

    close() {
      this.$emit('menu-closed')
    },

    getMigrateRequest() {
      // Items consists of a list of {id, functionDeployment, functionImplementation and functionType}
      // But the api does not expect the id field, so create a new object without that

      return this.items.map(item => {
        return {
          functionDeployment: item.functionDeployment,
          functionImplementation: item.functionImplementation,
          functionType: item.functionType
        }
      })
    },

    migrateToMyAccount() {

      HfApi.prepareMigration(this.getMigrateRequest(), this.$store.state.userId, 'FUNCTION_USER')
          .then((response) => {
            HfApi.migrateFunctions(response.data)
                .then(() => {
                  this.close()
                })

          })

    },

    migrateToProvider(provider) {

      HfApi.prepareMigration(this.getMigrateRequest(), provider, 'FUNCTION_PROVIDER')
          .then((response) => {
            HfApi.migrateFunctions(response.data)
                .then(() => {
                  this.close()
                })

          })

    },

    openMigrationDialog() {
      this.functionMigrationDialog = true
    },

    openDeploymentDialog() {
      this.deploymentDialogVisible = true
    }

  }

}
</script>