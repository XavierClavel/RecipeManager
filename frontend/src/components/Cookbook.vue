<script setup lang="ts">
import {getCookbookIconUrl, toViewCookbookRecipes} from "@/scripts/common";
import {ICON_COOKBOOK_RECIPES, ICON_COOKBOOK_USERS, ICON_RECIPE_YIELD} from "../scripts/icons";

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
    rounded="lg"
    width="250px"
    @click="toViewCookbookRecipes(cookbook.id)"
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
    </v-card>

</template>

<style scoped>

</style>
