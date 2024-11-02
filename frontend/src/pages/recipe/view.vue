<template>
  <v-card
    class="mx-auto rounded-xl pa-5 ma-auto my-5 d-flex flex-row"
    max-width="1000px"
    v-if="displayError"
  >
    <v-icon color="primary" class="text-h3 mr-5 ml-3 mt-2" >mdi-alert</v-icon>
    <v-card-title prepend-icon="mdi-alert" class="text-primary text-h4">
      {{ errorMessage }}
    </v-card-title>
  </v-card>

  <v-card
  class="mx-auto rounded-xl pa-5 ma-auto my-5"
  max-width="1000px"
  v-if="!displayError"
  >
    <v-card-title
      class="mx-auto px-3 text-primary text-h3"
    >{{ recipe.title }}</v-card-title>

    <v-card-text
      class="mx-auto px-3"
    > {{recipe.description}} </v-card-text>

    <v-img
      color="surface-variant"
      height="562px"
      width="1000px"
      class="mt-n3"
      rounded="lg"
      :src="imageUrl"

      cover
    ></v-img>

      <span class="d-flex flex-row">
        <span class="d-flex row align-center justify-center mx-auto">
          <v-icon
            class="mx-auto px-3 text-h3"
            color="primary"
          > mdi-silverware-fork-knife </v-icon>
          <v-card-text
            class="mx-auto px-3  align-center text-h4"
          > {{ recipe.servings }} </v-card-text>
        </span>

        <span class="d-flex row align-center justify-center mx-auto">
          <v-icon
            class="mx-auto px-3 text-h3"
            color="primary"
          > mdi-chef-hat </v-icon>
          <v-card-text
            class="mx-auto px-3  align-center text-h4"
          > {{ recipe.preparationTime }} min </v-card-text>
        </span>

        <span class="d-flex row align-center justify-center mx-auto">
          <v-icon
            class="mx-auto px-3 text-h3"
            color="primary"
          > mdi-stove </v-icon>
          <v-card-text
            class="mx-auto px-3  align-center text-h4"
          > {{ recipe.cookingTime }} min </v-card-text>
        </span>
      </span>

      <h2 class="my-3" >Ingredients</h2>

      <span v-for="(ingredient, index) in recipe.ingredients">
            <v-card-text
              outlined
              class="flex-grow-1"
              color="primary"
            > {{ ingredient.amount }} {{ ingredient.name }} </v-card-text>
      </span>


      <h2 class="my-3" >Steps</h2>

      <span v-for="(step, index) in recipe.steps">
            <!-- Editable text field -->
            <v-card-text
              :label="`Step ${index + 1}`"
              outlined
              class="flex-grow-1"
              color="primary"
            >{{index + 1}} - {{step}}</v-card-text>
      </span>

      <span class="d-flex align-center justify-center mb-2 mt-16 ga-16" v-if="isOwner" >
        <v-btn
          @click="remove(recipeId)"
          prepend-icon="mdi-delete"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="300px"
        >Delete</v-btn>
        <v-btn
          prepend-icon="mdi-pencil"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="300px"
          @click="toEditRecipe(recipeId)"
        >Edit</v-btn>
      </span>

  </v-card>

</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router';
import {deleteRecipe, getRecipe} from "@/scripts/recipes";
import {ref} from "vue";
import {base_url, toEditRecipe, toListRecipe} from "@/scripts/common";

// Get the route object
const route = useRoute();
const recipeId = route.query.id
let displayError = ref<Boolean>(false)
const errorMessage = ref<String>("This recipe does not exist")
const isOwner = true

const imageUrl = computed(() => `${base_url}/image/recipes/${recipeId}.webp`);

const recipe = ref<object>({
  steps: [''],
  ingredients: [],
})
/*
getRecipe(recipeId).then (
  function (response) {
    recipe.value.title = response.data.title
    recipe.value.description = response.data.description
    recipe.value.servings = response.data.portions
    recipe.value.ingredients = response.data.ingredients
    recipe.value.steps = response.data.steps
    console.log(recipe.true)
  }).catch(function (error) {
    displayError.value = true
  console.log(error);
    console.log(displayError)
}).finally(function () {
  // always executed
});

 */

const remove = (id) => {
  deleteRecipe(id)
  toListRecipe()
}

</script>
