<template>
  <v-file-input
    @change="onImageUpload"
    v-model="image"
    style="display: none"
    ref="fileInput"
    accept="image/*"
  ></v-file-input>

  <v-img
    :src="imageUrl"
    :width="width"
    :aspect-ratio="aspectRatio"
    cover
    color="surface-variant"
    class="preview-image my-6 mx-auto"
    :rounded="rounded"
    @click="triggerFileInput"
    @load="handleImageLoad"
    @error="handleImageError"
    style="border: 4px solid #0d1821 !important;"
  >
    <v-container :class="`image-overlay d-flex flex-row ${buttonsSpacing}`"  >
      <v-btn
        :width="buttonsSize"
        :height="buttonsSize"
        :rounded="buttonsRounded"
        color="primary"
        icon="mdi-pencil"
        :class="`${buttonsIconSize} overlay-button`"
      ></v-btn>
      <v-btn
        :width="buttonsSize"
        :height="buttonsSize"
        :rounded="buttonsRounded"
        color="primary"
        icon="mdi-arrow-u-left-top"
        :class="`${buttonsIconSize} overlay-button`"
        v-if="imageUpdated"
        @click.stop="undoImageChange"
      ></v-btn>
      <v-btn
        :width="buttonsSize"
        :height="buttonsSize"
        :rounded="buttonsRounded"
        color="primary"
        icon="mdi-close"
        :class="`${buttonsIconSize} overlay-button`"
        v-if="hasImage && !imageDeleted"
        @click.stop="deleteImage"
      ></v-btn>
    </v-container>

  </v-img>
</template>

<script setup lang="ts">

const props = defineProps({
  id: {
    type: [Number],
    required: false,
  },
  version: {
    type: [Number],
    required: false,
  },
  path: {
    type: [String],
    required: true,
  },
  aspectRatio: {
    type: [Number],
    required: false,
    default: "1"
  },
  width: {
    type: [String],
    required: false,
    default: "1000px"
  },
  rounded: {
    type: [String],
    required: false,
    default: "lg"
  },
  buttonsSize: {
    type: [String],
    required: false,
    default: "100px"
  },
  buttonsIconSize: {
    type: [String],
    required: false,
    default: "text-h4"
  },
  buttonsSpacing: {
    type: [String],
    required: false,
    default: "ga-16"
  },
  buttonsRounded: {
    type: [String],
    required: false,
    default: "xl"
  },
})

import {doDeleteImage, getDefaultImageUrl, getImageUrl, uploadImage} from "@/scripts/common";
import {ref} from "vue";

const image = ref<File | null>(null)
const baseImageUrl = ref<string>(getImageUrl(props.path, props.id, props.version))
const defaultImageUrl = ref<string>(getDefaultImageUrl(props.path))
const imageUrl = ref<string>(baseImageUrl.value)

const fileInput = ref(null)
const imageUpdated = ref<Boolean>(false)
const imageDeleted = ref<Boolean>(false)
const hasImage = ref<Boolean>(false)

const onImageUpload = () => {
  imageUrl.value = URL.createObjectURL(image.value)
  imageUpdated.value = true
}

function undoImageChange() {
  imageUpdated.value = false
  imageDeleted.value = false
  imageUrl.value = baseImageUrl.value
}

function deleteImage() {
  imageDeleted.value = hasImage.value
  imageUpdated.value = hasImage.value

  imageUrl.value = defaultImageUrl.value
}

// Trigger the v-file-input click
function triggerFileInput() {
  if (fileInput.value) {
    fileInput.value.click();
  }
}

function handleImageLoad(url) {
  // Only consider the primary image for setting imageExists
  if (url === baseImageUrl.value) {
    console.log("image success")
    hasImage.value = true
  }
}

function handleImageError(url) {
  console.log("load error")
  // If primary image fails, set imageExists to false and hide the button
  if (url === baseImageUrl.value) {
    console.log("image error")
    hasImage.value = false
  }
}

async function submitImage(): Promise<number> {
  console.log(imageDeleted.value)
  if (imageDeleted.value) {
    await doDeleteImage(props.path, props.id)
    return 0
  } else if (imageUpdated.value) {
    await uploadImage(props.id, image.value, props.path)
    return props.version + 1
  }
}

defineExpose({
  submitImage,
})

</script>

<style scoped>
.preview-image {
  cursor: pointer;
}
.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0);
  transition: background-color 0.3s ease;
}
.preview-image:hover .image-overlay {
  background-color: rgba(255, 255, 255, 0.3); /* Adjust opacity for desired whitening effect */
}

.overlay-button {
  opacity: 0;
  transition: opacity 0.3s ease;
}

.preview-image:hover .overlay-button {
  opacity: 1; /* Button becomes visible on hover */
}
</style>
