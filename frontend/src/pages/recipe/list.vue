<template>
  <v-container>
    <v-chip-group
      v-model="chosenSource"
      column
      multiple
      @update:modelValue="updateUrl"
      color="surface"
      variant="flat"
    >
      <v-chip
        v-for="s in source"
        :text="s"
        filter
      ></v-chip>
    </v-chip-group>

    <v-chip-group
      v-model="chosenDishType"
      column
      multiple
      @update:modelValue="updateUrl"
      color="surface"
      variant="flat"
    >
      <v-chip
        v-for="dish in dishTypes"
        :text="dish"
        filter
      ></v-chip>
    </v-chip-group>


  </v-container>
  <recipes-list :query="recipeQueries"></recipes-list>
</template>

<script lang="ts" setup>
import RecipesList from "@/components/RecipesList.vue";
import {useAuthStore} from "@/stores/auth";

const dishTypes = ["Entree","Main Dish","Desert","Other"]
const dishClasses = ["ENTREE", "MAIN_DISH", "DESERT", "OTHER"]
const source = ["My recipes", "Likes", "Cookbooks", "Follows"]

const chosenDishType = ref([0, 1, 2])
const chosenSource = ref([0,1])

const authStore = useAuthStore()

const recipeQueries = ref("")
import router from "@/router";

const updateUrl = () => {
  const route = router.currentRoute
  router.push({
    path: route.path, // Keep the current path
    query: Object.fromEntries(
      Object.entries({
        ...route.query,
        owner: chosenSource.value.includes(0) ? authStore.id : undefined,
        likedBy: chosenSource.value.includes(1) ? authStore.id : undefined,
        cookbookUser: chosenSource.value.includes(2) ? authStore.id : undefined,
        follows: chosenSource.value.includes(3) ? authStore.id : undefined,
        dishClasses: chosenDishType.value.length > 0 ? chosenDishType.value.map(it => dishClasses[it]) .join(",") : undefined
      }).filter(([_, value]) => value !== undefined) // Remove undefined values
    ),
  })
}

</script>

<style>
.input-group--disabled.checkbox .input-group__input {
  color: #000 !important;
}
</style>
