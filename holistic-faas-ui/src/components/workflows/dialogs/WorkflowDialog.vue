<template>


  <v-dialog
      v-model="dialogLocal"
      max-width="500px"
      @click:outside="close"
  >

    <v-card max-height="600px">

      <v-card-title>
        <span class="text-h5">Add Workflow</span>
      </v-card-title>

      <v-card-text>

        <v-form v-model="isValid">

          <v-text-field
              v-model="workflow.name"
              label="Name"
              required
              :rules="[v => !!v || 'Name is required']"
          />

          <v-textarea
              v-model="workflow.description"
              label="Description"
              required
              no-resize
              rows="10"
              :rules="[v => !!v || 'Description is required']"
          />

          <v-file-input
              v-model="currentFile"
              truncate-length="60"
              label="Workflow definition (optional)"
              @change="selectFile"
          ></v-file-input>

        </v-form>

      </v-card-text>

      <v-card-actions>

        <v-spacer></v-spacer>

        <v-btn color="neutral" @click="close">Close</v-btn>
        <v-btn color="primary" :disabled="!isValid" @click="save">Save</v-btn>

      </v-card-actions>

    </v-card>

  </v-dialog>

</template>

<script>

import HfApi from "@/utils/hf-api";

export default {

  props: {
    dialog: {type: Boolean, default: false}
  },

  data: () => ({
    dialogLocal: false,
    workflow: {},
    currentFile: null,
    isValid: false
  }),

  watch: {
    dialog() {
      this.dialogLocal = this.dialog
    }
  },

  methods: {

    selectFile(file) {
      this.currentFile = file;
    },

    close() {
      this.dialogLocal = false
      this.isValid = false
    },

    save() {

      HfApi.uploadWorkflow(this.currentFile, this.workflow)
          .then(() => {
            this.$emit('workflow-added');
            this.$root.snackbar.showSuccess({message: 'Workflow added'})
          })
          .catch(() => {
            this.$root.snackbar.showError({message: 'Failed to add workflow'})
          })

      this.dialogLocal = false

    }

  }


}


</script>