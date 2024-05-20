<template>

  <v-data-table
      :headers="headers"
      :items="types"
      item-key="id"
      class="elevation-1"
  >

    <template v-slot:top>

      <FunctionImplementationDialogExtended
          :dialog.sync="functionImplementationDialogVisible"
          :workflow="workflow"
          :type="selected"
          @dialog-closed="close"
      />

    </template>

    <template v-slot:[`item.addImplementation`]="{ item }">

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

import FunctionImplementationDialogExtended
  from "@/components/function/dialogs/FunctionImplementationDialogExtended.vue";

export default {
  components: {FunctionImplementationDialogExtended},

  props: {
    workflow: {
      type: Object,
      required: true
    },
    types: {
      type: Array,
      required: true
    }
  },

  data() {
    return {
      headers: [
        {text: 'Functiontype', value: 'name'},
        {text: 'Nr. of implementations', value: 'functionImplementations.length'},
        {text: '', value: 'addImplementation'}
      ],
      functionImplementationDialogVisible: false,
      selected: {},
    };
  },

  watch: {

  },

  created() {
  },

  computed: {
    functions() {

      if(this.workflow.functions == null) {
        return [];
      }

      return this.workflow.functions.map(f => {
        return {
          id: f.id,
          name: f.name,
          functionType: f.functionType,
          implementations: f.functionType.functionImplementations.length,
          addImplementation: f
        }
      });

    }
  },

  methods: {

    addImplementation(item) {
      this.selected = item;
      this.functionImplementationDialogVisible = true;
    },

    close() {
      this.functionImplementationDialogVisible = false;
      this.$emit('dialog-closed');
    }

  },

}

</script>