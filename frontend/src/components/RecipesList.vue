<script setup lang="ts">
import {listRecipes} from "@/scripts/recipes";
import {useRoute} from "vue-router";

const props = defineProps({
  query: {
    type: String,
    required: false,
  },
})

const recipes = ref<object[]>([])
const noRecipes = ref(false)
const router = useRouter()
const routesToCheck = ["/recipe/list", "/ingredient/view", "/cookbook/recipes"]

const updateGrid = (query) => {
  console.log("updated recipe list")
  listRecipes(query).then(
    function (response) {
      console.log(`recipes from ${router.currentRoute.value.name}`, response)
      recipes.value = response.data
      noRecipes.value = recipes.value.length == 0
    }
  )
}

if (props.query) {
  updateGrid(props.query) }
else {
  updateGrid(window.location.search)
}

const removeAfterEach = router.afterEach((to, from) => {
  if (props.query) return
  if (!routesToCheck.includes(router.currentRoute.value.name)) {
    return
  }
  updateGrid(window.location.search)
})

// Cleanup when the component is unmounted
onUnmounted(() => {
  removeAfterEach() // Remove the navigation guard
})


</script>

<template>
  <v-row class="ma-1">
        <recipe :recipe="recipe" v-for="recipe in recipes"></recipe>
  </v-row>

  <v-card
    class="pa-5 ma-5 d-flex flex-row"
    v-if="noRecipes"
  >
    <v-icon color="black" class="text-h3 mr-5 ml-3 mt-2" >mdi-alert</v-icon>
    <v-card-title prepend-icon="mdi-alert" class="text-black text-h4">
      {{$t('no_recipe_to_display')}}
    </v-card-title>
  </v-card>

</template>

<style scoped>

</style>
