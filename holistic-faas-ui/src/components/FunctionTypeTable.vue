<template>

  <v-data-table
      :headers="headers"
      :items="functionTypes"
  >

    <template v-slot:top>

      <v-spacer></v-spacer>

      <v-btn
          color="primary"
          class="mx-2"
          @click="dialogVisible = true"
      >Add
      </v-btn>

      <FunctionTypeDialog
          :dialog.sync="dialogVisible"
          @dialog-closed="loadFunctionTypes"
          :function-type="{}"
      />

    </template>


  </v-data-table>


</template>

<script>


import HfApi from "@/utils/hf-api";
import FunctionTypeDialog from "@/components/FunctionTypeDialog.vue";

export default {
  components: {FunctionTypeDialog},

  data: () => ({

    functionTypes: [],
    headers: [
      {text: 'Name', value: 'name', sortable: true},
    ],

    dialogVisible: false,

  }),

  created() {
    this.loadFunctionTypes();
  },

  methods: {
    loadFunctionTypes() {

      console.log("Loading function types");

      HfApi.getAllFunctionTypes()
          .then(response => {
            this.functionTypes = response.data;
            console.log("Loaded function types", this.functionTypes);
          })
    },
  },


}

</script>