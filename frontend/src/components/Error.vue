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
    <v-card-text class="text-h5  text-sm-h4 text-black text-pre-wrap font-weight-bold">
      ⚠️ {{ $t(error) }}
    </v-card-text>
  </v-card>
</template>

<style scoped>

</style>
