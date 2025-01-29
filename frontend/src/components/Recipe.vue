<script setup lang="ts">
import {getRecipeIconUrl, toViewRecipe} from "@/scripts/common";
import {ICON_RECIPE_YIELD} from "../scripts/icons";

defineProps({
  recipe: {
    type: Object,
    required: true,
  },
})
</script>

<template>
      <v-btn
        class="ma-4"
        rounded="lg"
        min-height="300px"
        width="300px"
        @click="toViewRecipe(recipe.id)"
      >
        <span class="flex-column">
        <v-img
          color="surface-variant"
          height="169px"
          width="300px"
          class="mt-n3"
          rounded="t-lg"
          :src="getRecipeIconUrl(recipe.id)"
          cover
        ></v-img>
      <v-card-item>
        <div>
          <div class="text-h6 mb-1 text-primary">
            {{recipe.title}}
          </div>
          <div class="text-caption"></div>
        </div>

        <span class="d-flex flex-row">
        <span class="d-flex row align-center justify-center mx-auto">
          <v-icon
            class="mx-auto px-3 text-h4"
            color="primary"
          > {{ICON_RECIPE_YIELD}} </v-icon>
          <v-card-text
            class="mx-auto px-3  align-center text-h5"
          > {{ recipe.yield }} </v-card-text>
        </span>

        <span class="d-flex row align-center justify-center mx-auto">
          <v-icon
            class="mx-auto px-3 text-h4"
            color="primary"
          > mdi-chef-hat </v-icon>
          <v-card-text
            class="mx-auto px-3 ml-n2  align-center text-h6"
          > {{ recipe.preparationTime }} min </v-card-text>
        </span>

        <span class="d-flex row align-center justify-center mx-auto" v-if="recipe.cookingTime">
          <v-icon
            class="mx-auto px-3 text-h4"
            color="primary"
          > mdi-stove </v-icon>
          <v-card-text
            class="mx-auto px-3  align-center ml-n2 text-h6"
          > {{ recipe.cookingTime }} min </v-card-text>
        </span>
      </span>
      </v-card-item>
          </span>

    </v-btn>

</template>

<style scoped>

</style>
