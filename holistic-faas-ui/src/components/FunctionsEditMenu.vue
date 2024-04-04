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
          Migrate
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



            <v-list-item-title
                @click="functionMigrationProviderDialog = true;"
            >
              to another Provider
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

  }

}
</script>