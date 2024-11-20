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

    <v-card-text
      class="mx-auto px-3"
    > Created on : {{new Date(recipe.creationDate * 1000).toLocaleDateString()}} </v-card-text>

    <v-card-text
      class="mx-auto px-3"
    > Last edited on : {{new Date(recipe.editionDate * 1000).toLocaleDateString()}} </v-card-text>

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
        <picto-info :value="recipe.servings" icon="mdi-silverware-fork-knife"></picto-info>
        <picto-info :value="`${recipe.preparationTime} min`" icon="mdi-chef-hat"></picto-info>
        <picto-info :value="`${recipe.cookingTime} min`" icon="mdi-stove"></picto-info>
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

    <span class="d-flex align-center justify-center mb-2 ga-16">
        <v-btn
          prepend-icon="mdi-tray-arrow-down"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="300px"
          @click="downloadRecipe(recipeId)"
        >Download</v-btn>
      </span>



  </v-card>

</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router';
import {deleteRecipe, downloadRecipe, getRecipe} from "@/scripts/recipes";
import {ref} from "vue";
import {base_url, toEditRecipe, toListRecipe} from "@/scripts/common";
import {useAuthStore} from "@/stores/auth";
//import PictoInfo from "@/components/RecipeInfo.vue";

// Get the route object
const route = useRoute();
const recipeId = route.query.id
let displayError = ref<Boolean>(false)
const errorMessage = ref<String>("This recipe does not exist")
const isOwner = ref<Boolean>(false)

const imageUrl = computed(() => `${base_url}/image/recipes/${recipeId}.webp`);

const recipe = ref<object>({
  steps: [''],
  ingredients: [],
})

getRecipe(recipeId).then (
  function (response) {
    recipe.value = response.data
    console.log(recipe.value)
    const authStore = useAuthStore()
    isOwner.value = response.data.owner == authStore.username
    console.log(response.data.owner)
  }).catch(function (error) {
    displayError.value = true
  console.log(error);
    console.log(displayError)
}).finally(function () {
  // always executed
});



const remove = (id) => {
  deleteRecipe(id)
  toListRecipe()
}

</script>
