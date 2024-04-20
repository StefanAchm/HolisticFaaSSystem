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

        <v-file-input
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

export default  {

  props: {
    dialog: Boolean,
    editItem: {type: Object, default: null},
  },

  data: () => ({
    editItemLocal: {},
    currentFile: null
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
      return 'Function type id: ' + this.editItemLocal.functionType?.id
    },

    formTitle() {
      return this.editItemLocal.functionImplementation?.id ? 'Edit Functionimplementation' : 'Add Functionimplementation'
    }
  },

  watch: {
    dialog(val) {
      if (val) {
        this.editItemLocal = Object.assign({}, this.editItem)
      }
    }
  },

  methods: {

    selectFile(file) {
      this.currentFile = file;
    },

    close() {
      this.dialogLocal = false
      this.$emit('dialog-closed')
    },

    save() {

      if(this.editItemLocal.functionImplementation?.id) {

        HfApi.updateFunction(this.currentFile, this.editItemLocal.functionImplementation)
            .then(() => {
              this.close()
            })

      } else {

        HfApi.uploadFunction(this.currentFile, this.editItemLocal.functionImplementation)
            .then(() => {
              this.close()
            })

      }
    }
  }



}


</script>