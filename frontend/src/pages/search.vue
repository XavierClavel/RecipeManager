<script lang="ts" setup>

import {searchUsers, updateUser} from "@/scripts/users";
import {listRecipes} from "@/scripts/recipes";
import {useAuthStore} from "@/stores/auth";
import {searchIngredients} from "@/scripts/ingredients";

const route = useRoute()
const searchQuery = ref(route.query.search || '')
const users = ref([])
const recipes = ref([])
const ingredients = ref([])

const authStore = useAuthStore()
const userId = authStore.id

async function updateSearch() {
  console.log(searchQuery.value)
  updateUsers()
  updateRecipes()
  updateIngredients()
}

async function updateUsers() {
  const response = await searchUsers(searchQuery.value, 0, 20);
  users.value = response.data;
  console.log(users.value)
}

async function updateRecipes() {
  const response = await listRecipes(`?user=${userId}&likedBy=${userId}&cookbookUser=${userId}&followedBy=${userId}&search=${searchQuery.value}`)
  recipes.value = response.data
  console.log(recipes.value)
}

async function updateIngredients() {
  const response = await searchIngredients(searchQuery.value, 0, 20)
  ingredients.value = response.data
  console.log(ingredients.value)
}

watch(() => route.query.search, (newQuery) => {
  searchQuery.value = newQuery || ''
  updateSearch()
})

updateSearch()


</script>
<template>
  <v-card
    class="ma-5"
  >
    <v-card-title
      class="text-primary text-h3"
    >{{$t("users")}}</v-card-title>
  </v-card>

    <v-row class="mx-5">
      <user v-for="user in users" :user="user"></user>
    </v-row>
  <v-card
    class="ma-5"
  >
    <v-card-title
      class="text-primary text-h3"
    >{{$t("recipes")}}</v-card-title>
  </v-card>
    <v-row class="mx-5">
      <recipe v-for="recipe in recipes" :recipe="recipe"></recipe>
    </v-row>
  <v-card
    class="ma-5"
  >
    <v-card-title
      class="text-primary text-h3"
    >{{$t("ingredients")}}</v-card-title>
  </v-card>

</template>
