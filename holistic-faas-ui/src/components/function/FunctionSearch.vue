<template>
  <v-toolbar
      elevation="0"
  >
    <v-text-field
        v-model="search"
        append-icon="mdi-magnify"
        label="Search"
        single-line
        hide-details
    ></v-text-field>
  </v-toolbar>
</template>

<script>
export default {

  data: () => ({
    search: ''
  }),

  watch: {
    search() {
      this.$emit('update-search', this.search)
    }
  }

};
</script>
