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
})
</script>

<template>
  <span class="d-flex row align-center justify-center mx-auto">
    <v-icon
      :class="`mx-auto px-3 text-h5 text-sm-h4`"
      color="black"
    > {{ icon }} </v-icon>
    <v-card-text
      :class="`mx-auto px-3  align-center text-h6 text-sm-h5`"
    > {{ value }} </v-card-text>
  </span>
</template>

<style scoped>

</style>
