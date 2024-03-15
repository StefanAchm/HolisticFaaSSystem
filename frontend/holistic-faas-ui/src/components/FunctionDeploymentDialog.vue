<template>


  <v-dialog v-model="dialogVisible" max-width="500px">

    <v-card>
      <v-card-title>
        <span class="text-h5">{{ formTitle }}</span>
      </v-card-title>

<!--      Add some spacing between the two titles-->

      <v-spacer></v-spacer>

      <v-card-subtitle>
        <span class="text-h7">{{ formSubtitle }}</span>
      </v-card-subtitle>

      <v-card-text>
        <v-container>

          <v-row>

            <v-col>

              <v-select
                  v-model="editItem.provider"
                  :items="providers"
                  item-text="title"
                  item-value="value"
                  label="Provider"
              ></v-select>

            </v-col>

          </v-row>

          <v-row>

            <v-col>

              <v-select
                  v-model="editItem.userName"
                  :items="users"
                  item-text="title"
                  item-value="value"
                  label="User"
              ></v-select>


            </v-col>

          </v-row>

<!--          <v-row>-->

<!--            <v-col>-->

<!--              <v-select-->
<!--                  v-model="editItem.functionId"-->
<!--                  :items="functions"-->
<!--                  :disabled=true-->
<!--                  item-text="title"-->
<!--                  item-value="value"-->
<!--                  label="Function"-->
<!--              ></v-select>-->

<!--            </v-col>-->

<!--          </v-row>-->


          <v-row>

            <v-col>
              <v-text-field v-model="editItem.memory" label="Memory"></v-text-field>
            </v-col>

          </v-row>

          <v-row>

            <v-col>
              <v-text-field v-model="editItem.timeoutSecs" label="TimeoutSecs"></v-text-field>
            </v-col>

          </v-row>

          <v-row>

            <v-col>
              <v-text-field v-model="editItem.handler" label="Handler"></v-text-field>
            </v-col>

          </v-row>

          <v-row>

            <v-col>

              <v-select
                  v-model="editItem.region"
                  :items="regions"
                  item-text="title"
                  item-value="value"
                  label="Region"
              ></v-select>


            </v-col>

          </v-row>

          <v-row>

            <v-col>

              <v-select
                  v-model="editItem.runtime"
                  :items="runtimes"
                  item-text="title"
                  item-value="value"
                  label="Runtime"
              ></v-select>

            </v-col>

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


import axios from "axios";
import {Properties} from "@/config";

import common from '../common'

import {CloudFunction} from "@/models/CloudFunction";

export default {

  mixins: [common],

  props: {
    dialog: Boolean,
    itemprop: CloudFunction,
  },

  data() {

    return {

      dialogDelete: false,
      currentFile: null,

      menu: false,
      activePicker: null,

      users: [],
      allUsers: [],

      functions: [],
      allFunctions: [],

      providerOptions: [],

      runtimes: [],

      regions: []

    }
  },

  watch: {

    editItem: {
      handler(newValue) {

        // Filter users by provider
        // Create a new array with the filtered users for select
        this.users = this.allUsers
            .filter(user => user.provider === newValue.provider)
            .map(user => {
              return {
                title: user.username,
                value: user.username
              }

            })

        this.functions = this.allFunctions
            // .filter(func => func.provider === newValue.provider)
            .map(func => {
              return {
                title: func.id,
                value: func.id
              }

            })

        this.runtimes = this.providerOptions
            .filter(provider => provider.provider === newValue.provider)
            .map(provider => provider.runtimes)
            .flat()
            .map(runtime => {
              return {
                title: runtime,
                value: runtime
              }
            })

        this.regions = this.providerOptions
            .filter(provider => provider.provider === newValue.provider)
            .map(provider => provider.regions)
            .flat()
            .map(region => {
              return {
                title: region,
                value: region
              }
            })


      },
      deep: true

    }

  },

  methods: {

    init() {

      axios
          .get(Properties.API_IP + '/user/getAll')
          .then(response => {
            this.allUsers = response.data;
          })

      axios
          .get(Properties.API_IP + '/function/getAll')
          .then(response => {
            this.allFunctions = response.data;
          })

      axios
          .get(Properties.API_IP + '/provider/getProviderOptions')
          .then(response => {
            this.providerOptions = response.data;
          })

    },

    close(cf) {
      this.dialogVisible = false
      this.$emit('dialog-closed', cf)
    },


    upload() {

      axios.post(
          Properties.API_IP + "/deploy/add",
          JSON.stringify(this.editItem),
          {headers: {'Content-Type': 'application/json'}})
          .then(response => {
            console.log(response)
          })
          .finally(() => {
            this.close(this.editItem);
          });

    }

  },

  computed: {

    formTitle() {
      return 'Create Deployment'
    },

    formSubtitle() {
      return 'Function: ' +  this.editItem.functionName
    },

    dialogVisible: {
      get() {
        return this.dialog
      },
      set(value) {
        this.$emit('update:dialog', value)
      }
    },

    editItem: {
      get() {
        return this.itemprop ? this.itemprop : new CloudFunction()
      },
      set(value) {
        this.$emit('update:itemProp', value)
      }
    }

  },

  created() {
    this.init();
  },

}


</script>