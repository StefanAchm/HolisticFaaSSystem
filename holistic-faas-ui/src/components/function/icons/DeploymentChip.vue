<template>

  <v-chip
      small
      :color="getColor(item)"
  >
    {{ item.status }}
  </v-chip>
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

    getColor(item) {

      if (item.status === 'DEPLOYED') {
        return 'success'
      } else if (item.status === 'FAILED') {
        return 'error'
      } else if (item.status === 'STARTED') {
        return 'info'
      } else if (item.status === 'CREATED') {
        return 'warning'
      } else if (item.status === 'CHANGED') {
        return 'warning'
      } else {
        return ''
      }

    },

  }

}

</script>

<style scoped>


</style>