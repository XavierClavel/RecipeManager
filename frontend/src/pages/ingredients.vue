<template>
  <v-layout class="rounded rounded-md">

    <v-main class="d-flex align-center justify-center" style="min-height: 300px;" >
      <v-card width="100%" max-width="1500px"  class="ma-5" rounded="xl">
        <v-card-title class="text-primary">
          Ingredients
        </v-card-title>
        <v-btn prepend-icon="mdi-plus-box-outline" color="primary" rounded="lg" flat class="ml-8">New ingredient</v-btn>

        <v-container>
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
          <span class="d-flex align-center justify-center mb-2 mt-16 ga-16" >
        <v-btn
          prepend-icon="mdi-close-circle-outline"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="200px"
          @click="toViewRecipe(recipeId)"
        >Cancel</v-btn>
        <v-btn
          prepend-icon="mdi-delete"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="200px"
          @click="toViewRecipe(recipeId)"
        >Delete</v-btn>
        <v-btn
          @click="submit"
          prepend-icon="mdi-content-save"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="200px"
        >Save</v-btn>
      </span>
        </v-container>

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
              <th class="text-left">
                Actions
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
              <td class="y-5">
                <v-btn icon="mdi-pencil" color="primary" rounded="e" flat class="mr-1"></v-btn>
                <v-btn icon="mdi-delete" color="primary" rounded="s" flat @click="performDelete(ingredient.id)"></v-btn>
              </td>
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
import {deleteIngredient, getCount, searchIngredients} from "@/scripts/ingredients";
import {toViewRecipe} from "@/scripts/common";

const ingredients = ref<object[]>([])
const ingredient = ref({})
const page = ref<number>(1)
const pagesCount = ref<number>(1)
const ingredientTypes = [
  "CHEESE",
  "VEGETABLE",
  "MEAT",

]
const action = ref<string>("Create ingredient")

const save = () => {
  if (ingredient.value.id == undefined) {

  } else {

  }
}

const editIngredient = (ingredientToEdit) => {
  if (ingredient.value.id == ingredientToEdit.id) {
    ingredient.value = {}
    action.value = "Create ingredient"
  } else {
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
  console.log(page)
  searchIngredients("",page.value - 1, 20).then (
    function (response) {
      console.log(response)
      ingredients.value = response.data
    }).catch(function (error) {
    console.log(error);
  })
}

updateDisplay()


getCount().then (
  function (response) {
    console.log(response)
    pagesCount.value = Math.ceil(response.data / 20)
    console.log(pagesCount.value)
  }).catch(function (error) {
  console.log(error);
})


</script>
