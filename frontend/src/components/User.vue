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
    width="150px"
    @click="toViewUser(user.id)"
  >
        <v-img
          color="surface-variant"
          height="150px"
          class="mt-n3"
          rounded="t-lg"
          :src="getUserIconUrl(user.id, user.version)"
          cover
        ></v-img>
        <v-card-title class="text-black text-h6 text-sm-h6 text-truncate font-weight-regular">{{ user.username }}</v-card-title>

    </v-card>

</template>

<style scoped>

</style>
