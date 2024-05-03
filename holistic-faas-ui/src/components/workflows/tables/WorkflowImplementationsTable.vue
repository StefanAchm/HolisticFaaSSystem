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
      type: Array,
      required: true
    }
  },

  data() {
    return {
      headers: [

        {text: 'Implementation', value: 'functionImplementation.fileName'},

        {text: 'Name', value: 'function.name'},
        {text: 'Type', value: 'function.functionType.name'},

        // {text: 'File Path', value: 'filePath'},

      ],

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