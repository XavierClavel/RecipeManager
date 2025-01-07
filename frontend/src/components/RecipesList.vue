<script setup lang="ts">
import {base_url, toViewRecipe} from "@/scripts/common";
import {listRecipes} from "@/scripts/recipes";
import {useRoute} from "vue-router";


const recipes = ref<object[]>([])

console.log("list recipes")

const updateGrid = () => {
  listRecipes(window.location.search).then(
    function (response) {
      console.log(response)
      recipes.value = response.data
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
      <v-btn
        class="ma-4"
        rounded="lg"
        min-height="300px"
        width="300px"
        @click="toViewRecipe(recipe.id)"
      >
        <span class="flex-column">
        <v-img
          color="surface-variant"
          height="169px"
          width="300px"
          class="mt-n3"
          rounded="t-lg"
          :src="`${base_url}/image/recipes/${recipe.id}.webp`"
          cover
        ></v-img>
      <v-card-item>
        <div>
          <div class="text-h6 mb-1 text-primary">
            {{recipe.title}}
          </div>
          <div class="text-caption"></div>
        </div>

        <span class="d-flex flex-row">
        <span class="d-flex row align-center justify-center mx-auto">
          <v-icon
            class="mx-auto px-3 text-h4"
            color="primary"
          > mdi-silverware-fork-knife </v-icon>
          <v-card-text
            class="mx-auto px-3  align-center text-h5"
          > {{ recipe.yield }} </v-card-text>
        </span>

        <span class="d-flex row align-center justify-center mx-auto">
          <v-icon
            class="mx-auto px-3 text-h4"
            color="primary"
          > mdi-chef-hat </v-icon>
          <v-card-text
            class="mx-auto px-3 ml-n2  align-center text-h6"
          > {{ recipe.preparationTime }} min </v-card-text>
        </span>

        <span class="d-flex row align-center justify-center mx-auto" v-if="recipe.cookingTime">
          <v-icon
            class="mx-auto px-3 text-h4"
            color="primary"
          > mdi-stove </v-icon>
          <v-card-text
            class="mx-auto px-3  align-center ml-n2 text-h6"
          > {{ recipe.cookingTime }} min </v-card-text>
        </span>
      </span>
      </v-card-item>
          </span>

    </v-btn>
    </span>


  </v-layout>
</template>

<style scoped>

</style>
