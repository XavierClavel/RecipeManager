<script setup lang="ts">
import {getRecipeIconUrl, getUserIconUrl, toViewRecipe, toViewUser} from "@/scripts/common";

defineProps({
  recipe: {
    type: Object,
    required: true,
  },
})
</script>

<template>
      <v-card
        class="ma-4 pa-0"
        rounded="lg"
        width="250px"
        @click="toViewRecipe(recipe.id)"
      >
          <v-img
            color="surface-variant"
            height="187px"
            class="mt-n2"
            rounded="t-lg"
            :src="getRecipeIconUrl(recipe.id)"
            cover
          ></v-img>
        <v-card-title class="text-primary justify-end">{{recipe.title}}</v-card-title>
        <user-info :user="recipe.owner" class="mt-n4"></user-info>

    </v-card>

</template>

<style scoped>

</style>
