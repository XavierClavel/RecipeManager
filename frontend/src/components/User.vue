<script setup lang="ts">
import {getCookbookIconUrl, getUserIconUrl, toViewCookbook, toViewUser} from "@/scripts/common";

defineProps({
  user: {
    type: Object,
    required: true,
  },
})
</script>

<template>
  <v-card
    class="ma-4"
    rounded="lg"
    width="150px"
    @click="toViewUser(user.id)"
  >
        <v-img
          color="surface-variant"
          height="150px"
          class="mt-n3"
          rounded="t-lg"
          :src="getUserIconUrl(user.id)"
          cover
        ></v-img>
        <v-card-title class="text-primary">{{ user.username }}</v-card-title>

    </v-card>

</template>

<style scoped>

</style>
