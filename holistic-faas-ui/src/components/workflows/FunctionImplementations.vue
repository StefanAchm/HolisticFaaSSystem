<template>

  <v-data-table
      :headers="headers"
      :items="functions"
      item-key="id"
      class="elevation-1"
  >

    <template v-slot:top>

    </template>

  </v-data-table>

</template>

<script>

export default {

  props: {
    workflow: {
      type: Object,
      required: true
    }
  },

  data() {
    return {
      headers: [
        {text: 'Function Implementation', value: 'functionImplementation.fileName'},

        {text: 'Function Name', value: 'function.name'},
        {text: 'Function Type', value: 'function.functionType.name'},

        // {text: 'File Path', value: 'filePath'},
      ],
      functions: [],
      functionImplementationDialogVisible: false,
      editItem: {},
    };
  },

  created() {
    this.loadImplementations();
  },

  watch: {
    workflow() {
      this.loadImplementations();
    }
  },

  methods: {
    loadImplementations() {

      this.functions = this.workflow.functions; // Flatten this list!
      // Each function has a name and a type, but multiple implementations
      // We need to flatten this list to show all implementations
      this.functions = this.functions.map(function (functionItem) {

        if (functionItem.functionType.functionImplementations == null || functionItem.functionType.functionImplementations.length === 0) {
          return [];
        }

        return functionItem.functionType.functionImplementations.map(function (functionImplementation) {
          return {
            function: functionItem,
            functionImplementation: functionImplementation
          }
        });
      }).flat()
          .filter(item => Object.keys(item).length !== 0); // Filter out empty objects

      console.log(this.functions);

    }

  }


}
</script>