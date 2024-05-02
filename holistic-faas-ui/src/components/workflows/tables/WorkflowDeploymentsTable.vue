<template>
  <v-data-table
      :headers="headers"
      :items="deployments"
      item-key="id"
      class="elevation-1"
      loading="deployments.length === 0"
  >

    <template v-slot:item="{ item }">
      <tr @click="goToDetails(item.id)">
        <td>{{ item.name }}</td>
        <td>{{ item.user.username }}</td>
        <td>{{ item.deploymentDetails }}</td>
      </tr>
    </template>

  </v-data-table>

</template>

<script>

export default {

  props: {
    workflow: {
      type: Object,
      required: true
    },
    deployments: {
      type: Array,
      required: true
    }
  },

  data() {
    return {
      headers: [
        {text: 'Deployment', value: 'name'},
        {text: 'User', value: 'user.username'},
        {text: 'Details', value: 'deploymentDetails'}
      ],
    };
  },

  created() {
  },

  watch: {
  },

  methods: {

    goToDetails(id) {
      this.$router.push({ name: 'deployments', params: { id: this.workflow.id, deploymentId: id } });
    }

  },

}

</script>