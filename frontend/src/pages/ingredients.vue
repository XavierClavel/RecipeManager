<template>
  <v-layout class="rounded rounded-md">

    <v-main class="d-flex align-center justify-center" style="min-height: 300px;" >
      <v-card width="100%" max-width="1500px"  class="ma-5" rounded="xl">
        <v-card-title class="text-primary">
          Ingredients
        </v-card-title>
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
              <td class="y-5">
                <v-btn icon="mdi-pencil" color="primary" rounded="e" flat class="mr-1"></v-btn>
                <v-btn icon="mdi-delete" color="primary" rounded="s" flat @click="performDelete(ingredient.id)"></v-btn>
              </td>
            </tr>
            </tbody>
          </v-table>
      </v-card>

    </v-main>
  </v-layout>
</template>

<script lang="ts" setup>
import {deleteIngredient, getIngredients} from "@/scripts/ingredients";

let ingredients = ref<object[]>([])

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

getIngredients().then (
  function (response) {
    console.log(response)
    ingredients.value = response.data
}).catch(function (error) {
  console.log(error);
}).finally(function () {
    // always executed
  });

</script>
