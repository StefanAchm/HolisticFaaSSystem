<template>

  <v-toolbar
      elevation="0"
  >

    <v-toolbar-title>Functions</v-toolbar-title>

    <v-spacer></v-spacer>

    <FunctionTypeDialog
        :dialog.sync="functionDialogVisible"
        @dialog-closed="close"
        :edit-item="{}"/>

    <v-spacer v-if="workflowMode"></v-spacer>

    <v-menu offset-y v-if="!workflowMode">
      <template v-slot:activator="{ on, attrs }">
        <v-btn
            color="primary"
            v-bind="attrs"
            v-on="on"
            class="mx-2"
        >
          <v-icon
              left>
            mdi-plus
          </v-icon>
          Add
        </v-btn>
      </template>
      <v-list>
        <v-list-item-group>
          <v-list-item>

            <v-list-item-icon>
              <v-icon>mdi-lambda</v-icon>
            </v-list-item-icon>

            <v-list-item-title @click="functionDialogVisible = true;">Function</v-list-item-title>

          </v-list-item>

          <v-list-item>

            <v-list-item-icon>
              <v-icon>mdi-file-document-outline</v-icon>
            </v-list-item-icon>

            <input
                type="file"
                ref="fileInput1"
                @change="onFileSelected"
                style="display: none"
                accept=".yaml,.yml"
            />
            <v-list-item-title @click="openFileSelect">YAML</v-list-item-title>

          </v-list-item>

          <v-list-item>

            <v-list-item-icon>
              <v-icon>mdi-folder-zip</v-icon>
            </v-list-item-icon>

            <input
                type="file"
                ref="fileInput2"
                @change="onDeploymentSelected"
                style="display: none"
                accept=".zip"
            />
            <v-list-item-title @click="openDeploymentSelect">Deployment</v-list-item-title>

          </v-list-item>

        </v-list-item-group>
      </v-list>
    </v-menu>

    <v-btn
        color="primary"
        class="mx-2"
        :disabled="selected.length === 0"
        @click="downloadYaml">

      <v-icon
          left>
        mdi-download
      </v-icon>

      Download

    </v-btn>

    <FunctionsEditMenu
        :items="selected"
        @menu-closed="close"
    />

    <v-btn
        :disabled="selected.length === 0"
        color="primary"
        class="mx-2"
        @click="deployAll">

      <v-icon
          left>
        mdi-rocket-launch
      </v-icon>

      Deploy ({{ selected.length }})

    </v-btn>

    <v-spacer v-if="!workflowMode"></v-spacer>

    <v-switch
        v-if="!workflowMode"
        class="pl-10"
        append-icon="mdi-file-tree"
        v-model="treeView"
        prepend-icon="mdi-view-list"
        color="none"
    >

    </v-switch>


  </v-toolbar>

</template>

<script>

import FunctionsEditMenu from "@/components/function/FunctionsEditMenu.vue";
import HfApi from "@/utils/hf-api";
import common from "@/utils/common";
import FunctionTypeDialog from "@/components/function/dialogs/FunctionTypeDialog.vue";

export default {

  mixins: [common],

  components: {FunctionTypeDialog, FunctionsEditMenu},

  props: {
    selected: [],
    workflowMode: {
      type: Boolean,
      default: false
    }
  },

  data: () => ({

    functionMigrationDialogVisible: false,
    functionDialogVisible: false,
    functionImplementationDialogVisible: false,
    functionDeploymentDialogVisible: false,

  }),

  created() {
    //
  },

  computed: {
    treeView: {
      get() {
        return this.$store.state.treeView
      },
      set(value) {
        this.$store.commit('setTreeView', value)
      }
    },
  },

  methods: {

    deployAll() {

      this.selected.forEach(item => {
        HfApi.deployFunctionDeployment(item.functionDeployment.id, this.$store.state.awsSessionToken)
      })
      this.close()
    },

    close() {
      this.$emit('dialog-closed')
    },

    openFileSelect() {
      this.$refs.fileInput1.click()
    },

    onFileSelected(event) {
      HfApi.uploadYaml(event.target.files[0], this.$store.state.userId)
          .then(() => {
            this.close()
          })

    },

    openDeploymentSelect() {
      this.$refs.fileInput2.click()
    },

    onDeploymentSelected(event) {
      HfApi.uploadPackage(event.target.files[0], this.$store.state.userId)
          .then(() => {
            this.close()
          })

    },

    downloadYaml() {
      HfApi.downloadYaml(this.selected)
          .then(response => {
            // Create a new Blob object using the response data of the file
            let blob = new Blob([response.data], {type: 'application/yaml'});

            // Create a link element
            let link = document.createElement('a');

            // Create an object URL for the file
            link.href = window.URL.createObjectURL(blob);

            // Set the file name and download attribute
            link.download = 'functions.yaml';

            // Add the link to the DOM
            document.body.appendChild(link);

            // Simulate clicking the link
            link.click();

            // Remove the link from the DOM
            document.body.removeChild(link);
          });
    },


  }

}

</script>

<style>

</style>