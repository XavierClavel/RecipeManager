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

      <v-col class="py-2" cols="12">

        <v-btn-toggle
          v-model="recipe.dishClass"
          color="primary"
          rounded="xl"
          group
          mandatory
        >
          <v-btn value="ENTREE"> Entree </v-btn>

          <v-btn value="MAIN_DISH"> Main dish </v-btn>

          <v-btn value="DESERT"> Desert </v-btn>

          <v-btn value="OTHER"> Other </v-btn>
        </v-btn-toggle>
      </v-col>

       <editable-picture path="image/recipes" :id="recipeId" ref="editablePicture"></editable-picture>


      <v-number-input
        v-model="recipe.yield"
        label="Yield"
        class="mx-auto px-3"
        type="number"
        color="primary"
        :min=1
      ></v-number-input>

      <v-number-input
        v-model="recipe.conservationTime"
        label="Conservation time (days)"
        class="mx-auto px-3"
        type="number"
        color="primary"
        :min=1
      ></v-number-input>

      <v-number-input
        v-model="recipe.preparationTime"
        label="Preparation Time (minutes)"
        class="mx-auto px-3"
        type="number"
        color="primary"
        :step="5"
        :min=0
      ></v-number-input>

      <v-number-input
        v-model="recipe.cookingTime"
        label="Cooking time (minutes)"
        class="mx-auto px-3"
        type="number"
        color="primary"
        :step="5"
        :min=0
      ></v-number-input>

      <v-number-input
        v-model="recipe.cookingTemperature"
        label="Cooking temperature (°C)"
        class="mx-auto px-3"
        type="number"
        color="primary"
        :step="5"
        :min=0
      ></v-number-input>

      <!-- Ingredients -->
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

            <v-autocomplete
              v-model="recipe.ingredients[index].ingredient"
              :label="`Ingredient ${index + 1}`"
              class="flex-grow-1"
              color="primary"
              :items="autocompleteList[index]"
              item-color="primary"
              item-title="name"
              item-value="id"
              @update:search="(query) => onIngredientAutocompleteChange(query, index)"
              :key="index"
              return-object
            ></v-autocomplete>

            <v-select
              v-model="recipe.ingredients[index].unit"
              label="Unit"
              outlined
              class="flex-grow-1 mx-2"
              :items="units"
              variant="outlined"
              max-width="200px"
              color="primary"
              item-title="unit"
              return-object
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
              min=0
              item-title="amount"
              return-object
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

      <!-- Custom ingredients -->
      <h2 class="my-3" v-if="recipe.customIngredients && recipe.customIngredients.length > 0">Custom ingredients</h2>
      <draggable v-model="recipe.customIngredients" ghost-class="ghost" item-key="index" handle=".drag-handle">
        <template #item="{ element, index }">
          <div class="d-flex align-center mb-2">
            <!-- Add a handle for dragging -->
            <v-icon
              class="mr-2 drag-handle mb-5"
              color="primary"
              small
            >mdi-drag</v-icon>

            <v-text-field
              v-model="recipe.customIngredients[index].name"
              :label="`Custom ingredient ${index + 1}`"
              class="flex-grow-1"
              color="primary"
              item-color="primary"
              :key="index"
            ></v-text-field>

            <v-select
              v-model="recipe.customIngredients[index].unit"
              label="Unit"
              outlined
              class="flex-grow-1 mx-2"
              :items="units"
              variant="outlined"
              max-width="200px"
              color="primary"
            ></v-select>

            <v-number-input
              v-model="recipe.customIngredients[index].amount"
              label="Amount"
              outlined
              class="flex-grow-1"
              type="number"
              max-width="150px"
              color="primary"
              control-variant="stacked"
              min=0
            ></v-number-input>

            <div>
              <v-btn
                @click="removeCustomIngredient(index)"
                icon="mdi-delete"
                color="primary"
                class="rounded-lg ml-4 mb-5"
              ></v-btn>
            </div>


          </div>
        </template>
      </draggable>

      <!-- Button to add ingredient -->
      <v-btn
        @click="addIngredient"
        prepend-icon="mdi-plus-circle-outline"
        color="primary"
        flat
        rounded
        class="mb-10"
      >Add new ingredient</v-btn>

      <!-- Button to add custom ingredient -->
      <v-tooltip text="Custom ingredients should only be used if an ingredient you want to add is not registered in the database."
      location="bottom">
        <template v-slot:activator="{ props }">
          <v-btn
            v-bind="props"
            @click="addCustomIngredient"
            prepend-icon="mdi-plus-circle-outline"
            color="primary"
            flat
            rounded
            class="mb-10 ml-8"
            variant="outlined"
          >Add custom ingredient</v-btn>
        </template>
      </v-tooltip>


      <!-- Steps -->
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
              :id="`step_${index}`"
              @keyup.enter="addStepAt(index)"
              @keyup.delete="deleteStepAt(index)"
            ></v-text-field>

            <v-btn
              @click="removeStep(index)"
              icon="mdi-delete"
              color="primary"
              class="rounded-lg ml-4 mb-5"
              tabindex="-1"
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
          v-if="recipeId != null"
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
          prepend-icon="mdi-content-save"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="300px"
        >Save</v-btn>
      </span>
    </form>
  </v-card>

</template>

<script lang="ts" setup>
import { ref } from 'vue';
import draggable from 'vuedraggable';
import { useRoute } from 'vue-router';
import {getRecipe, createRecipe, uploadRecipeImage, deleteRecipeImage, updateRecipe} from "@/scripts/recipes";
import {toViewRecipe} from "@/scripts/common";
import {searchIngredients} from "@/scripts/ingredients";
import EditablePicture from "@/components/EditablePicture.vue";



// Get the route object
const route = useRoute();
let recipeId = ref(route.query.id)
const editablePicture = ref(null)
const units = ref(['UNIT','GRAM','lb','teaspoon', 'sugarspoon', 'cup'])

const autocompleteList = ref([])

const onIngredientAutocompleteChange = async (query, index) => {
  const response = await searchIngredients(query, 0, 20);
  autocompleteList.value[index] = response.data.map(item => ({ id: item.id, name: item.name }));
}




const recipe = ref<object>({
  description: "",
  steps: [''],
  dishClass: "MAIN_DISH",
  ingredients: [],
  customIngredients: [],
})



// Function to add a new item
const addStep = () => {
  console.log(recipe.value)
  recipe.value.steps.push('');
};

const addIngredient = () => {
  recipe.value.ingredients.push({});
}

async function deleteStepAt(index) {
  console.log("called")
  console.log(recipe.value.steps[index])
  if (recipe.value.steps[index]) {
    return
  }
  recipe.value.steps.splice(index, 1);
  await nextTick()
  document.getElementById(`step_${index-1}`).focus()
}

async function addStepAt(index) {
  recipe.value.steps.splice(index+1, 0, "");
  await nextTick()
  document.getElementById(`step_${index + 1}`).focus()
}

const addCustomIngredient = () => {
  recipe.value.customIngredients.push({});
}

// Function to add a new item
const removeStep = (index) => {
  recipe.value.steps.splice(index,1);
};

const removeIngredient = (index) => {
  recipe.value.ingredients.splice(index,1);
}

const removeCustomIngredient = (index) => {
  recipe.value.customIngredients.splice(index,1);
}

async function submit() {
  console.log(recipe.value)
  const submitted = JSON.parse(JSON.stringify(recipe.value))
  submitted.ingredients = recipe.value.ingredients
    .filter((it) => it.ingredient )
    .map(item => ({
    id: item.ingredient.id,
    amount: item.amount,
    unit: item.unit }))
  submitted.customIngredients = submitted.customIngredients.filter((it) => "name" in it)
  submitted.steps = submitted.steps.filter((it) => it)
  console.log(submitted)
  if (recipeId.value == null) {
    const response = await createRecipe(submitted)
    recipeId.value = response.data.id
    await nextTick()
  } else {
    await updateRecipe(recipeId.value, submitted)
  }
  await editablePicture.value.submitImage()

  toViewRecipe(recipeId.value)
}

if (recipeId.value != null) {
  getRecipe(recipeId.value).then (
    function (response) {
      recipe.value.title = response.data.title
      recipe.value.description = response.data.description
      recipe.value.dishClass = response.data.dishClass
      recipe.value.steps = response.data.steps
      recipe.value.ingredients = response.data.ingredients.map(item => ({
        ingredient: {
          id: item.id,
          name: item.name
        },
        amount: item.amount,
        unit: item.unit
      }))
      recipe.value.customIngredients = response.data.customIngredients
      recipe.value.yield = response.data.yield
      recipe.value.conservationTime = response.data.conservationTime
      recipe.value.preparationTime = response.data.preparationTime
      recipe.value.cookingTime = response.data.cookingTime
      recipe.value.cookingTemperature = response.data.cookingTemperature
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
