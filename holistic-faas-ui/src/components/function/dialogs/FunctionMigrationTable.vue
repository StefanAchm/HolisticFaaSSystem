<template>

  <v-dialog
      v-model="dialogLocal"
      max-width="95%"
      @click:outside="close"
  >

    <v-card>

      <v-card-title>
        Migrate Function(s)
      </v-card-title>

      <v-simple-table class="table-styling">
        <template v-slot:default>

          <thead>

          <tr>
            <th class="text-left">Name</th>
            <th class="text-left">Implementation</th>
            <th class="text-left">Handler</th>
            <th class="text-left">Provider</th>
            <th class="text-left">Region</th>
            <th class="text-left">TimeoutSecs</th>
            <th class="text-left">Memory</th>
            <th class="text-left">Runtime</th>
            <th class="text-left">User</th>
          </tr>
          </thead>

          <tbody>

          <tr
              class="table-styling-common-row grey lighten-3"
          >
            <td>

              <v-text-field
                  class="column-name"
                  type="text"
                  v-model="common.functionType.name"
                  disabled
              ></v-text-field>
            </td>

            <td>

              <v-text-field
                  class="column-implementation"
                  type="text"
                  v-model="common.functionImplementation.fileName"
                  disabled
              ></v-text-field>
            </td>

            <td>
              <v-text-field
                  class="column-handler"
                  type="text"
                  v-model="common.functionDeployment.handler"
                  @input="updateEditItemsForHandler"
              ></v-text-field>
            </td>

            <td>
              <v-autocomplete
                  class="column-provider"
                  v-model="common.functionDeployment.provider"
                  :items="providers"
                  item-text="title"
                  item-value="value"
                  @change="updateEditItemsForProvider"
              ></v-autocomplete>

            </td>

            <td>
              <v-autocomplete
                  class="column-region"
                  v-model="common.functionDeployment.region"
                  :items="common.functionDeployment.regions"
                  item-text="title"
                  item-value="value"
                  @change="updateEditItemsForRegion"
              ></v-autocomplete>
            </td>


            <td>
              <v-text-field
                  class="column-timeoutSecs"
                  type="number"
                  v-model="common.functionDeployment.timeoutSecs"
                  @input="updateEditItemsForTimeoutSecs"
              ></v-text-field>
            </td>


            <td>
              <v-text-field
                  class="column-memory"
                  type="number"
                  v-model="common.functionDeployment.memory"
                  @input="updateEditItemsForMemory"
              ></v-text-field>

            </td>

            <td>
              <v-autocomplete
                  class="column-runtime"
                  v-model="common.functionDeployment.runtime"
                  :items="common.functionDeployment.runtimes"
                  item-text="title"
                  item-value="value"
                  @change="updateEditItemsForRuntimes"
              ></v-autocomplete>
            </td>

            <td>

              <v-text-field
                  class="column-username"
                  type="text"
                  v-model="common.functionDeployment.userName"
                  disabled
              ></v-text-field>
            </td>

          </tr>

          <tr
              v-for="item in editItems"
              :key="item.id"
              class="table-styling-rows grey lighten-5"
          >
            <td>

              <v-text-field
                  class="column-name"
                  type="text"
                  v-model="item.functionType.name"
                  disabled></v-text-field>
            </td>

            <td>

              <v-text-field
                  class="column-implementation"
                  type="text"
                  v-model="item.functionImplementation.fileName"
                  disabled></v-text-field>
            </td>

            <td>
              <v-text-field
                  class="column-handler"
                  type="text"
                  v-model="item.functionDeployment.handler"
              ></v-text-field>
            </td>

            <td>
              <v-autocomplete
                  class="column-provider"
                  v-model="item.functionDeployment.provider"
                  :items="providers"
                  item-text="title"
                  item-value="value"
              ></v-autocomplete>

            </td>

            <td>
              <v-autocomplete
                  class="column-region"
                  v-model="item.functionDeployment.region"
                  :items="item.functionDeployment.regions"
                  item-text="title"
                  item-value="value"
              ></v-autocomplete>
            </td>


            <td>
              <v-text-field
                  class="column-timeoutSecs"
                  type="number"
                  v-model="item.functionDeployment.timeoutSecs"
              ></v-text-field>
            </td>


            <td>
              <v-text-field
                  class="column-memory"
                  type="number"
                  v-model="item.functionDeployment.memory"
              ></v-text-field>

            </td>

            <td>
              <v-autocomplete
                  class="column-runtime"
                  v-model="item.functionDeployment.runtime"
                  :items="item.functionDeployment.runtimes"
                  item-text="title"
                  item-value="value"
              ></v-autocomplete>
            </td>

            <td>

              <v-text-field
                  class="column-username"
                  type="text"
                  v-model="item.functionDeployment.userName"
                  disabled></v-text-field>
            </td>

          </tr>

          </tbody>

        </template>
      </v-simple-table>

      <v-card-actions>

        <v-spacer></v-spacer>

        <v-btn color="neutral" @click="close">Close</v-btn>
        <v-btn color="primary" @click="save">Save</v-btn>

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
    items: [],
  },

  data: () => ({
    editItems: [],
    common: {
      functionType: {
        name: '',
      },
      functionImplementation: {
        fileName: '',
      },
      functionDeployment: {
        handler: '',
        provider: '',
        region: '',
        timeoutSecs: null,
        memory: null,
        runtime: '',
        userName: '',
        regions: [],
        runtimes: [],
      }
    },

  }),

  watch: {

    dialog(val) {

      this.init();

      this.dialogLocal = val;

      HfApi.prepareMigration(this.items, this.$store.state.userId, 'FUNCTION_USER').then((response) => {
        this.editItems = response.data.functions
      })

    },

    editItems: {
      deep: true,
      handler() {
        this.updateEditItems()
        this.updateCommon()
      }
    },

  },

  created() {

    // just for debugging
    
    // this.init()
    //
    // this.dialogLocal = true;
    //
    // HfApi.getAllFunctions().then((response) => {
    //   HfApi.prepareMigration(response.data.slice(0, 3), this.$store.state.userId, 'FUNCTION_USER').then((response) => {
    //     this.editItems = response.data.functions
    //   })
    // })


  },

  methods: {

    close() {
      this.dialogLocal = false
      this.$emit('dialog-closed')
    },

    save() {

      let migrateRequest = {
        functions: this.editItems
      }

      HfApi.migrateFunctions(migrateRequest).then(() => {
        this.close()
      })
    },

    updateEditItems() {
      this.editItems.forEach((item) => {
        item.functionDeployment.runtimes = this.getRuntimes(item.functionDeployment.provider)
        item.functionDeployment.regions = this.getRegions(item.functionDeployment.provider)
      })
    },

    updateCommon() {

      // If all items have the same provider, update the provider

      const names = this.editItems.map(item => item.functionType.name)
      const implementations = this.editItems.map(item => item.functionImplementation.fileName)
      const providers = this.editItems.map(item => item.functionDeployment.provider)
      const regions = this.editItems.map(item => item.functionDeployment.region)
      const runtimes = this.editItems.map(item => item.functionDeployment.runtime)
      const timeoutSecs = this.editItems.map(item => item.functionDeployment.timeoutSecs)
      const memory = this.editItems.map(item => item.functionDeployment.memory)
      const handlers = this.editItems.map(item => item.functionDeployment.handler)
      const users = this.editItems.map(item => item.functionDeployment.userName)

      if(names.every((val, i, arr) => val === arr[0])) {
        this.common.functionType.name = names[0]
      }

      if(implementations.every((val, i, arr) => val === arr[0])) {
        this.common.functionImplementation.fileName = implementations[0]
      }

      if (providers.every((val, i, arr) => val === arr[0])) {
        this.common.functionDeployment.provider = providers[0]
        this.common.functionDeployment.runtimes = this.getRuntimes(this.common.functionDeployment.provider)
        this.common.functionDeployment.regions = this.getRegions(this.common.functionDeployment.provider)
      }

      if (regions.every((val, i, arr) => val === arr[0])) {
        this.common.functionDeployment.region = regions[0]
      }

      if (runtimes.every((val, i, arr) => val === arr[0])) {
        this.common.functionDeployment.runtime = runtimes[0]
      }

      if (timeoutSecs.every((val, i, arr) => val === arr[0])) {
        this.common.functionDeployment.timeoutSecs = timeoutSecs[0]
      }

      if (memory.every((val, i, arr) => val === arr[0])) {
        this.common.functionDeployment.memory = memory[0]
      }

      if (handlers.every((val, i, arr) => val === arr[0])) {
        this.common.functionDeployment.handler = handlers[0]
      }

      if(users.every((val, i, arr) => val === arr[0])) {
        this.common.functionDeployment.userName = users[0]
      }

    },

    updateEditItemsForHandler() {

      this.editItems.forEach((item) => {
        item.functionDeployment.handler = this.common.functionDeployment.handler
      })

    },

    updateEditItemsForProvider() {

      this.common.functionDeployment.runtimes = this.getRuntimes(this.common.functionDeployment.provider)
      this.common.functionDeployment.regions = this.getRegions(this.common.functionDeployment.provider)

      this.editItems.forEach((item) => {
        item.functionDeployment.provider = this.common.functionDeployment.provider
      })

    },

    updateEditItemsForRegion() {

      this.editItems.forEach((item) => {
        item.functionDeployment.region = this.common.functionDeployment.region
      })

    },

    updateEditItemsForTimeoutSecs() {

      this.editItems.forEach((item) => {
        item.functionDeployment.timeoutSecs = this.common.functionDeployment.timeoutSecs
      })

    },

    updateEditItemsForMemory() {

      this.editItems.forEach((item) => {
        item.functionDeployment.memory = this.common.functionDeployment.memory
      })

    },

    updateEditItemsForRuntimes() {

      this.editItems.forEach((item) => {
        item.functionDeployment.runtime = this.common.functionDeployment.runtime
      })

    },

  },

  computed: {

    dialogLocal: {
      get() {
        return this.dialog
      },
      set(value) {
        this.$emit('update:dialog', value)
      }
    },

  }

}

</script>

<style scoped>

.table-styling-rows td, .table-styling-rows .v-input {
  font-size: 0.85rem !important;
}

.table-styling-common-row td, .table-styling-common-row .v-input {
  font-size: 0.85rem !important;
  font-weight: bold;
}

.table-styling-rows .v-input--is-disabled >>> .v-input__slot::before,
.table-styling-common-row .v-input--is-disabled >>> .v-input__slot::before {
  border-image: none !important;
  border-style: none !important;
}

.table-styling-rows .v-input >>> .v-text-field__details,
.table-styling-common-row .v-input >>> .v-text-field__details {
  display: none !important;
}

.column-name {
  width: 100px;
}

.column-implementation {
  width: 300px;
  border-style: none;
  text-decoration:none;
}

.column-handler {
  width: 300px;
}

.column-provider {
  width: 200px
}

.column-region {
  width: 200px
}

.column-timeoutSecs {
  width: 50px
}

.column-memory {
  width: 50px
}

.column-runtime {
  width: 100px
}

.column-username {
  width: 100px
}


</style>