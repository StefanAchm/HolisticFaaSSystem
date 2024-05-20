<template>
  <div>

    <!-- Header -->
    <v-toolbar flat fluid>

      <v-toolbar-title>{{ workflow.name }}</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn color="primary" :disabled="!workflow.filePath" class="mx-2" @click="downloadWorkflow">

        <v-icon
            left>
          mdi-download
        </v-icon>

        Download Definition

      </v-btn>

      <WorkflowAddMenu
          :workflow-deployment="workflowDeployment"
          :workflow="workflow"
          :deployments="deployments"
          @workflow-updated="workflowUpdated()"
      >

      </WorkflowAddMenu>

    </v-toolbar>

    <v-card elevation="0" class="pb-6">

      <v-divider></v-divider>

      <v-card-text>
        <strong> Created by: </strong>
        {{ workflow.createdBy }}
      </v-card-text>

      <v-card-text>
        <strong> Description: </strong>
        {{ workflow.description }}
      </v-card-text>

    </v-card>

  </div>
</template>

<script>

import HfApi from "@/utils/hf-api";
import download from "@/utils/download";
import WorkflowAddMenu from "@/components/workflows/WorkflowAddMenu.vue";

export default {

  props: {
    workflow: {
      type: Object,
      required: true
    },
    deployments: {
      type: Array,
      required: false
    }
  },

  components: {WorkflowAddMenu,},

  data() {

    return {
      workflowDeployment: {}, // This will hold the fetched workflow deployment
    };

  },

  created() {
    this.getWorkflowDeployment();
  },

  computed: {},

  methods: {

    workflowUpdated() {
      this.getWorkflowDeployment();
      this.$emit('workflow-updated');
    },

    downloadWorkflow() {

      HfApi.downloadWorkflow(this.workflow.id)
          .then((response) => {
                download.downloadFile(response.data, this.workflow.name + '.yaml')
              }
          )

    },

    getWorkflowDeployment() {

      HfApi.prepareWorkflowDeployment(this.$route.params.id)
          .then((response) => {

            this.workflowDeployment = response.data

          })

    },

  },

}

</script>