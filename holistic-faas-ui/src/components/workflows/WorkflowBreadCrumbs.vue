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

  props: {
    workflow: {
      type: Object,
      required: false
    },
    workflowDeployment: {
      type: Object,
      required: false
    }
  },

  data() {
    return {
      breadcrumbs: this.getBreadcrumbs()
    }
  },

  watch: {
    workflow() {
      this.breadcrumbs = this.getBreadcrumbs();
    },
    workflowDeployment() {
      this.breadcrumbs = this.getBreadcrumbs();
    }
  },

  methods: {
    getBreadcrumbs() {

      const workflowId = this.$route.params.id;
      const deploymentId = this.$route.params.deploymentId;

      const workflowName = this.workflow ? this.workflow.name : '';
      const deploymentName = this.workflowDeployment ? this.workflowDeployment.name : '';

      let breadcrumbs = [
        {
          text: 'Workflows',
          disabled: false,
          href: '#/workflows'
        }
      ];

      if(workflowId && workflowName) {
        breadcrumbs.push({
          text: `${workflowName}`,
          disabled: false,
          href: `#/workflows/${workflowId}`
        });
      }

      if(deploymentId && deploymentName) {
        breadcrumbs.push({
          text: `${deploymentName}`,
          disabled: false,
          href: `#/workflows/${workflowId}/deployments/${deploymentId}`
        });
      }

      return breadcrumbs;
    }
  },

  // watch: {
  //   '$route': function () {
  //     this.breadcrumbs = this.getBreadcrumbs();
  //   }
  // }

}

</script>