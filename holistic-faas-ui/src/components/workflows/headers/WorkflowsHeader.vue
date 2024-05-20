<template>

  <v-toolbar
      elevation="0"
  >

    <v-toolbar-title>Explore Workflows</v-toolbar-title>

    <v-spacer></v-spacer>

    <v-btn
        color="primary"
        class="mx-2"
        @click="openWorkflowDialog">
      <v-icon
          left>
        mdi-plus
      </v-icon>
      Add
    </v-btn>

    <WorkflowDialog
        @workflow-added="$emit('workflow-added')"
        :dialog.sync="dialog"/>

  </v-toolbar>

</template>

<script>

import WorkflowDialog from "@/components/workflows/dialogs/WorkflowDialog.vue";

export default {
  components: {WorkflowDialog},

  props: {

  },

  data: () => ({

    dialog: false

  }),

  created() {
    //
  },

  computed: {

  },

  methods: {

    openWorkflowDialog() {
      this.dialog = true
    },

  }

}

</script>

<style>

</style>