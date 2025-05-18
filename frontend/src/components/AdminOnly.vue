<script setup lang="ts">

import {useAuthStore} from "@/stores/auth";

const authStore = useAuthStore()
const isAdmin = ref(authStore.isAdmin)
</script>

<template>
  <div v-if="isAdmin">
    <slot/>
  </div>

</template>

<style scoped>

</style>
