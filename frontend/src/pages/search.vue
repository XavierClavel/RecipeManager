<script lang="ts" setup>

import {searchUsers, updateUser} from "@/scripts/users";
import {listRecipes} from "@/scripts/recipes";
import {useAuthStore} from "@/stores/auth";
import {searchIngredients} from "@/scripts/ingredients";
import {searchCookbooks} from "@/scripts/cookbooks";

const route = useRoute()
const searchQuery = ref(route.query.search || '')
const users = ref([])
const recipes = ref([])
const ingredients = ref([])
const cookbooks = ref([])

const authStore = useAuthStore()
const userId = computed(() => authStore.id)

async function updateSearch() {
  console.log(searchQuery.value)
  await updateUsers()
  await updateRecipes()
  await updateIngredients()
  await updateCookbooks()
}

async function updateCookbooks() {
  const response = await searchCookbooks(searchQuery.value, 0, 20);
  cookbooks.value = response.data
}

async function updateUsers() {
  const response = await searchUsers(searchQuery.value, 0, 20);
  users.value = response.data.items;
  console.log(users.value)
}

async function updateRecipes() {
  const response = await listRecipes(`?user=${userId}&likedBy=${userId}&cookbookUser=${userId}&followedBy=${userId}&search=${searchQuery.value}`)
  recipes.value = response.data
  console.log(recipes.value)
}

async function updateIngredients() {
  const response = await searchIngredients(searchQuery.value, 0, 20)
  ingredients.value = response.data.items
  console.log(ingredients.value)
}

watch(() => route.query.search, (newQuery) => {
  searchQuery.value = newQuery || ''
  updateSearch()
})

updateSearch()


</script>
<template>
  <v-card-title
    v-if="!ingredients?.length && !users?.length && !recipes?.length && !cookbooks?.length"
    class="text-black text-h2 font-weight-bold my-8"
  >{{$t("no_result")}}</v-card-title>
  <v-card
    class="my-5"
    color="transparent"
    style="border:0"
    variant="flat"
    v-if="users?.length"
  >
    <v-card-title
      class="text-black text-h2 font-weight-bold"
    >{{$t("users")}}</v-card-title>
  </v-card>

    <v-row class="mx-5">
      <user v-for="user in users" :user="user"></user>
    </v-row>
  <v-card
    class="my-5 mt-16"
    color="transparent"
    style="border:0"
    variant="flat"
    v-if="recipes?.length"
  >
    <v-card-title
      class="text-black text-h2 font-weight-bold"
    >{{$t("recipes")}}</v-card-title>
  </v-card>
    <v-row class="mx-5">
      <recipe v-for="recipe in recipes" :recipe="recipe"></recipe>
    </v-row>
  <v-card
    class="my-5 mt-16"
    color="transparent"
    style="border:0"
    variant="flat"
    v-if="ingredients?.length"
  >
    <v-card-title
      class="text-black text-h2 font-weight-bold"
    >{{$t("ingredients")}}</v-card-title>
  </v-card>

  <v-row class="mx-5">
    <ingredient v-for="ingredient in ingredients" :ingredient="ingredient"></ingredient>
  </v-row>

  <v-card
    class="my-5 mt-16"
    color="transparent"
    style="border:0"
    variant="flat"
    v-if="cookbooks?.length"
  >
    <v-card-title
      class="text-black text-h2 font-weight-bold"
    >{{$t("cookbooks")}}
    </v-card-title>
  </v-card>

  <v-row class="mx-5 mb-16">
    <cookbook v-for="cookbook in cookbooks" :cookbook="cookbook"></cookbook>
  </v-row>

</template>
