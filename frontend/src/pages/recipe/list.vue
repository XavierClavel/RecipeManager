<template>
  <v-layout class="rounded rounded-md">
    <span v-for="recipe in recipes">
      <v-btn
        :color="color"
        :variant="variant"
        class="ma-4"
        rounded="lg"
        min-height="200px"
        @click="toViewRecipe(recipe.id)"
      >
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
          > mdi-silverware-fork-knife </v-icon>
          <v-card-text
            class="mx-auto px-3  align-center text-h5"
          > {{ recipe.servings }} </v-card-text>
        </span>

        <span class="d-flex row align-center justify-center mx-auto">
          <v-icon
            class="mx-auto px-3 text-h4"
            color="primary"
          > mdi-chef-hat </v-icon>
          <v-card-text
            class="mx-auto px-3  align-center text-h5"
          > {{ recipe.preparationTime }} min </v-card-text>
        </span>

        <span class="d-flex row align-center justify-center mx-auto">
          <v-icon
            class="mx-auto px-3 text-h4"
            color="primary"
          > mdi-stove </v-icon>
          <v-card-text
            class="mx-auto px-3  align-center text-h5"
          > {{ recipe.cookingTime }} min </v-card-text>
        </span>
      </span>
      </v-card-item>

    </v-btn>
    </span>


  </v-layout>
</template>

<script lang="ts" setup>
import {listRecipes} from "@/scripts/recipes";
import {toViewRecipe} from "@/scripts/common";

const recipes = ref<object[]>([])

listRecipes().then(
  function (response) {
    console.log(response)
    recipes.value = response.data
  }
)

const toRecipe = (id) => toViewRecipe((id))
</script>
