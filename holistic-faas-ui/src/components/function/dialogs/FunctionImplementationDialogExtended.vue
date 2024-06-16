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

        <v-form v-model="isValid" ref="form">

          <v-select
              v-model="functionType"
              :items="functionTypes"
              item-text="name"
              item-value="id"
              label="Function Type"
              :disabled="type !== null"
              required
              :rules="[v => !!v || 'Function Type is required']"
          ></v-select>

          <v-file-input
              append-icon="mdi-paperclip"
              prepend-icon=""
              v-model="currentFile"
              truncate-length="60"
              @change="selectFile"
              label="Function implementation"
              required
              :rules="[v => !!v || 'Function implementation is required']"
          ></v-file-input>

          <v-text-field
              v-model="functionImplementationName"
              label="Function Implementation Name"
              required
              :rules="[v => !!v || 'Name is required']"
              append-icon="mdi-information-outline"
          >

            <template v-slot:append>
              <v-tooltip bottom>
                <template #activator="{on}">
                  <v-icon v-on="on">mdi-information-outline</v-icon>
                </template>
                <span>
                  Choose a unique name for the function-implementation <br>
                  (e.g. 'AWS myFunction1' or 'GCP myFunction1')
                </span>
                </v-tooltip>
            </template>

          </v-text-field>

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
    dialog: Boolean,
    workflow: {
      type: Object,
      default: null
    },
    implementation: {
      type: Object,
      default: null
    },
    type: {
      type: Object,
      default: null
    }

  },

  data: () => ({
    currentFile: null,
    functionType: null,
    functionImplementationName: null,
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

    formSubtitle() {
      return ''
    },

    formTitle() {
      return 'Add Functionimplementation'
    },

    functionTypes() {
      return this.workflow.functions?.map(f => f.functionType);
    },

  },

  watch: {
    dialog(val) {
      if (val) {
        this.currentFile = null
        this.functionType = this.type?.id
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
      this.isValid = false
      this.$refs.form.reset(); // reset the form validation
      this.$emit('dialog-closed')
    },

    save() {

      let functionImplementation = {
        functionTypeId: this.functionType,
        name: this.functionImplementationName
      }

      HfApi.uploadFunction(this.currentFile, functionImplementation)
          .then(() => {
            this.$root.snackbar.showSuccess({message: 'Function implementations added'})
            this.close()
          })
          .catch(() => {
            this.$root.snackbar.showError({message: 'Failed to add function implementations'})
          })

    }
  }


}


</script>