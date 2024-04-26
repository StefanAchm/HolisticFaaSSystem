<template>
  <v-data-table
      :headers="headers"
      :items="deployments"
      item-key="id"
      class="elevation-1"
  >

    <template v-slot:item="{ item }">
      <tr @click="goToDetails(item.id)">
        <td>{{ item.name }}</td>
        <td>{{ item.user.username }}</td>
      </tr>
    </template>

  </v-data-table>

</template>

<script>
import HfApi from "@/utils/hf-api";

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
        {text: 'Deployment', value: 'name'},
        {text: 'User', value: 'user.username'},
      ],
      deployments: [],
    };
  },

  created() {
    this.loadDeployment();
  },

  watch: {
    workflow() {
      this.loadFunctions();
    }
  },

  methods: {
    loadDeployment() {
      HfApi.getDeployments(this.workflow.id)
          .then(response => {
            this.deployments = response.data;
          })
          .catch(error => {
            console.error("Failed to load deployments:", error);
          });
    },

    goToDetails(id) {
      this.$router.push({ name: 'deployments', params: { id: this.workflow.id, deploymentId: id } });
    }

  },

}

</script>