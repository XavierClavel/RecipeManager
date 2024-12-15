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
        <picto-info :value="recipe.yield" icon="mdi-silverware-fork-knife"></picto-info>
        <picto-info :value="recipe.conservationTime" icon="mdi-fridge" v-if="recipe.conservationTime"></picto-info>
        <picto-info :value="`${recipe.preparationTime} min`" icon="mdi-chef-hat" v-if="recipe.preparationTime"></picto-info>
        <picto-info :value="`${recipe.cookingTime} min`" icon="mdi-stove" v-if="recipe.cookingTime"></picto-info>
        <picto-info :value="`${recipe.cookingTemperature} °C`" icon="mdi-thermometer" v-if="recipe.cookingTemperature"></picto-info>
      </span>


    <span class="d-flex align-center justify-center mb-2 my-5">
      <v-btn
        v-if="recipeLiked != null"
        :icon="recipeLiked ? 'mdi-heart' : 'mdi-heart-outline' "
        color="primary"
        flat
        rounded="circle"
        class="mb-10 text-h6"
        min-height="70px"
        min-width="70px"
        @click="onLikeButtonClick"
        variant="outlined"
      ></v-btn>
    </span>

      <h2 class="my-3" >Ingredients</h2>

    <div v-for="(ingredient, index) in recipe.ingredients" :key="index">
      <div class="d-flex align-center">
        <v-icon>mdi-circle-small</v-icon>
        <span
          class="p-0 mr-2 text-start text-h5 text-primary"
          style="width: auto; min-width: 0;"
        >
          {{ ingredient.amount }}
        </span>
        <span
          class="p-0 mr-2 text-start text-h5 text-primary"
          style="width: auto; min-width: 0;"
        >
          {{ unitToReadable(ingredient.unit) }}
        </span>
        <span
          class="p-0 text-start"
        >
          {{ ingredient.name }}
        </span>
      </div>
    </div>

    <div v-for="(ingredient, index) in recipe.customIngredients" :key="index">
      <div class="d-flex align-center">
        <v-icon>mdi-circle-small</v-icon>
        <span
          class="p-0 text-start text-h5 text-primary"
          style="width: auto; min-width: 0;"
        >
          {{ ingredient.amount }}
        </span>
        <span
          class="p-0 mr-2 text-start text-h5 text-primary"
          style="width: auto; min-width: 0;"
        >
          {{ unitToReadable(ingredient.unit) }}
        </span>
        <span
          class="p-0 text-start"
        >
          {{ ingredient.name }}
        </span>
      </div>
    </div>

      <v-container v-if="recipe.steps.length > 0">

      <h2 class="my-3" >Steps</h2>

      <span v-for="(step, index) in recipe.steps">
        <div class="d-flex align-center">
          <span
            class="p-0 mr-2 text-h5 text-primary"
            style="width: auto; min-width: 50px;"
          >
            {{index + 1}} -
          </span>
          <span
            class="p-0 text-h7"
            style="width: auto; min-width: 0;"
          >
            {{step}}
          </span>
        </div>
      </span>

      </v-container>

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
import {base_url, toEditRecipe, toListRecipe, unitToReadable} from "@/scripts/common";
import {useAuthStore} from "@/stores/auth";
import {addLike, isLiked, removeLike} from "@/scripts/likes";
//import PictoInfo from "@/components/RecipeInfo.vue";

// Get the route object
const route = useRoute();
const recipeId = route.query.id
let displayError = ref<boolean>(false)
const errorMessage = ref<string>("This recipe does not exist")
const isOwner = ref<boolean>(false)
const recipeLiked = ref<string>(null)

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

isLiked(recipeId).then (
  function (response) {
    recipeLiked.value = response
    console.log(response)
  }
)


const remove = (id) => {
  deleteRecipe(id)
  toListRecipe()
}

const onLikeButtonClick = () => {
  if (recipeLiked.value) {
    removeLike(recipeId)
  } else {
    addLike(recipeId)
  }
  recipeLiked.value = !recipeLiked.value
}

</script>
