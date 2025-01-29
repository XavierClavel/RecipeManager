<script setup lang="ts">
import {base_url, toViewRecipe} from "@/scripts/common";
import {listRecipes} from "@/scripts/recipes";
import {useRoute} from "vue-router";
import {ICON_RECIPE_YIELD} from "../scripts/icons";


const recipes = ref<object[]>([])
const noRecipes = ref(false)

console.log("list recipes")

const updateGrid = () => {
  listRecipes(window.location.search).then(
    function (response) {
      console.log(response)
      recipes.value = response.data
      noRecipes.value = recipes.value.length == 0
    }
  )
}

updateGrid()


const router = useRouter()

router.afterEach((to, from) => {
  updateGrid()
})


</script>

<template>
  <v-layout class="rounded rounded-md d-flex flex-wrap  justify-space-evenly mt-6">
    <span v-for="recipe in recipes" class="justify-start ">
      <recipe :recipe="recipe"></recipe>
    </span>


  </v-layout>

  <v-card
    class="rounded-xl pa-5 ma-5 d-flex flex-row"
    v-if="noRecipes"
  >
    <v-icon color="primary" class="text-h3 mr-5 ml-3 mt-2" >mdi-alert</v-icon>
    <v-card-title prepend-icon="mdi-alert" class="text-primary text-h4">
      No recipes to display
    </v-card-title>
  </v-card>

</template>

<style scoped>

</style>
