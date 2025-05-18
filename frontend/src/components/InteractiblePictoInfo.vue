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
  action: {
    type: Function,
  }
})
</script>

<template>
  <span class="d-flex row align-center justify-center mx-auto">
    <v-btn class="d-flex pa-8 px-3" variant="flat" @click="action" color="background">
      <v-icon
        class="mx-auto px-3 text-h3"
        color="primary"
      > {{ icon }} </v-icon>
      <v-card-text
        class="mx-auto px-3  align-center text-h4"
        style="width: 100px"
      > {{ value }} </v-card-text>
    </v-btn>
  </span>
</template>

<style scoped>

</style>
