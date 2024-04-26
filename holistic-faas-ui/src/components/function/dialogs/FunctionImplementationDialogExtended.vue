<template>

  <v-dialog
      v-model="dialogLocal"
      max-width="500px"
      @click:outside="close"
  >

    <v-card>

      <v-card-title>
        <span class="text-h5">{{ formTitle }}</span>
      </v-card-title>

      <v-spacer></v-spacer>

      <v-card-subtitle>
        <span class="text-h7">{{ formSubtitle }}</span>
      </v-card-subtitle>

      <v-card-text>

        <v-select
            v-model="functionType"
            :items="functionTypes"
            item-text="name"
            item-value="id"
            label="Function Type"
        ></v-select>

        <v-file-input
            v-model="currentFile"
            truncate-length="60"
            @change="selectFile"
        ></v-file-input>

      </v-card-text>

      <v-card-actions>

        <v-spacer></v-spacer>
        <v-btn color="neutral" @click="close">Close</v-btn>
        <v-btn color="primary" @click="save">Save</v-btn>

      </v-card-actions>

    </v-card>

  </v-dialog>

</template>

<script>

import HfApi from "@/utils/hf-api";

export default {

  props: {
    dialog: Boolean,
    functionTypes: {type: Array, default: () => []}
  },

  data: () => ({
    currentFile: null,
    functionType: null,
  }),

  computed: {
    dialogLocal: {
      get() {
        return this.dialog
      },
      set(value) {
        this.$emit('update:dialog', value)
      }
    },

    formSubtitle() {
      return ''
    },

    formTitle() {
      return 'Add Functionimplementation'
    }
  },

  watch: {
    dialog(val) {
      if (val) {
        this.currentFile = null
      }
    }
  },

  methods: {

    selectFile(file) {
      this.currentFile = file;
    },

    close() {
      this.dialogLocal = false
      this.currentFile = null
      this.functionType = null
      this.$emit('dialog-closed')
    },

    save() {

      let functionImplementation = {
        functionTypeId: this.functionType,
      }

      HfApi.uploadFunction(this.currentFile, functionImplementation)
          .then(() => {
            this.close()
          })

    }
  }


}


</script>