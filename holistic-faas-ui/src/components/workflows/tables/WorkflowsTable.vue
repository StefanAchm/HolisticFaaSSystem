<template>

  <v-data-table
      :headers="headers"
      :items="workflowsFromProps"
      v-model="selected"
      :single-select="false"
      item-key="id"
      :items-per-page="10"
  >

    <template v-slot:top>

    </template>

    <template v-slot:item="{ item }">
      <tr @click="goToDetails(item.id)">
        <td>{{ item.name }}</td>
      </tr>
    </template>

  </v-data-table>


</template>

<script>

export default {

  props: {
    workflowsFromProps: []
  },

  data: () => ({
    selected: [],
    headers: [

      {text: 'Name', value: 'name', sortable: true},

    ],

  }),

  created() {
  },

  watch: {
    selected() {
      this.$emit('update-selected', this.selected)
    },
  },

  methods: {

    goToDetails(id) {
      this.$router.push({ name: 'workflow', params: { id } });
    },

  },

}

</script>