<template>

  <v-toolbar
      flat
  >
    <v-breadcrumbs
        class="mx-2"
        :items="breadcrumbs"
    >

      <template v-slot:item="{ item }">
        <v-breadcrumbs-item
            :href="item.href"
            :disabled="item.disabled"
        >
          {{ item.text }}
        </v-breadcrumbs-item>
      </template>

    </v-breadcrumbs>

  </v-toolbar>

</template>

<script>

export default {

  data: function () {
    return {
      breadcrumbs: this.getBreadcrumbs()
    }
  },

  methods: {
    getBreadcrumbs() {
      const workflowId = this.$route.params.id;
      const deploymentId = this.$route.params.deploymentId;

      let breadcrumbs = [
        {
          text: 'Workflows',
          disabled: false,
          href: '#/workflows'
        }
      ];

      if(workflowId) {
        breadcrumbs.push({
          text: `Workflow ${workflowId}`,
          disabled: false,
          href: `#/workflows/${workflowId}`
        });
      }

      if(deploymentId) {
        breadcrumbs.push({
          text: `Deployment ${deploymentId}`,
          disabled: true,
          href: `#/workflows/${workflowId}/deployments/${deploymentId}`
        });
      }

      return breadcrumbs;
    }
  },

  watch: {
    '$route': function () {
      this.breadcrumbs = this.getBreadcrumbs();
    }
  }

}

</script>