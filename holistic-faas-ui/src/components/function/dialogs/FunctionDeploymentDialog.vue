<template>


  <v-dialog
      v-model="dialogLocal"
      max-width="500px"
      @click:outside="close"
  >

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
                  v-model="editItemLocal.functionDeployment.provider"
                  :items="providers"
                  item-text="title"
                  item-value="value"
                  label="Provider"
              ></v-select>

            </v-col>

          </v-row>

          <v-row>

            <v-col>
              <v-text-field
                  type="number"
                  v-model="editItemLocal.functionDeployment.memory"
                  label="Memory"></v-text-field>
            </v-col>

          </v-row>

          <v-row>

            <v-col>
              <v-text-field
                  type="number"
                  v-model="editItemLocal.functionDeployment.timeoutSecs"
                  label="TimeoutSecs"></v-text-field>
            </v-col>

          </v-row>

          <v-row>

            <v-col>
              <v-text-field
                  type="text"
                  v-model="editItemLocal.functionDeployment.handler"
                  label="Handler"></v-text-field>
            </v-col>

          </v-row>

          <v-row>

            <v-col>

              <v-select
                  v-model="editItemLocal.functionDeployment.region"
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
                  v-model="editItemLocal.functionDeployment.runtime"
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
        <v-btn color="blue darken-1" text @click="save">Save</v-btn>
      </v-card-actions>

    </v-card>
  </v-dialog>

</template>

<script>

import common from '../../../utils/common'

import {CloudFunction} from "@/models/CloudFunction";

import HfApi from "@/utils/hf-api";

export default {

  mixins: [common],

  props: {
    dialog: Boolean,
    editItem: CloudFunction,
  },

  data() {

    return {

      dialogDelete: false,

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
        this.init()
            .then(() => {
                  this.editItemLocal = Object.assign({}, this.editItem);
                  // console.log("Edit item", this.editItem);
                }
            )
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

        console.log(newValue.provider)

        this.runtimes = this.getRuntimes(newValue.provider)
        this.regions = this.getRegions(newValue.provider)

        console.log(this.regions[0])

      },
      deep: true

    }

  },

  methods: {

    close() {
      this.dialogLocal = false
      this.$emit('dialog-closed')
    },


    save() {

      this.editItemLocal.functionDeployment.userId = this.$store.state.userId; // TODO ???

      if(this.editItemLocal.functionDeployment?.id) {

          HfApi.updateFunctionDeployment(this.editItemLocal.functionDeployment)
              .then(() => {
                this.close()
              })

        } else {

            HfApi.addFunctionDeployment(this.editItemLocal.functionDeployment)
                .finally(() => {
                  this.close();
                });
      }

    }

  },

  computed: {

    formTitle() {
      return this.editItemLocal.functionDeployment?.id ? 'Edit Deployment' :  'Create Deployment'
    },

    formSubtitle() {
      return 'Function: ' + this.editItemLocal.functionImplementation?.id
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