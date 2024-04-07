<template>
  <div class="text-center">

    <FunctionMigrationProviderDialog
        :dialog.sync="functionMigrationProviderDialog"
        :items="items"
    />

    <v-menu
        :disabled="disabled"
        offset-y
    >

      <template v-slot:activator="{ on, attrs }">
        <v-btn
            color="primary"
            v-bind="attrs"
            v-on="on"
            :class="{ 'v-btn--disabled': disabled }"
        >

          <span v-if="items.length === 0">
            Migrate
          </span>

          <span v-else-if="items.length === 1">
            Migrate 1 function
          </span>

          <span v-else>
            Migrate {{ items.length }} functions
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
    disabled: {
      type: Boolean,
      default: false,
    },
  },

  data: () => ({

    functionMigrationProviderDialog: false,

  }),

  methods: {

    migrateToMyAccount() {
      console.log('migrateToMyAccount');
      console.log(this.items);
    },

    migrateToProvider(provider) {

      // Items consists of a list of {id, functionDeployment, functionImplementation and functionType}
      // But the api does not expect the id field, so create a new object without that

      let itemsRequest = this.items.map(item => {
        return {
          functionDeployment: item.functionDeployment,
          functionImplementation: item.functionImplementation,
          functionType: item.functionType
        }
      })

      HfApi.prepareMigration(itemsRequest, provider)
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