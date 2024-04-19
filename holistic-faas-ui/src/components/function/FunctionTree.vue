<template>

  <div>

    <FunctionTypeDialog
        :dialog.sync="functionDialogVisible"
        @dialog-closed="close"
        :edit-item="editItem"/>


    <FunctionDeploymentDialog
        :dialog.sync="functionDeploymentDialogVisible"
        @dialog-closed="close"
        :edit-item="editItem"
    />

    <FunctionImplementationDialog
        :dialog.sync="functionImplementationDialogVisible"
        @dialog-closed="close"
        :edit-item="editItem"
    />


    <v-treeview
        v-model="selected"
        :items="items"
        :search="search"
        selectable
        item-key="id"
    >


<!--      <template v-slot:prepend="{ item }">-->



<!--      </template>-->

      <template v-slot:label="{ item }">

        <v-icon v-if="item.icon">
          {{ item.icon }}
        </v-icon>

        <v-btn
            color="secondary"
            outlined
            rounded
            v-if="item.empty"
            small
            @click="add(item)"
        >

          <v-icon left>
            mdi-plus
          </v-icon>

          {{ item.name }}
        </v-btn>


        <span v-else>

        {{ item.name }}

        <DeploymentIcon
            v-if="item.type === 'functionDeployment'"
            :item=item.actualFunctionDeployment
        />

      <LinkIcon
          v-if="item.type === 'functionDeployment'"
          :item=item.actualFunctionDeployment
      />

      </span>

      </template>

    </v-treeview>
  </div>


</template>

<script>

import DeploymentIcon from "@/components/function/icons/DeploymentIcon.vue";
import LinkIcon from "@/components/function/icons/LinkIcon.vue";
import FunctionDeploymentDialog from "@/components/function/dialogs/FunctionDeploymentDialog.vue";
import FunctionImplementationDialog from "@/components/function/dialogs/FunctionImplementationDialog.vue";
import FunctionTypeDialog from "@/components/function/dialogs/FunctionTypeDialog.vue";

export default {
  components: {FunctionTypeDialog, FunctionImplementationDialog, FunctionDeploymentDialog, LinkIcon, DeploymentIcon},

  props: {
    search: String,
    functionsFromProps: []
  },

  data: () => ({
    items: [],
    selected: [],

    functionDialogVisible: false,
    functionDeploymentDialogVisible: false,
    functionImplementationDialogVisible: false,

    editItem: {
      functionType: {},
      functionImplementation: {},
      functionDeployment: {}
    },

  }),

  watch: {
    functionsFromProps() {
      this.init()
    },
    selected() {
      this.$emit('update-selected', this.functionsFromProps.filter(f => this.selected.includes(f.id)))
    },
  },

  created() {
    this.init()
  },

  methods: {

    add(item) {
      this.editItem = Object.assign({}, item.actualFunctionDeployment)
      if (item.type === 'functionType') {
        // this.editItem.functionType = null
        this.functionDialogVisible = true
      } else if (item.type === 'functionImplementation') {
        this.editItem.functionImplementation = {functionTypeId: this.editItem.functionType.id};
        this.functionImplementationDialogVisible = true
      } else if (item.type === 'functionDeployment') {
        this.editItem.functionDeployment = {functionImplementationId: this.editItem.functionImplementation.id}
        this.functionDeploymentDialogVisible = true
      }
    },

    close() {
      this.$emit('update-list')
    },

    init() {

      this.editItem = {
        functionType: {},
        functionImplementation: {},
        functionDeployment: {}
      };

      this.items = []

      this.items.push({
        id: 'add-function',
        name: 'Add function',
        icon: '',
        children: [],
        empty: true,
        type: 'functionType',
      })

      this.functionsFromProps.forEach(f => {

        let functionType = this.items.find(item => item.id === f.functionType.id)
        if (!functionType) {
          functionType = {
            id: f.functionType.id,
            name: f.functionType.name,
            icon: 'mdi-lambda',
            children: [],
            type: 'functionType',
            actualFunctionDeployment: f
          }
          this.items.push(functionType)

          let functionImplementationToAdd = {
            id: f.functionType.id + '-empty',
            name: 'Add implementation',
            icon: '',
            children: [],
            empty: true,
            type: 'functionImplementation',
            actualFunctionDeployment: f
          }
          functionType.children.push(functionImplementationToAdd)

          if (!f.functionImplementation) {
            return;
          }

        }

        let functionImplementation = functionType.children.find(item => item.id === f.functionImplementation.id)
        if (!functionImplementation) {
          functionImplementation = {
            id: f.functionImplementation.id,
            name: f.functionImplementation.fileName,
            icon: 'mdi-file-code-outline',
            children: [],
            type: 'functionImplementation',
            actualFunctionDeployment: f
          }
          functionType.children.push(functionImplementation)

          let functionDeploymentToAdd = {
            id: f.functionImplementation.id + '-empty',
            name: 'Add deployment',
            icon: '',
            children: [],
            empty: true,
            type: 'functionDeployment',
            actualFunctionDeployment: f
          }
          functionImplementation.children.push(functionDeploymentToAdd)

          if (!f.functionDeployment) {
            return;
          }

        }

        let functionDeployment = functionImplementation.children.find(item => item.id === f.functionDeployment.id)
        if (!functionDeployment) {
          functionDeployment = {
            id: f.functionDeployment.id,
            name: 'Deployment: ' + f.functionDeployment.handler + ' - ' + f.functionDeployment.provider + ' - ' + f.functionDeployment.region + ' - ' + f.functionDeployment.timeoutSecs + 's - ' + f.functionDeployment.memory + 'mb - ' + f.functionDeployment.runtime + ' - ' + f.functionDeployment.userName,
            icon: 'mdi-rocket',
            children: [],
            type: 'functionDeployment',
            actualFunctionDeployment: f
          }
          functionImplementation.children.push(functionDeployment)
        }

      })

    }

  },


}

</script>