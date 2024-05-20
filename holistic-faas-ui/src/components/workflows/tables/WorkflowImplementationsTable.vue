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

        {text: 'File', value: 'functionImplementation.fileName'},
        {text: 'Name', value: 'functionImplementation.name'},
        {text: 'Functiontype', value: 'functionType.name'},

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