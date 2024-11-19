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
      <v-file-input
        @change="onImageUpload"
        v-model="image"
        style="display: none"
        ref="fileInput"
        accept="image/*"
      >

      </v-file-input>

      <v-img :src="imageUrl"
             @load="handleImageLoad"
             @error="handleImageError"
             cover
             color="surface-variant"
             height="562px"
             width="1000px"
             class="preview-image my-6"
             rounded="lg"
             fill-height
             fluid
             @click="triggerFileInput"
      >
        <v-container class="image-overlay d-flex flex-row ga-16"  >
          <v-btn
            width="100px"
            height="100px"
            rounded="xl"
            color="primary"
            icon="mdi-pencil"
            class="text-h4 overlay-button"
            hint="hihi"
          ></v-btn>
          <v-btn
            width="100px"
            height="100px"
            rounded="xl"
            color="primary"
            icon="mdi-arrow-u-left-top"
            class="text-h4 overlay-button"
            v-if="imageUpdated"
            @click.stop="undoImageChange"
          ></v-btn>
          <v-btn
            width="100px"
            height="100px"
            rounded="xl"
            color="primary"
            icon="mdi-close"
            class="text-h4 overlay-button"
            v-if="recipeHasImage && !imageDeleted"
            @click.stop="deleteImage"
          ></v-btn>
        </v-container>

      </v-img>

      <v-number-input
        v-model="recipe.servings"
        label="Yield"
        class="mx-auto px-3"
        type="number"
        color="primary"
        min="1"
      ></v-number-input>

      <v-number-input
        v-model="recipe.preparationTime"
        label="Preparation Time (minutes)"
        class="mx-auto px-3"
        type="number"
        color="primary"
        :step="5"
        :min="0"
      ></v-number-input>

      <v-number-input
        v-model="recipe.cookingTime"
        label="Cooking time (minutes)"
        class="mx-auto px-3"
        type="number"
        color="primary"
        :step="5"
        :min="0"
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
              v-model="recipe.ingredients[index].id"
              :label="`Ingredient ${index + 1}`"
              class="flex-grow-1"
              color="primary"
              :items="autocompleteList[index]"
              item-color="primary"
              item-title="name"
              item-value="id"
              @update:search="(query) => onIngredientAutocompleteChange(query, index)"
              :key="index"
            ></v-autocomplete>

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

      <!-- Custom ingredients -->
      <h2 class="my-3" v-if="recipe.customIngredients.length > 0">Custom ingredients</h2>
      <draggable v-model="recipe.customIngredients" tag="div2" ghost-class="ghost" item-key="index" handle=".drag-handle">
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
              :items="['unit','g','lb','teaspoon', 'sugarspoon', 'cup']"
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
              min="0"
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
import {base_url, toViewRecipe} from "@/scripts/common";
import {searchIngredients} from "@/scripts/ingredients";

const fileInput = ref(null)
const imageUpdated = ref<Boolean>(false)
const imageDeleted = ref<Boolean>(false)
const recipeHasImage = ref<Boolean>(false)

// Get the route object
const route = useRoute();
let recipeId = route.query.id

const image = ref<File | null>(null)
const baseImageUrl = `${base_url}/image/recipes/${recipeId}.webp`
const imageUrl = ref<string>(baseImageUrl)

const autocompleteList = ref([])

const onIngredientAutocompleteChange = async (query, index) => {
  const response = await searchIngredients(query, 0, 20);
  autocompleteList.value[index] = response.data.map(item => ({ id: item.id, name: item.name }));
}




const recipe = ref<object>({
  description: "",
  steps: [''],
  ingredients: [],
  customIngredients: [],
})

// Trigger the v-file-input click
function triggerFileInput() {
  if (fileInput.value) {
    fileInput.value.click();
  }
}

const onImageUpload = () => {
  imageUrl.value = URL.createObjectURL(image.value)
  imageUpdated.value = true
}

function undoImageChange() {
  imageUpdated.value = false
  imageDeleted.value = false
  imageUrl.value = `${base_url}/image/recipes/${recipeId}.webp`
}

function deleteImage() {
  imageDeleted.value = recipeHasImage.value
  imageUpdated.value = recipeHasImage.value

  imageUrl.value = `${base_url}/image/recipes/default.webp`
}

function handleImageLoad(url) {
  // Only consider the primary image for setting imageExists
  if (url === baseImageUrl.value) {
    console.log("image success")
    recipeHasImage.value = true
  }
}

function handleImageError(url) {
  // If primary image fails, set imageExists to false and hide the button
  if (url === baseImageUrl.value) {
    console.log("image error")
    recipeHasImage.value = false
  }
}

// Function to add a new item
const addStep = () => {
  console.log(recipe.value)
  recipe.value.steps.push('');
};

const addIngredient = () => {
  recipe.value.ingredients.push({});
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
  if (recipeId == null) {
    const response = await createRecipe(recipe.value)
    recipeId = response.data.id
  } else {
    await updateRecipe(recipeId, recipe.value)
  }

  if (imageDeleted.value) {
    await deleteRecipeImage(recipeId)
  } else if (imageUpdated.value) {
    await uploadRecipeImage(recipeId, image.value)
  }
  toViewRecipe(recipeId)

}

if (recipeId != undefined) {
  getRecipe(recipeId).then (
    function (response) {
      recipe.value = response.data
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

.preview-image {
  cursor: pointer;
}

.drag-handle {
  cursor: grab;
  display:flex;
  align-items: center;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0);
  transition: background-color 0.3s ease;
}

.preview-image:hover .image-overlay {
  background-color: rgba(255, 255, 255, 0.3); /* Adjust opacity for desired whitening effect */
}

.overlay-button {
  opacity: 0;
  transition: opacity 0.3s ease;
}

.preview-image:hover .overlay-button {
  opacity: 1; /* Button becomes visible on hover */
}

</style>
