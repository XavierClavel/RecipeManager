<script setup lang="ts">
import {listRecipes} from "@/scripts/recipes";
import {useRoute} from "vue-router";


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
  console.log("router update")
  updateGrid()
})


</script>

<template>
  <v-row class="ma-8">
        <recipe :recipe="recipe" v-for="recipe in recipes"></recipe>
  </v-row>
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
