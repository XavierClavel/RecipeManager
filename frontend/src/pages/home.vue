<template>
  <v-layout class="rounded rounded-md">
    <v-card class="pa-4 d-flex flex-column" color="transparent" style="border:0" variant="flat">
    <v-card-title
      class="text-black text-left text-h2 font-weight-bold "
      style="justify-content: flex-start;"
    >{{$t("welcome")}}, {{username}}! ✨</v-card-title>
    <v-card-text class="text-h3 mb-8 mt-n6 font-weight-thin">{{$t("welcome_subtext")}}</v-card-text>
        <v-timeline class="my-2" align="center" side="end" line-color="black" line-thickness="3px" density="comfortable" >
          <v-timeline-item
            v-for="(timelineRecipe, date) in timelineRecipes"
            :key="date"
            size="large"
            side="end"
            fill-dot
            dot-color="black"
          >
          <template v-slot:icon>
            <v-avatar color="primary text-black" size="38">
              {{timelineRecipe.length}}
            </v-avatar>
          </template>
          <template v-slot:opposite>
            <v-card color="surface" rounded="xl">
              <v-card-text>{{date}}</v-card-text>
            </v-card>
          </template>
            <v-row class="my-4">
              <recipe :recipe="recipe" v-for="recipe in timelineRecipe"></recipe>
            </v-row>

          </v-timeline-item>
        </v-timeline>
    </v-card>
  </v-layout>
</template>

<script lang="ts" setup>

import {listRecipes} from "@/scripts/recipes";
import {useAuthStore} from "@/stores/auth";
import {useI18n} from "vue-i18n";

const recipes = ref([])
const timelineRecipes = ref()
const authStore = useAuthStore()
const userId = computed(() => authStore.id)
const username = computed(() => authStore.username)

const { t } = useI18n()

listRecipes(`?user=${userId.value}&followedBy=${userId.value}&sort=DATE_DESCENDING`).then(
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
