<script setup lang="ts">
import {getCookbookIconUrl, getUserIconUrl, toViewCookbook} from "@/scripts/common";

defineProps({
  cookbook: {
    type: Object,
    required: true,
  },
})
</script>

<template>
  <v-card
    class="ma-4"
    width="250px"
    @click="toViewCookbook(cookbook.id)"
  >
        <v-img
          color="surface-variant"
          height="250px"
          class="mt-n3"
          rounded="t-lg"
          :src="getCookbookIconUrl(cookbook.id)"
          cover
        ></v-img>
        <v-card-title class="text-primary">{{ cookbook.title }}</v-card-title>
        <v-row class="px-6 pt-2 pb-5">
          <v-avatar start size="30">
            <v-img :src="getUserIconUrl(cookbook.members[0].id)"></v-img>
          </v-avatar>
        </v-row>

    </v-card>

</template>

<style scoped>

</style>
