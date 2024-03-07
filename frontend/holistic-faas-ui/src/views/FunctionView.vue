<template>
  <v-data-table :headers="headers" :items="functions"></v-data-table>
</template>

<script>

import axios from "axios";
import {Properties} from "@/config";

export default {

  data() {
    return {
      headers: [
        {text: 'Id', value: 'id', width: '150px'},
        {text: 'Name', value: 'name', width: '150px'},
        {text: 'FilePath', value: 'filePath', width: '400px'}
      ],
      functions: []
    }
  },

  methods: {

    init() {
      axios
          .get(Properties.API_IP + '/function/getAll')
          .then(response => {

            this.functions = response.data;

            console.log(this.functions)

          })
    }

  },

  created() {
    this.init();
  },

  // mounted() {
  //   this.init();
  // },




}


</script>