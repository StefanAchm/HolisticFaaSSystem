<template>
  <div class="text-center">

    <FunctionMigrationProviderDialog
        :dialog.sync="functionMigrationProviderDialog"
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
                @click="migrateToMyAccount()"
            >
              to another Region
            </v-list-item-title>
          </v-list-item>

        </v-list-item-group>
      </v-list>

    </v-menu>
  </div>
</template>

<script>

import FunctionMigrationProviderDialog from "@/components/FunctionMigrationProviderDialog.vue";
import HfApi from "@/utils/hf-api";

export default {

  components: {FunctionMigrationProviderDialog},

  props: {
    items: {
      type: Array,
      required: true,
    },
  },

  data: () => ({

    functionMigrationProviderDialog: false,

  }),

  methods: {

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

            console.log(response);

            HfApi.migrateFunctions(response.data)
                .then((response) => {

                  console.log(response);

                  this.$emit('menu-closed')

                })

          })

    },

    migrateToProvider(provider) {

      HfApi.prepareMigration(this.getMigrateRequest(), provider, 'FUNCTION_PROVIDER')
          .then((response) => {

            console.log(response);

            HfApi.migrateFunctions(response.data)
                .then((response) => {

                  console.log(response);

                  this.$emit('menu-closed')

                })

          })

    }

  }

}
</script>