<template>
  <v-card
  class="mx-auto rounded-xl pa-5 ma-auto my-5"
  max-width="1000px"
  >
      <v-card-title
        class="mx-auto px-3 text-primary text-h3"
      >{{ recipe.title }}</v-card-title>

      <v-card-text
        class="mx-auto px-3"
      > {{recipe.description}} </v-card-text>

    <span class="d-flex my-10">
      <v-card color="#dddddd" width="100px" height="100px" rounded="xl" class="d-flex flex-column align-center justify-center mx-auto">
      <v-card-text
        class="mx-auto px-3 mb-n8 align-center text-h4"
      > {{ recipe.servings }} </v-card-text>
      <v-icon
        class="mx-auto px-3 mb-3"
        color="primary"
      > mdi-silverware-fork-knife </v-icon>
    </v-card>

    <v-card color="#dddddd" width="100px" height="100px" rounded="xl" class="d-flex flex-column align-center justify-center mx-auto">
      <v-card-text
        class="mx-auto px-3 mb-n8 align-center text-h5"
      > {{ recipe.preparationTime }} min </v-card-text>
      <v-icon
        class="mx-auto px-3 mb-3 text-h8"
        color="primary"
      > mdi-chef-hat </v-icon>
    </v-card>

      <v-card color="#dddddd" width="100px" height="100px" rounded="xl" class="d-flex flex-column align-center justify-center mx-auto">
      <v-card-text
        class="mx-auto px-3 mb-n8 align-center text-h5"
      > {{ recipe.cookingTime }} min </v-card-text>
      <v-icon
        class="mx-auto px-3 mb-3 text-h8"
        color="primary"
      > mdi-stove </v-icon>
    </v-card>
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
          @click="addStep"
          prepend-icon="mdi-delete"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="300px"
        >Delete</v-btn>
        <v-btn
          @click="submit"
          prepend-icon="mdi-pencil"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="300px"
        >Edit</v-btn>
      </span>

  </v-card>

</template>

<script lang="ts" setup>
import { ref } from 'vue';
import draggable from 'vuedraggable';

const isOwner = true

const title = ref<string>()
const description = ref<string>()
const servings = ref<number>()
const preparationTime = ref<number>()
const cookingTime = ref<number>()
const ingredients = ref<object[]>([
  {name:''}
]);
const steps = ref<string[]>(['']);

title.value = "aaaaaaaa"

// Function to add a new item
const addStep = () => {
  steps.value.push('');
};

const addIngredient = () => {
  ingredients.value.push({name:''});
}

// Function to add a new item
const removeStep = (index) => {
  steps.value.splice(index,1);
};

const removeIngredient = (index) => {
  ingredients.value.splice(index,1);
}

const recipe = {
  title: "My title",
  description: "My description",
  servings: 4,
  preparationTime: 20,
  cookingTime: 30,
  ingredients: [
    {
      name: "tomates",
      amount: 3,
    }
  ],
  steps: ["Couper", "cuire"],
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
