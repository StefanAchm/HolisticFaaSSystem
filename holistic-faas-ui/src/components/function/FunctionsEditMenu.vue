<template>
  <div class="text-center">

    <FunctionMigrationProviderDialog
        :dialog.sync="functionMigrationProviderDialog"
        :items="items"
    />

    <FunctionMigrationTable
        :dialog.sync="functionMigrationDialog"
        @dialog-closed="close"
        :items="items"
    />

    <v-menu
        :disabled="items.length === 0"
        offset-y
    >

      <template v-slot:activator="{ on, attrs }">
        <v-btn
            color="primary"
            v-bind="attrs"
            v-on="on"
            :disabled="items.length === 0"
        >

          <span>
            Migrate ({{ items.length }})
          </span>

        </v-btn>
      </template>

      <v-list>
        <v-list-item-group>

          <v-list-item>
            <v-list-item-title
                @click="migrateToMyAccount()"
            >
              to my account
            </v-list-item-title>
          </v-list-item>

          <v-list-item>
            <v-list-item-title @click="migrateToProvider('AWS')">
              to AWS
            </v-list-item-title>
          </v-list-item>

          <v-list-item>
            <v-list-item-title @click="migrateToProvider('GCP')">
              to GCP
            </v-list-item-title>
          </v-list-item>

          <v-list-item>
            <v-list-item-title
                @click="openMigrationDialog()"
            >
              custom
            </v-list-item-title>
          </v-list-item>

        </v-list-item-group>
      </v-list>

    </v-menu>
  </div>
</template>

<script>

import FunctionMigrationProviderDialog from "@/components/function/dialogs/FunctionMigrationProviderDialog.vue";
import HfApi from "@/utils/hf-api";
import FunctionMigrationTable from "@/components/function/dialogs/FunctionMigrationTable.vue";

export default {

  components: {FunctionMigrationTable, FunctionMigrationProviderDialog},

  props: {
    items: {
      type: Array,
      required: true,
    },
  },

  data: () => ({

    functionMigrationProviderDialog: false,
    functionMigrationDialog: false,

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
    }

  }

}
</script>