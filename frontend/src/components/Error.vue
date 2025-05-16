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
    class="pa-1 pa-sm-5 ma-5"
    v-if="error"
    variant="outlined"
    color="primary"
  >
    <v-card-title >
      ⚠️ {{ $t(error) }}
    </v-card-title>
  </v-card>
</template>

<style scoped>

</style>
