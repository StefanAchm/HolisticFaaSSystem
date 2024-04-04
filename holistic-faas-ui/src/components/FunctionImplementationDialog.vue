<template>

  <v-dialog
    v-model="dialogLocal"
    >

    <v-card>

      <v-card-title>
        <span class="text-h5">{{ formTitle }}</span>
      </v-card-title>

      <v-card-text>

        <v-file-input
            truncate-length="60"
            @change="selectFile"
        ></v-file-input>

      </v-card-text>

      <v-card-actions>

        <v-spacer></v-spacer>

        <v-btn
            color="blue darken-1"
            text
            @click="close"
        >Close
        </v-btn>

        <v-btn
            color="blue darken-1"
            text
            @click="save"
        >Save</v-btn>

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
    functionType: {type: Object, default: null},
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
    formTitle() {
      return this.editItemLocal?.id ? 'Change' : 'New'
    }
  },

  watch: {
    dialog(val) {
      if (val) {

        console.log('dialog, val:' + val, this.editItem, this.functionType)

        if(this.editItem?.id) {
          this.editItemLocal = this.editItem
        } else if(this.editItemLocal) {
          this.editItemLocal = {
            functionTypeId: this.functionType.id
          }
        } else {
          this.editItemLocal = {}
        }

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

      console.log('save with:', this.currentFile, this.editItemLocal)

      HfApi.uploadFunction(this.currentFile, this.editItemLocal)
          .then(() => {
            this.close()
          })
    }
  }



}


</script>