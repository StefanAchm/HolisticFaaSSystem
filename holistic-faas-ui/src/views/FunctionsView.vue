<template>

  <v-card>

    <FunctionHeader
        :selected="selected"
        @dialog-closed="updateSearch"
    />

    <FunctionSearch
        @update-search="updateSearch"
    />

    <FunctionTree
        ref="functionTree"
        :search="search"
        :functionsFromProps="functions"
        v-if="treeView"
        @update-list="init"
        @update-selected="handleSelected"
    />

    <FunctionTable
        ref="functionTable"
        v-else
        :search="search"
        :functionsFromProps="functions"
        @update-selected="handleSelected"
    />

  </v-card>

</template>

<script>

import FunctionTable from "@/components/function/FunctionTable.vue";
import FunctionTree from "@/components/function/FunctionTree.vue";
import FunctionHeader from "@/components/function/FunctionHeader.vue";
import FunctionSearch from "@/components/function/FunctionSearch.vue";
import HfApi from "@/utils/hf-api";
import hfWebsocket from "@/utils/hf-websocket";

export default {
  components: {FunctionSearch, FunctionTable, FunctionTree, FunctionHeader},

  data: () => ({
    selected: [],
    search: '',
    functions: []
  }),

  created() {
    this.init()
  },

  watch: {

    treeView(val) {
      this.functions.treeView = val
    }

  },

  computed: {
    treeView: {
      get() {
        return this.$store.state.treeView
      },
      set(value) {
        this.functions.treeView = value
        this.$store.commit('setTreeView', value)
      }
    },
  },

  methods: {

    handleSelected(selected) {
      this.selected = selected
    },

    updateSearch(search) {
      this.search = search
      // this.$refs.functionTable.init()
      // this.$refs.functionTree.init()
      // this.init()
    },

    init() {

      HfApi.getAllFunctions()
          .then(response => {

            this.functions = response.data;

            for (let i = 0; i < this.functions.length; i++) {

              if (this.functions[i].functionDeployment) {
                this.functions[i].id = this.functions[i].functionDeployment.id;
              } else if (this.functions[i].functionImplementation) {
                this.functions[i].id = this.functions[i].functionImplementation.id;
              } else {
                this.functions[i].id = this.functions[i].functionType.id;
              }

            }

          })

      hfWebsocket.onMessage(this.updateProgress)

    },

    updateProgress(message) {

      // Update progress of process component, depending on the message

      let id = message.id;
      let step = message.step;
      let steps = message.steps;
      let status = message.status;
      let statusMessage = message.statusMessage;
      let text = message.text;

      let value = step / steps * 100

      this.functions
          .filter(f => f.id === id)
          .forEach(f => {

            let fd = f.functionDeployment

            this.$set(fd, 'isLoadingValue', value)

            if (value === 100) {
              this.$set(fd, 'isLoadingValue', null)
            }

            this.$set(fd, 'status', status)
            this.$set(fd, 'statusMessage', statusMessage)
            this.$set(fd, 'text', text)

          })

    },

  }

}

</script>