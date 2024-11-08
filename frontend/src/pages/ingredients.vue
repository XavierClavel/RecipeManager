<template>
  <v-layout class="rounded rounded-md">

    <v-main class="d-flex align-center justify-center" style="min-height: 300px;" >
      <v-card width="100%" max-width="1500px"  class="ma-5" rounded="xl">
        <v-card-title class="text-primary">
          Ingredients
        </v-card-title>
        <v-btn prepend-icon="mdi-plus-box-outline" color="primary" rounded="lg" flat class="ml-8" @click="newIngredient">New ingredient</v-btn>
        <v-slide-x-reverse-transition v-show="isPanelOpen">
          <v-sheet class="pa-4 right-panel" elevation="3" z-index="50" style='z-index:20001;'>
            <v-btn icon @click="isPanelOpen = false">
              <v-icon>mdi-close</v-icon>
            </v-btn>
            <v-card-title
              class="text-primary"
            >
              {{ action }}
            </v-card-title>
            <v-text-field
              v-model="ingredient.name"
              label="Name"
              class="mx-auto px-3"
              color="primary"
            ></v-text-field>
            <v-select
              v-model="ingredient.type"
              label="Type"
              class="mx-auto px-3"
              color="primary"
              :items="ingredientTypes"
            ></v-select>
            <v-number-input
              v-model="ingredient.calories"
              label="Calories"
              class="mx-auto px-3"
              type="number"
              color="primary"
              :min="0"
              :step="10"
            ></v-number-input>
            <span class="d-flex flex-row align-center justify-center mb-2 mt-16 ga-16" >
        <v-btn
          icon="mdi-close-circle-outline"
          color="primary"
          flat
          rounded="lg"
          class="mb-10 text-h6"
          min-height="70px"
          min-width="70px"
          @click="cancel"
        ></v-btn>
        <v-btn
          v-if="ingredient.id != undefined"
          icon="mdi-delete"
          color="primary"
          flat
          rounded="lg"
          class="mb-10 text-h6"
          min-height="70px"
          min-width="70px"
          @click="performDelete(ingredient.id)"
        ></v-btn>
        <v-btn
          @click="save"
          icon="mdi-content-save"
          color="primary"
          flat
          rounded="lg"
          class="mb-10 text-h6"
          min-height="70px"
          min-width="70px"
        ></v-btn>
      </span>
          </v-sheet>
        </v-slide-x-reverse-transition>

        <v-table
            min-height="300px"
            fixed-header
          >
            <thead>
            <tr>
              <th class="text-left">
                Ingredient
              </th>
              <th class="text-left">
                Type
              </th>
              <th class="text-left">
                Calories
              </th>
            </tr>
            </thead>
            <tbody>
            <tr
              v-for="ingredient in ingredients"
              :key="ingredient.username"
              @click="editIngredient(ingredient)"
            >
              <td>{{ ingredient.name }}</td>
              <td>{{ ingredient.type }}</td>
              <td>{{ ingredient.calories }}</td>
            </tr>
            </tbody>
          </v-table>
        <v-container>
          <v-row justify="center">
            <v-col cols="6">
              <v-container class="max-width">
                <v-pagination
                  v-model="page"
                  :length="pagesCount"
                  class="my-4"
                  active-color="primary"
                  @update:modelValue="updateDisplay"
                ></v-pagination>
              </v-container>
            </v-col>
          </v-row>
        </v-container>
      </v-card>

    </v-main>
  </v-layout>
</template>

<script lang="ts" setup>
import {createIngredient, deleteIngredient, getCount, searchIngredients, updateIngredient} from "@/scripts/ingredients";
import {toViewRecipe} from "@/scripts/common";

const ingredients = ref<object[]>([])
const ingredient = ref({})
const page = ref<number>(1)
const pagesCount = ref<number>(1)
const isPanelOpen = ref<boolean>(false)
const ingredientTypes = [
  "CHEESE",
  "VEGETABLE",
  "MEAT",

]
const action = ref<string>("Create ingredient")

async function save() {
  const { id, ...ingredientToUpdate} = ingredient.value
  if (ingredient.value.id == undefined) {
    await createIngredient(ingredientToUpdate)
  } else {
    await updateIngredient(ingredient.value.id, ingredientToUpdate)
  }
  updateDisplay()
}

const cancel = () => {
  ingredient.value = {}
  isPanelOpen.value = false
}

const newIngredient = () => {
  isPanelOpen.value = true
  ingredient.value = {}
  action.value = "Create ingredient"
}

const editIngredient = (ingredientToEdit) => {
  if (ingredient.value.id == ingredientToEdit.id) {
    cancel()
  } else {
    isPanelOpen.value = true
    ingredient.value = ingredientToEdit
    action.value = `Edit ingredient "${ingredientToEdit.name}"`
  }

}

const performDelete = (id) => {
  deleteIngredient(id).then(
    function (response) {
      if (response.status == 200) {
        const index = ingredients.value.findIndex((user) => user.id == id)
        ingredients.value.splice(index,1)
      }
    }).catch(function (error) {
    console.log(error);
  }).finally(function () {
    // always executed
  });

};

const updateDisplay = () => {
  searchIngredients("",page.value - 1, 20).then (
    function (response) {
      ingredients.value = response.data
    }).catch(function (error) {
    console.log(error);
  })
}

updateDisplay()


getCount().then (
  function (response) {
    pagesCount.value = Math.ceil(response.data / 20)
  }).catch(function (error) {
  console.log(error);
})


</script>

<style scoped>
/* Styling to make the overlay appear as a fixed panel on the right */
.right-panel {
  position: fixed;
  top: 0;
  right: 0;         /* Sticks the panel to the right side */
  height: 100vh;    /* Full vertical height */
  width: 400px;
  overflow-y: auto; /* Enables scrolling within the panel if content is long */
}
</style>
