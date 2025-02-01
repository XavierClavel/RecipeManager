<script setup lang="ts">
import {listRecipes} from "@/scripts/recipes";
import {useRoute} from "vue-router";


const recipes = ref<object[]>([])
const noRecipes = ref(false)
const router = useRouter()
const routesToCheck = ["/recipe/list", "/ingredient/view", "/cookbook/recipes"]

const updateGrid = () => {
  listRecipes(window.location.search).then(
    function (response) {
      console.log(`recipes from ${router.currentRoute.value.name}`, response)
      recipes.value = response.data
      noRecipes.value = recipes.value.length == 0
    }
  )
}

updateGrid()



const removeAfterEach = router.afterEach((to, from) => {
  if (!routesToCheck.includes(router.currentRoute.value.name)) {
    return
  }
  updateGrid()
})

// Cleanup when the component is unmounted
onUnmounted(() => {
  removeAfterEach() // Remove the navigation guard
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
