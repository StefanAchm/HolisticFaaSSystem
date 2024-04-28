<template>
  <v-tooltip bottom>

    <template v-slot:activator="{ on, attrs }">

      <v-icon
          :color="getColor(item)"
          v-on="on"
          v-bind="attrs"
          @click="deployFunction(item)"
          :class="{
                'pulsate-icon': item.functionDeployment?.status === 'STARTED',
              }"
      >
        {{ getIcon(item) }}
      </v-icon>


    </template>

    <span>{{ toolTipMessage(item) }}</span>


  </v-tooltip>
</template>
<script >

import HfApi from "@/utils/hf-api";

export default {

  props: {
    item: {
      type: Object,
      required: true
    }
  },


  methods: {

    getIcon(item) {

      if (item.functionDeployment === null) {
        return 'mdi-progress-helper'
      }

      let status = item.functionDeployment.status

      if (status === 'DEPLOYED') {
        return 'mdi-progress-check'
      } else if (status === 'FAILED') {
        return 'mdi-progress-alert'
      } else if (status === 'STARTED') {
        return 'mdi-progress-upload'
      } else if (status === 'CREATED') {
        return 'mdi-progress-clock'
      } else if (status === 'CHANGED') {
        return 'mdi-progress-wrench'
      } else {
        return ''
      }
    },

    getColor(item) {

      if (item.functionDeployment === null) {
        return 'neutral'
      }

      let status = item.functionDeployment.status

      if (status === 'DEPLOYED') {
        return 'success'
      } else if (status === 'FAILED') {
        return 'error'
      } else if (status === 'STARTED') {
        return 'info'
      } else if (status === 'CREATED') {
        return 'warning'
      } else if (status === 'CHANGED') {
        return 'warning'
      } else {
        return ''
      }

    },

    toolTipMessage(item) {

      if (item.functionDeployment === null) {
        return 'No Deployment'
      }

      if (item.functionDeployment.text) {
        return item.functionDeployment.text
      } else {
        return item.functionDeployment?.statusMessage
      }

    },

    deployFunction(item) {

      if(item.functionDeployment.status === 'CREATED') {
        HfApi.deployFunctionDeployment(item.functionDeployment.id)
      }

    },

  }

}

</script>

<style scoped>

.pulsate-icon {
  animation: pulsate 1s infinite;
}

@keyframes pulsate {
  0% {
    transform: scale(0.9);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(0.9);
  }
}

</style>