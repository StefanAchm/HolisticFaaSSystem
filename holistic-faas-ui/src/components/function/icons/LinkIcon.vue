<template>
  <v-tooltip
      v-if="item.functionDeployment"
      bottom>

    <template v-slot:activator="{ on, attrs }">

      <!-- open a new tab either for aws or gcp to the corresponding provider console -->
      <v-icon
          v-on="on"
          v-bind="attrs"
          @click="openNewTab(item)"
          color="secondary"
      >mdi-open-in-new
      </v-icon>

    </template>

    <span>Open in {{ item.functionDeployment?.provider }} console</span>

  </v-tooltip>
</template>
<script>

export default {

  props: {
    item: {
      type: Object,
      required: true
    }
  },

  methods: {

    openNewTab(item) {
      window.open(item.functionDeployment.uri)
    },

  }

}

</script>