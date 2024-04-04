<template>

  <v-data-table
      :headers="headers"
      :items="functionImplementations"
  >

    <template v-slot:top>

      <v-spacer></v-spacer>

      <v-btn
          color="primary"
          class="mx-2"
          @click="dialogVisible = true"
      >Add
      </v-btn>

      <FunctionImplementationDialog
          :dialog.sync="dialogVisible"
          @dialog-closed="loadFunctionImplementations"
          :function-type="{}"
      />

    </template>


  </v-data-table>


</template>

<script>


import HfApi from "@/utils/hf-api";
import FunctionImplementationDialog from "@/components/FunctionImplementationDialog.vue";

export default {
  components: {FunctionImplementationDialog},

  data: () => ({

    functionImplementations: [],
    headers: [
      {text: 'Name', value: 'name', sortable: true},
    ],

    dialogVisible: false,

  }),

  created() {
    this.loadFunctionImplementations();
  },

  methods: {
    loadFunctionImplementations() {
      HfApi.getAllFunctionImplementations()
          .then(response => {
            this.functionImplementations = response.data;
          })
    },
  },


}

</script>