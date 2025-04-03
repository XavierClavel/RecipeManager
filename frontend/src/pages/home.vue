<template>
  <v-layout class="rounded rounded-md">
        <v-timeline class="ma-2" align="center" side="end" line-color="black" line-thickness="3px" density="comfortable" >
          <v-timeline-item
            v-for="(timelineRecipe, date) in timelineRecipes"
            :key="date"
            size="large"
            side="end"
            fill-dot
            dot-color="black"
            class="mb-8"
            style="padding-bottom: 50px;"
          >
          <template v-slot:icon>
            <v-avatar color="primary text-black">
              {{timelineRecipe.length}}
            </v-avatar>
          </template>
          <template v-slot:opposite>
            <v-card color="surface" rounded="xl">
              <v-card-text>{{date}}</v-card-text>
            </v-card>
          </template>
            <v-row>
              <recipe :recipe="recipe" v-for="recipe in timelineRecipe"></recipe>
            </v-row>

          </v-timeline-item>
        </v-timeline>
  </v-layout>
</template>

<script lang="ts" setup>

import {listRecipes} from "@/scripts/recipes";
import {useAuthStore} from "@/stores/auth";

const recipes = ref([])
const timelineRecipes = ref()
const authStore = useAuthStore()
const userId = authStore.id

listRecipes(`?user=${userId}&followedBy=${userId}&sort=DATE_DESCENDING`).then(
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
