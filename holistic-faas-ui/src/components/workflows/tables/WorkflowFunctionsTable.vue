<template>

  <v-data-table
      :headers="headers"
      :items="functions"
      item-key="id"
      class="elevation-1"
  >

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
        {text: 'Functionname', value: 'name'},
        {text: 'Functiontype', value: 'functionType.name'},
        {text: 'Nr. of implementations', value: 'implementations'},
      ],
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

  },

}

</script>