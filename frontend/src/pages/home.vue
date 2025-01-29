<template>
  <v-layout class="rounded rounded-md">
        <v-timeline class="ma-2" align="center" side="end">
          <v-timeline-item
            v-for="(timelineRecipe, date) in timelineRecipes"
            :key="date"
            size="large"
            side="end"
          >
          <template v-slot:icon>
            <v-avatar :image="getUserIconUrl(userId)"></v-avatar>
          </template>
          <template v-slot:opposite>
            <v-card color="surface">
              <v-card-text>{{date}}</v-card-text>
            </v-card>
          </template>
          <recipe :recipe="recipe" v-for="recipe in timelineRecipe"></recipe>
          </v-timeline-item>
        </v-timeline>
  </v-layout>
</template>

<script lang="ts" setup>

import {listRecipes} from "@/scripts/recipes";
import {useAuthStore} from "@/stores/auth";
import {getUserIconUrl} from "@/scripts/common";

const recipes = ref([])
const timelineRecipes = ref()
const authStore = useAuthStore()
const userId = authStore.id

listRecipes(`?sort=DATE_DESCENDING`).then(
  function (response) {
    console.log(response)
    recipes.value = response.data
    //noRecipes.value = recipes.value.length == 0

    timelineRecipes.value = recipes.value.reduce((acc, recipe) => {
      const groupKey = new Date(recipe.creationDate * 1000).toLocaleDateString()

      if (!acc[groupKey]) {
        acc[groupKey] = [];
      }

      acc[groupKey].push(recipe);
      return acc;
    }, {}); // Initialize with a Map
    console.log(timelineRecipes.value)
  }
)

</script>
