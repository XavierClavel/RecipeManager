<script setup lang="ts">

defineProps({
  error: {
    type: String,
    required: true,
  },
})
</script>

<template>
  <v-card
    class="pa-5 ma-5 d-flex flex-row"
    v-if="error"
    variant="outlined"
    color="primary"
  >
    <v-icon color="black" class="text-h3 mr-5 ml-3 mt-2" >mdi-alert</v-icon>
    <v-card-title prepend-icon="mdi-alert" class="text-black text-h4">
      {{ $t(error) }}
    </v-card-title>
  </v-card>
</template>

<style scoped>

</style>
