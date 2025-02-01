<script setup lang="ts">
const props = defineProps({
  action: {
    type: Function,
    required: true,
  },
  icon: {
    type: String,
    required: true,
  },
  text: {
    type: String,
    required: true,
  }
})

const loading = ref(false);

async function handleClick() {
  if (!props.action || loading.value) return; // Avoid duplicate clicks
  loading.value = true;
  try {
    await props.action(); // Wait for async function
  } finally {
    loading.value = false; // Reset loading state after execution
  }
}
</script>

<template>
  <v-btn
    :loading="loading"
    height="48"
    @click="handleClick"
    :prepend-icon="icon"
    color="primary"
    flat
    rounded
    class="text-h6 px-10 mx-3"
    min-height="70px"
    min-width="200px"
    :text="text"
  >
  </v-btn>

</template>

<style scoped>

</style>
