<template>


  <v-dialog v-model="dialogLocal" max-width="500px">

    <v-card>
      <v-card-title>
        <span class="text-h5">{{ formTitle }}</span>
      </v-card-title>

      <v-spacer></v-spacer>

      <v-card-subtitle>
        <span class="text-h7">{{ formSubtitle }}</span>
      </v-card-subtitle>

      <v-card-text>
        <v-container>

          <v-row>

            <v-col>

              <v-select
                  v-model="editItemLocal.provider"
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
                  v-model="editItemLocal.userName"
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
          <!--                  v-model="editItemLocal.functionId"-->
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
              <v-text-field v-model="editItemLocal.memory" label="Memory"></v-text-field>
            </v-col>

          </v-row>

          <v-row>

            <v-col>
              <v-text-field v-model="editItemLocal.timeoutSecs" label="TimeoutSecs"></v-text-field>
            </v-col>

          </v-row>

          <v-row>

            <v-col>
              <v-text-field v-model="editItemLocal.handler" label="Handler"></v-text-field>
            </v-col>

          </v-row>

          <v-row>

            <v-col>

              <v-select
                  v-model="editItemLocal.region"
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
                  v-model="editItemLocal.runtime"
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

import common from '../utils/common'

import {CloudFunction} from "@/models/CloudFunction";

import HfApi from "@/utils/hf-api";

export default {

  mixins: [common],

  props: {
    dialog: Boolean,
    editItem: CloudFunction,
    functionImplementation: {type: Object, default: null}
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

      regions: [],
      
      editItemLocal: {}

    }
  },

  watch: {
    
    dialog(val) {
      if(val) {

        if(this.editItem?.id) {
          this.editItemLocal = this.editItem
        } else {
          this.editItemLocal = {
            functionImplementationId: this.functionImplementation.id
          }
        }

        this.init()

      }

    },

    editItemLocal: {
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

      HfApi.getAllUsers()
          .then(response => {
            this.allUsers = response.data;
          })

      HfApi.getAllFunctions()
          .then(response => {
            this.allFunctions = response.data;
          })

      HfApi.getProviderOptions()
          .then(response => {
            this.providerOptions = response.data;
          })

    },

    close() {
      console.log('close before')
      this.dialogLocal = false
      this.$emit('dialog-closed')
      console.log('close after')
    },


    upload() {

      HfApi.deployFunction(this.editItemLocal)
          .finally(() => {
            this.close();
          });

    }

  },

  computed: {

    formTitle() {
      return 'Create Deployment'
    },

    formSubtitle() {
      return 'Function: ' + this.editItemLocal.functionImplementationId
    },

    dialogLocal: {
      get() {
        return this.dialog
      },
      set(value) {
        this.$emit('update:dialog', value)
      }
    },

  },

}


</script>