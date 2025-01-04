<template>
  <v-container>
    <v-chip-group
      v-model="chosenSource"
      column
      multiple
      @update:modelValue="onUpdateSource"
    >
      <v-chip
        v-for="s in source"
        :text="s"
        variant="outlined"
        filter
      ></v-chip>
    </v-chip-group>

    <v-chip-group
      v-model="chosenDishType"
      column
      multiple
    >
      <v-chip
        v-for="dish in dishTypes"
        :text="dish"
        variant="outlined"
        filter
      ></v-chip>
    </v-chip-group>


  </v-container>
  <recipes-list :query="recipeQueries" ref="recipesList"></recipes-list>
</template>

<script lang="ts" setup>
import RecipesList from "@/components/RecipesList.vue";
import {useAuthStore} from "@/stores/auth";
import { useRouter, useRoute } from 'vue-router';

const dishTypes = ["Entree","Main Dish","Desert"]
const source = ["My recipes", "Likes", "Cookbooks", "Follows"]

const chosenDishType = ref([0, 1, 2])
const chosenSource = ref([0,1])

const recipesList = ref(null)

const authStore = useAuthStore()

const recipeQueries = ref("")
import router from "@/router";

const onUpdateSource = () => {
  updateUrl()
}

const updateUrl = () => {
  const route = router.currentRoute
  router.push({
    path: route.path, // Keep the current path
    query: Object.fromEntries(
      Object.entries({
        ...route.query,
        owner: chosenSource.value.includes(0) ? authStore.username : undefined,
        likedBy: chosenSource.value.includes(1) ? authStore.username : undefined,
        userCookbooks: chosenSource.value.includes(2) ? authStore.username : undefined,
        follows: chosenSource.value.includes(3) ? authStore.username : undefined,
        dishTypes: chosenDishType.value.length > 0 ? chosenDishType.value.join(",") : undefined
      }).filter(([_, value]) => value !== undefined) // Remove undefined values
    ),
  })
}

updateUrl()


</script>
