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

        <v-form v-model="isValid" ref="form">

          <v-text-field
              v-model="editItemLocal.name"
              label="Name"
              required
              :rules="[v => !!v || 'Name is required']"
          />

          <v-text-field
              v-model="editItemLocal.functionType.name"
              label="Type"
              required
              :rules="[v => !!v || 'Type is required']"
          />

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
    dialog: {type: Boolean, default: false},
    editItem: {type: Object, default: null},
    workflow: {type: Object, default: null}
  },

  data: () => ({
    editItemLocal: {
      name: '',
      functionType: {name: ''}
    },
    isValid: false
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
      return this.editItemLocal?.id ? 'Change' : 'Add Function'
    }
  },

  watch: {
    dialog(val) {
      if (val) {
        this.editItemLocal = this.editItem?.id ? this.editItem : {
          name: '',
          functionType: {name: ''}
        }
      }
    }
  },

  methods: {
    close() {
      this.dialogLocal = false
      this.isValid = false
      this.$refs.form.reset(); // reset the form validation
      this.editItemLocal = {
        name: '',
            functionType: {name: ''}
      };
      this.$emit('dialog-closed')

    },
    save() {
      this.editItemLocal.workflowId = this.workflow.id
      HfApi.addFunction(this.editItemLocal)
          .then(() => {
            this.close()
          })
    }
  }


}


</script>