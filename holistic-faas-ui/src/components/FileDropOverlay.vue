<template>

  <div>

    <div
        class="file-drop-zone"
        @dragover.prevent
        @dragenter.prevent="onDragEnter">

    </div>

    <div
        class="file-drop-zone file-drop-overlay text-h3"
        v-if="isDragging && !isDropping"
        @dragover.prevent
        @dragenter.prevent="onDragEnter"
        @dragleave.prevent="onDragLeave"
        @drop.prevent="onDrop"
    >

      Drop zip or yaml file to upload

    </div>

    <div class="file-drop-zone file-drop-overlay" v-if="isDropping">
      <v-progress-circular
          indeterminate
          size="64"
      ></v-progress-circular>
    </div>


  </div>

</template>

<script>
import HfApi from "@/utils/hf-api";

export default {
  data() {
    return {
      isDragging: false,
      isDropping: false,
    };
  },
  methods: {
    onDragEnter() {
      this.isDragging = true;
    },
    onDragLeave() {
      if(!this.isDropping) {
        this.isDragging = false;
      }
    },
    onDrop(e) {
      this.isDropping = true
      const files = e.dataTransfer.files;

      HfApi.uploadAny(files[0], this.$store.state.userId).then(() => {
        this.$emit('file-dropped');
      }).then(() => {

      }).finally(() => {
        this.isDragging = false;
        this.isDropping = false;
      });


    },

  },

};

</script>

<style scoped>

.file-drop-zone {
  position: fixed;
  top: 10px;
  left: 10px;
  right: 10px;
  bottom: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1;
  border-radius: 50px !important;
  transition: background-color 0.3s ease;
}

.file-drop-overlay {
  z-index: 1000 !important;
  background-color: rgba(0, 0, 0, 0.3);
  border-color: #00416a;
  border-width: 5px;
  border-style: dashed;
}


</style>