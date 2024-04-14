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

      <v-card-text>

        <v-text-field
          v-model="editItemLocal.name"
          label="Name"
        />

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
    dialog: {type: Boolean, default: false},
    editItem: {type: Object, default: null}
  },

  data: () => ({
    editItemLocal: {}
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
      return this.editItemLocal?.id ? 'Change' : 'Add Functiontype'
    }
  },

  watch: {
    dialog(val) {
      if (val) {
        this.editItemLocal = this.editItem?.id ? this.editItem : {}
      }
    }
  },

  methods: {
    close() {
      this.dialogLocal = false
      this.$emit('dialog-closed')
    },
    save() {
      HfApi.addFunctionType(this.editItemLocal)
          .then(() => {
            this.close()
          })
    }
  }



}


</script>