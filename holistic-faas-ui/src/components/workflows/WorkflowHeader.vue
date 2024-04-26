<template>

  <v-toolbar
      elevation="0"
  >

    <v-toolbar-title>Workflows</v-toolbar-title>

    <v-spacer></v-spacer>

    <input
        type="file"
        ref="fileInput"
        @change="onFileSelected"
        style="display: none"
        accept=".yaml, .yml"
    />

    <v-btn
        color="primary"
        class="mx-2"
        @click="openFileSelect">
      <v-icon
          left>
        mdi-plus
      </v-icon>
      Add
    </v-btn>

  </v-toolbar>

</template>

<script>

import HfApi from "@/utils/hf-api";

export default {

  props: {

  },

  data: () => ({

  }),

  created() {
    //
  },

  computed: {

  },

  methods: {

    openFileSelect() {
      this.$refs.fileInput.click();
    },

    onFileSelected(event) {
      HfApi.uploadWorkflow(event.target.files[0])
        .then(() => {
          this.$emit('workflow-added');
        })
        .catch(error => {
          console.error("Failed to upload workflow:", error);
        })
    }

  }

}

</script>

<style>

</style>