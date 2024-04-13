<template>

  <v-dialog v-model="dialogLocal" max-width="500px">

    <v-card>

      <v-card-title>
        <span class="text-h5">Edit provider: {{ provider }}</span>
      </v-card-title>

      <v-card-text>
        <v-container>

          <v-row>

            <v-file-input
                truncate-length="60"
                @change="selectFile"
            ></v-file-input>

          </v-row>

        </v-container>
      </v-card-text>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
        <v-btn color="blue darken-1" text @click="upload">Save</v-btn>
      </v-card-actions>

    </v-card>
  </v-dialog>


</template>

<script>

import common from "@/utils/common";

import HfApi from "@/utils/hf-api";

export default {

  mixins: [common],

  props: {
    dialog: Boolean,
    editItem: {type: Object, default: null},
    provider: {type: String, default: null},
  },

  data() {
    return {
      currentFile: null,
      defaultItem: {},
    }
  },

  computed: {
    dialogLocal: {
      get() {
        return this.dialog
      },
      set(value) {
        this.$emit('update:dialog', value)
      }
    },
  },

  methods: {

    selectFile(file) {
      this.currentFile = file;
    },

    close() {
      this.dialogLocal = false
      this.$emit('dialog-closed')
    },

    upload() {

      let request = {
        provider: this.provider,
        userId: this.$store.state.userId
      };

      HfApi.uploadUserCredentials(this.currentFile, request)
          .finally(() => {
            this.close();
          });

    }

  },

  created() {
    this.init();
  },

}


</script>