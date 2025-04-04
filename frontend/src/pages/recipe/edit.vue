<template>
  <v-card
  class="mx-auto pa-5 ma-auto my-5"
  max-width="1000px"
  style="border: 4px solid #0d1821 !important;"
  >


    <form @submit.prevent="submit" class="mx-auto">
      <v-card class="my-2">
      <v-text-field
        v-model="recipe.title"
        :label="`${$t('title')}`"
        class="mx-auto"
        color="primary"
        bg-color="background"
        :rules="[rules.max100]"
        variant="solo"
        flat
        single-line
        hide-details
      ></v-text-field>
      </v-card>

      <v-card class="my-2">
      <v-textarea
        v-model="recipe.description"
        :label="`${$t('description')}`"
        class="mx-auto"
        color="primary"
        :rules="[rules.max]"
        variant="solo"
        flat
        hide-details
        bg-color="background"
      ></v-textarea>
      </v-card>

      <v-col class="py-2" cols="12">

        <v-btn-toggle
          v-model="recipe.dishClass"
          color="primary"
          bg-color="background"
          base-color="background"
          rounded="lg"
          group
          mandatory
          class="ga-2"
        >
          <v-btn value="ENTREE">{{$t("entree")}}</v-btn>

          <v-btn value="MAIN_DISH">{{$t("plat")}}</v-btn>

          <v-btn value="DESERT">{{$t("desert")}}</v-btn>

          <v-btn value="OTHER">{{$t("other")}}</v-btn>
        </v-btn-toggle>
      </v-col>

       <editable-picture
         path="image/recipes"
         :id="recipeId"
         ref="editablePicture"
         width="800px"
         height="600px"
       ></editable-picture>


      <v-card class="my-2">
      <v-number-input
        v-model="recipe.yield"
        :label="`${$t('yield')}`"
        type="number"
        color="primary"
        :min=1
        variant="solo"
        flat
        single-line
        hide-details
        bg-color="background"
      ></v-number-input>
      </v-card>

      <v-card class="my-2">
      <v-number-input
        v-model="recipe.preparationTime"
        :label="`${$t('time_preparation')}`"
        type="number"
        color="primary"
        :step="5"
        :min=0
        variant="solo"
        flat
        single-line
        hide-details
        bg-color="background"
      ></v-number-input>
      </v-card>

      <v-card class="my-2">
      <v-number-input
        v-model="recipe.cookingTime"
        :label="`${$t('time_cooking')}`"
        type="number"
        color="primary"
        :step="5"
        :min=0
        variant="solo"
        flat
        single-line
        hide-details
        bg-color="background"
      ></v-number-input>
      </v-card>

      <v-card class="my-2">
      <v-number-input
        v-model="recipe.cookingTemperature"
        :label="`${$t('cooking_temperature')}`"
        type="number"
        color="primary"
        :step="5"
        :min=0
        variant="solo"
        flat
        single-line
        hide-details
        bg-color="background"
      >
      </v-number-input>
      </v-card>

      <!-- Ingredients -->
      <h2 class="my-3 mt-12" >{{$t("ingredients")}}</h2>
      <draggable v-model="recipe.ingredients" tag="div" ghost-class="ghost" item-key="index" handle=".drag-handle">
        <template #item="{ element, index }">
          <div class="d-flex align-center mb-2">
            <!-- Add a handle for dragging -->
            <v-icon
              class="mr-2 drag-handle"
              color="black"
              small
            >mdi-drag</v-icon>

            <v-card class="ma-1 flex-grow-1">
            <v-autocomplete
              v-model="recipe.ingredients[index].ingredient"
              :label="`${$t('ingredient')} ${index + 1}`"
              color="primary"
              :items="autocompleteList[index]"
              item-color="primary"
              item-title="name"
              item-value="id"
              @update:search="(query) => onIngredientAutocompleteChange(query, index)"
              :key="index"
              return-object
              variant="solo"
              flat
              single-line
              hide-details
              bg-color="background"
            ></v-autocomplete>
            </v-card>

            <v-card class="ma-1 flex-grow-1" max-width="200px">
            <v-select
              v-model="recipe.ingredients[index].unit"
              :label="`${$t('unit')}`"
              :items="units"
              color="primary"
              item-title="unit"
              return-object
              variant="solo"
              flat
              single-line
              hide-details
              bg-color="background"
            ></v-select>
            </v-card>

            <v-card class="ma-1 flex-grow-1" max-width="150px">
            <v-number-input
              v-model="recipe.ingredients[index].amount"
              :label="`${$t('amount')}`"
              type="number"
              color="primary"
              control-variant="stacked"
              min=0
              item-title="amount"
              return-object
              variant="solo"
              flat
              single-line
              hide-details
              bg-color="background"
            ></v-number-input>
            </v-card>

            <v-card class="ma-1 flex-grow-1">
            <v-text-field
              v-model="recipe.ingredients[index].complement"
              :label="`${$t('complement')}`"
              variant="solo"
              flat
              single-line
              hide-details
              bg-color="background"
            ></v-text-field>
            </v-card>

            <div>
              <v-btn
                @click="removeIngredient(index)"
                icon="mdi-delete"
                color="primary"
                class="ml-4"
              ></v-btn>
            </div>


          </div>
        </template>
      </draggable>

      <!-- Custom ingredients -->
      <h2 class="my-3 mt-12" v-if="recipe.customIngredients && recipe.customIngredients.length > 0">Custom ingredients</h2>
      <draggable v-model="recipe.customIngredients" ghost-class="ghost" item-key="index" handle=".drag-handle">
        <template #item="{ element, index }">
          <div class="d-flex align-center mb-2">
            <!-- Add a handle for dragging -->
            <v-icon
              class="mr-2 drag-handle"
              color="black"
              small
            >mdi-drag</v-icon>

            <v-card class="flex-grow-1 ma-1">
            <v-text-field
              v-model="recipe.customIngredients[index].name"
              :label="`${$t('custom_ingredient')} ${index + 1}`"
              color="primary"
              item-color="primary"
              :key="index"
              :rules="[rules.max50]"
              variant="solo"
              flat
              single-line
              hide-details
              bg-color="background"
            ></v-text-field>
            </v-card>

            <v-card class="flex-grow-1 ma-1" max-width="200px">
            <v-select
              v-model="recipe.customIngredients[index].unit"
              :label="`${$t('unit')}`"
              outlined
              :items="units"
              color="primary"
              variant="solo"
              flat
              single-line
              hide-details
              bg-color="background"
            ></v-select>
            </v-card>

            <v-card class="flex-grow-1 ma-1" max-width="150px">
            <v-number-input
              v-model="recipe.customIngredients[index].amount"
              :label="`${$t('amount')}`"
              type="number"
              color="primary"
              control-variant="stacked"
              min=0
              variant="solo"
              flat
              single-line
              hide-details
              bg-color="background"
            >
            </v-number-input>
            </v-card>

            <div>
              <v-btn
                @click="removeCustomIngredient(index)"
                icon="mdi-delete"
                color="primary"
                class="ml-4"
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
        class="mb-10"
      >{{$t("ingredients_add_new")}}</v-btn>

      <!-- Button to add custom ingredient -->
      <v-tooltip text="Custom ingredients should only be used if an ingredient you want to add is not registered in the database."
      location="bottom">
        <template v-slot:activator="{ props }">
          <v-btn
            v-bind="props"
            @click="addCustomIngredient"
            prepend-icon="mdi-plus-circle-outline"
            color="black"
            flat
            class="mb-10 ml-8"
            variant="outlined"
          >{{$t("ingredients_add_custom")}}</v-btn>
        </template>
      </v-tooltip>


      <!-- Steps -->
      <h2 class="my-3" >{{$t("steps")}}</h2>
      <draggable v-model="recipe.steps" tag="div" ghost-class="ghost" item-key="index" handle=".drag-handle">
        <template #item="{ element, index }">
          <div class="d-flex align-center mb-2">
            <!-- Add a handle for dragging -->
            <v-icon
              class="mr-2 drag-handle"
              color="black"
              small
            >mdi-drag</v-icon>

            <!-- Editable text field -->
            <v-card class="my-2 flex-grow-1">
            <v-text-field
              v-model="recipe.steps[index]"
              :label="`${$t('step')} ${index + 1}`"
              color="primary"
              :id="`step_${index}`"
              :rules="[rules.max]"
              @keyup.enter="addStepAt(index)"
              @keyup.delete="deleteStepAt(index)"
              variant="solo"
              flat
              single-line
              hide-details
              bg-color="background"
            ></v-text-field>
            </v-card>

            <v-btn
              @click="removeStep(index)"
              icon="mdi-delete"
              color="primary"
              class="ml-4"
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
        class="mb-10"
      >{{$t("steps_add_new")}}</v-btn>

      <span class="d-flex align-center justify-center mb-2 mt-16 ga-16" >
        <action-button
          icon="mdi-close-circle-outline"
          :text="`${$t('cancel')}`"
          :action="() => toViewRecipe(recipeId)"
        ></action-button>
        <action-button
          icon="mdi-content-save"
          :text="`${$t('save')}`"
          :action="submit"
        ></action-button>
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

const rules = {
  required: value => !!value || 'Required.',
  max: v => v.length <= 200 || 'Max 200 characters',
  max100: v => v.length <= 100 || 'Max 100 characters',
  max50: v => v.length <= 50 || 'Max 50 characters',
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
    unit: item.unit,
    complement: item.complement}))
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
