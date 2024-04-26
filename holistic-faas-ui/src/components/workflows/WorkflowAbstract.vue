<template>

  <v-data-table
      :headers="headers"
      :items="functions"
      item-key="id"
      class="elevation-1"
  >

    <template v-slot:top>

      <FunctionImplementationDialog
          :dialog.sync="functionImplementationDialogVisible"
          @dialog-closed="close"
          :edit-item="editItem"/>

    </template>

    <template v-slot:[`item.addImplementation`]="{ item }">

      <!--      <v-btn @click="addImplementation(item)">Add Implementation</v-btn>-->

      <v-btn
          color="secondary"
          outlined
          rounded
          small
          @click="addImplementation(item)"
      >

        <v-icon left>
          mdi-plus
        </v-icon>

        Add Implementation

      </v-btn>

    </template>


  </v-data-table>

</template>

<script>

import FunctionImplementationDialog from "@/components/function/dialogs/FunctionImplementationDialog.vue";

export default {
  components: {FunctionImplementationDialog},

  props: {
    workflow: {
      type: Object,
      required: true
    }
  },

  data() {
    return {
      headers: [
        {text: 'Name', value: 'name'},
        {text: 'Type', value: 'functionType.name'},
        {text: 'Implementations', value: 'implementations'},
        // {text: '', value: 'addImplementation'}
      ],
      functions: [],
      functionImplementationDialogVisible: false,
      editItem: {},
    };
  },

  watch: {
    workflow() {
      this.loadFunctions();
    }
  },

  created() {
    this.loadFunctions();
  },

  methods: {
    loadFunctions() {
      this.functions = this.workflow.functions;

      if(this.functions == null) {
        return;
      }

      for(let f of this.functions) {
        f.implementations = f.functionType.functionImplementations.length
      }
    },

    addImplementation(item) {
      this.editItem = item;

      this.editItem.functionImplementation = {
        functionTypeId: item.functionType.id,
      }

      this.functionImplementationDialogVisible = true;
    },

    close() {
      this.functionImplementationDialogVisible = false;
      this.loadFunctions();
    }

  },

}

</script>