<template>

  <v-dialog v-model="dialogLocal" max-width="500px">

    <v-card>

      <v-card-title>
        <span class="text-h5">Migrate to another provider</span>
      </v-card-title>

      <v-card-text>
        <v-container>

          <v-row>

            <v-col>

              <v-select
                  v-model="migrationSettings.provider"
                  :items="providers"
                  item-text="title"
                  item-value="value"
                  label="Provider"
              ></v-select>

            </v-col>

          </v-row>

        </v-container>

        <v-container>


        </v-container>


      </v-card-text>


      <v-card-actions>

        <v-spacer></v-spacer>

        <v-btn
            color="blue darken-1"
            text
            @click="close"
        >Close
        </v-btn>

        <v-btn
            color="blue darken-1"
            text
            @click="save"
        >Save
        </v-btn>

      </v-card-actions>

    </v-card>

  </v-dialog>

</template>

<script>

import HfApi from "@/utils/hf-api";

import common from "@/utils/common";

export default {

  mixins: [common],

  props: {
    dialog: Boolean,
    items: {type: Array, required: true},
  },

  data: () => ({
    currentFile: null,
    migrationSettings: {
      provider: null,
    },
    preparedItems: []

  }),

  computed: {
    dialogLocal: {
      get() {
        return this.dialog
      },
      set(value) {
        this.$emit('update:dialog', value)
      }
    },
  },

  created() {
  },

  watch: {

    // migrationSettings: {
    //
    //   handler(value) {
    //
    //     HfApi.prepareMigration(this.items, value)
    //         .then((response) => {
    //           this.preparedItems = response.data
    //         })
    //
    //   }, deep: true
    //
    // }


  },

  methods: {

    close() {
      this.dialogLocal = false
      this.$emit('dialog-closed')
    },

    save() {

      HfApi.prepareMigration(this.items, this.migrationSettings)
          .then((response) => {

            console.log(response);

            HfApi.migrateFunctions(response.data)
                .then((response) => {

                  console.log(response);

                  this.close();

                })

          })

    }

  }

}


</script>