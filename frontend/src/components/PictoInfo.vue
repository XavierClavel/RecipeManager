<script setup lang="ts">
defineProps({
  value: {
    type: [Number, String],
    required: true,
  },
  icon: {
    type: String,
    required: true,
  },
  iconSize: {
    type: String,
    required: false,
    default: "text-h3"
  },
  valueSize: {
    type: String,
    required: false,
    default: "text-h4"
  },
})
</script>

<template>
  <span class="d-flex row align-center justify-center mx-auto">
    <v-icon
      :class="`mx-auto px-3 ${iconSize}`"
      color="black"
    > {{ icon }} </v-icon>
    <v-card-text
      :class="`mx-auto px-3  align-center ${valueSize}`"
    > {{ value }} </v-card-text>
  </span>
</template>

<style scoped>

</style>
