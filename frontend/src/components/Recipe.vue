<script setup lang="ts">
import {getRecipeThumbnailUrl, toViewRecipe} from "@/scripts/common";

defineProps({
  recipe: {
    type: Object,
    required: true,
  },
})
</script>

<template>
      <v-card
        class="ma-0 pa-0"
        width="240px"
        @click="toViewRecipe(recipe.id)"
      >
          <v-img
            color="surface-variant"
            height="180px"
            class="mt-n2"
            rounded="t-lg"
            :src="getRecipeThumbnailUrl(recipe.id, recipe.version)"
            cover
          ></v-img>
        <v-card-title class="text-black text-h6 text-sm-h6 text text-truncate font-weight-regular justify-end">{{recipe.title}}</v-card-title>
        <user-info :user="recipe.owner" class="mt-n4"></user-info>

    </v-card>

</template>

<style scoped>

</style>
