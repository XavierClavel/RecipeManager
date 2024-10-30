<template>
  <v-card
  class="mx-auto rounded-xl pa-5 ma-auto my-5"
  max-width="1000px"
  >


    <form @submit.prevent="submit" class="mx-auto">
      <v-text-field
        v-model="recipe.title"
        label="Title"
        class="mx-auto px-3"
        color="primary"
      ></v-text-field>

      <v-textarea
        v-model="recipe.description"
        label="Description"
        class="mx-auto px-3"
        color="primary"
      ></v-textarea>

      <v-number-input
        v-model="recipe.servings"
        label="Yield"
        class="mx-auto px-3"
        type="number"
        color="primary"
        min="1"
      ></v-number-input>

      <v-text-field
        v-model="recipe.preparationTime"
        label="Preparation Time (minutes)"
        class="mx-auto px-3"
        type="number"
        color="primary"
      ></v-text-field>

      <v-text-field
        v-model="recipe.cookingTime"
        label="Cooking time (minutes)"
        class="mx-auto px-3"
        type="number"
        color="primary"
      ></v-text-field>

      <h2 class="my-3" >Ingredients</h2>

      <draggable v-model="recipe.ingredients" tag="div" ghost-class="ghost" item-key="index" handle=".drag-handle">
        <template #item="{ element, index }">
          <div class="d-flex align-center mb-2">
            <!-- Add a handle for dragging -->
            <v-icon
              class="mr-2 drag-handle mb-5"
              color="primary"
              small
            >mdi-drag</v-icon>

            <v-text-field
              v-model="recipe.ingredients[index].name"
              :label="`Ingredient ${index + 1}`"
              outlined
              class="flex-grow-1"
              color="primary"
            ></v-text-field>

            <v-select
              v-model="recipe.ingredients[index].unit"
              label="Unit"
              outlined
              class="flex-grow-1 mx-2"
              :items="['unit','g','lb','teaspoon', 'sugarspoon', 'cup']"
              variant="outlined"
              max-width="200px"
              color="primary"
            ></v-select>

            <v-number-input
              v-model="recipe.ingredients[index].amount"
              label="Amount"
              outlined
              class="flex-grow-1"
              type="number"
              max-width="150px"
              color="primary"
              control-variant="stacked"
              min="0"
            ></v-number-input>

            <div>
              <v-btn
                @click="removeIngredient(index)"
                icon="mdi-delete"
                color="primary"
                class="rounded-lg ml-4 mb-5"
              ></v-btn>
            </div>


          </div>
        </template>
      </draggable>

      <!-- Button to add a new item -->
      <v-btn
        @click="addIngredient"
        prepend-icon="mdi-plus-circle-outline"
        color="primary"
        flat
        rounded
        class="mb-10"
      >Add new ingredient</v-btn>

      <h2 class="my-3" >Steps</h2>

      <draggable v-model="recipe.steps" tag="div" ghost-class="ghost" item-key="index" handle=".drag-handle">
        <template #item="{ element, index }">
          <div class="d-flex align-center mb-2">
            <!-- Add a handle for dragging -->
            <v-icon
              class="mr-2 drag-handle mb-5"
              color="primary"
              small
            >mdi-drag</v-icon>

            <!-- Editable text field -->
            <v-text-field
              v-model="recipe.steps[index]"
              :label="`Step ${index + 1}`"
              outlined
              class="flex-grow-1"
              color="primary"
            ></v-text-field>

            <v-btn
              @click="removeStep(index)"
              icon="mdi-delete"
              color="primary"
              class="rounded-lg ml-4 mb-5"
            ></v-btn>
          </div>
        </template>
      </draggable>

      <!-- Button to add a new item -->
      <v-btn
        @click="addStep"
        prepend-icon="mdi-plus-circle-outline"
        color="primary"
        flat
        rounded
        class="mb-10"
      >Add new step</v-btn>

      <span class="d-flex align-center justify-center mb-2 mt-16 ga-16" >
        <v-btn
          prepend-icon="mdi-close-circle-outline"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="300px"
          @click="toViewRecipe(recipeId)"
        >Cancel</v-btn>
        <v-btn
          @click="submit"
          prepend-icon="mdi-send"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="300px"
        >Validate</v-btn>
      </span>




    </form>
  </v-card>

</template>

<script lang="ts" setup>
import { ref } from 'vue';
import draggable from 'vuedraggable';
import { useRoute } from 'vue-router';
import {getRecipe, createRecipe} from "@/scripts/recipes";
import {toViewRecipe} from "@/scripts/common";

// Get the route object
const route = useRoute();
const recipeId = route.query.id

const recipe = ref<object>({
  steps: [''],
  ingredients: [],
})

// Function to add a new item
const addStep = () => {
  console.log(recipe.value)
  recipe.value.steps.push('');
};

const addIngredient = () => {
  recipe.value.ingredients.push({name:''});
}

// Function to add a new item
const removeStep = (index) => {
  recipe.value.steps.splice(index,1);
};

const removeIngredient = (index) => {
  recipe.ingredients.value.splice(index,1);
}

const submit = () => {
  console.log(recipe.value)
  //createRecipe(recipe)
}

if (recipeId != undefined) {
  getRecipe(recipeId).then (
    function (response) {
      recipe.value.title = response.data.title
      recipe.value.description = response.data.description
      recipe.value.servings = response.data.portions
      recipe.value.ingredients = response.data.ingredients
      recipe.value.steps = response.data.steps
      console.log(recipe.value)
    }).catch(function (error) {
    console.log(error);
  }).finally(function () {
    // always executed
  });
}



</script>

<style scoped>
.ghost {
  opacity: 0.5;
}

.drag-handle {
  cursor: grab;
  display:flex;
  align-items: center;
}
</style>
