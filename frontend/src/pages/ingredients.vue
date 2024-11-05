<template>
  <v-layout class="rounded rounded-md">

    <v-main class="d-flex align-center justify-center" style="min-height: 300px;" >
      <v-card width="100%" max-width="1500px"  class="ma-5" rounded="xl">
        <v-card-title class="text-primary">
          Ingredients
        </v-card-title>
        <v-btn prepend-icon="mdi-plus-box-outline" color="primary" rounded="lg" flat class="ml-8">New ingredient</v-btn>
        <v-btn
          color="surface-variant"
          text="Open Dialog"
          variant="flat"
          @click="isActive.value=true"
        ></v-btn>
        <v-dialog max-width="500">
          <template v-slot:activator="{ props: activatorProps }">
            <v-btn
              v-bind="activatorProps"
              color="surface-variant"
              text="Open Dialog"
              variant="flat"
            ></v-btn>
          </template>

          <template v-slot:default="{ isActive }">
            <v-card title="Dialog">
              <v-card-text>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
              </v-card-text>
r
              <v-card-actions>
                <v-spacer></v-spacer>

                <v-btn
                  text="Close Dialog"
                  @click="isActive.value = false"
                ></v-btn>
              </v-card-actions>
            </v-card>
          </template>
        </v-dialog>

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

const ingredients = ref<object[]>([])
const page = ref<number>(1)
const pagesCount = ref<number>(1)

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
