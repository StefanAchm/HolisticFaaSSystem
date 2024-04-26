<template>

  <v-card>

    <WorkflowBreadCrumps></WorkflowBreadCrumps>

    Here are your deployments


  </v-card>

</template>

<script>

import WorkflowBreadCrumps from "@/components/workflows/WorkflowBreadCrumps.vue";

export default {
  components: {WorkflowBreadCrumps},

  data: () => ({
    // selected: [],
    // search: '',
    workflows: [],
    // withFileDrop: false, // Because this does not work atm!
  }),

  created() {
    this.loadWorkflows()
  },

  watch: {

  },

  computed: {

  },

  methods: {

    loadWorkflows() {

    }

  }

}

</script>