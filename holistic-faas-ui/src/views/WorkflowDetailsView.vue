<template>

  <v-card>

    <WorkflowBreadCrumps></WorkflowBreadCrumps>

    <WorkflowDetails></WorkflowDetails>

  </v-card>

</template>

<script>

import WorkflowDetails from "@/components/workflows/headers/WorkflowDetailsHeader.vue";
import WorkflowBreadCrumps from "@/components/workflows/WorkflowBreadCrumps.vue";

export default {
  components: {WorkflowBreadCrumps, WorkflowDetails},

  data: () => ({
    // selected: [],
    // search: '',
    // functions: [],
    // withFileDrop: false, // Because this does not work atm!
  }),

  created() {
    // this.init()
  },

  watch: {

  },

  computed: {

  },

  methods: {


  }

}

</script>