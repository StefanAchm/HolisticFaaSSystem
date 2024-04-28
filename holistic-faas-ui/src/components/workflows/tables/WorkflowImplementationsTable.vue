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
    functions: {
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

      functionImplementationDialogVisible: false,

    };
  },

  created() {

  },

  watch: {

  },

  methods: {

  }


}
</script>