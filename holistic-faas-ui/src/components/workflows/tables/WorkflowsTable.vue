<template>

  <v-data-table
      :headers="headers"
      :items="workflowsFromProps"
      v-model="selected"
      :single-select="false"
      item-key="id"
      :items-per-page="10"
      loading="deployments.length === 0"
      :search="search"
  >

    <template v-slot:top>

      <v-toolbar
          elevation="0"
      >
        <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="Search"
            single-line
            hide-details
        ></v-text-field>
      </v-toolbar>


    </template>

    <template v-slot:item="{ item }">
      <tr>
        <td>
          <a @click="goToDetails(item.id)">{{ item.name }}</a>
        </td>
        <td>{{ truncate(item.description, 80) }}</td>
        <td>{{ item.createdAt }}</td>
        <td>{{ item.createdBy }}</td>
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
    search: '',
    headers: [

      {text: 'Name', value: 'name', sortable: true, width: '20%'},
      {text: 'Description', value: 'description', sortable: true, width: '40%'},

      {text: 'Created at', value: 'createdAt', sortable: true, width: '20%'},
      {text: 'Created by', value: 'createdBy', sortable: true, width: '20%'},

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

    truncate(text, length) {
      return text.length > length ? text.substring(0, length) + '...' : text
    },

    goToDetails(id) {
      this.$router.push({name: 'workflow', params: {id}});
    },

  },

}

</script>