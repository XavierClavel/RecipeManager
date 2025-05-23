<template>

  <v-card
    class="pa-5 pb-n4"
  >
    <v-container class="d-flex
      flex-wrap justify-center
      flex-sm-nowrap">

      <v-img
        color="background"
        rounded="lg"
        height="200px"
        width="200px"
        cover
        style="min-width: 200px; max-width: 200px; border: 3px solid #0d1821 !important;"
        :src="getIngredientImageUrl(ingredient.type)"
      ></v-img>
      <v-container
      class="px-3 mx-auto mb-n8"
      >
        <v-card-title class="mx-auto px-3 mt-n8 text-black text-h2 text-center text-sm-left font-weight-bold"
        >{{ ingredient.name[getLocale().toUpperCase()] }}</v-card-title>
        <v-row class="d-flex flex-row mx-4" justify="center" justify-sm="start">
          <v-col class="d-inline-flex" cols="auto">
            <picto-info :icon="`${ICON_COOKBOOK_RECIPES}`" :value="recipesCount" icon-size="text-h4" value-size="text-h5"></picto-info>
          </v-col>
        </v-row>
      </v-container>
    </v-container>
  </v-card>


  <v-card class="pa-8 my-2 mt-4">
    <ingredient-nutrional-data :ingredient="ingredient" class="my-n6"></ingredient-nutrional-data>
  </v-card>
  <recipes-list :query="`?ingredient=${ingredientId}`"></recipes-list>
</template>

<script lang="ts" setup>
import {useRoute} from "vue-router";
import {ref} from "vue";
import {ICON_COOKBOOK_RECIPES} from "@/scripts/icons";
import {getIngredient, getIngredientRecipesCount} from "@/scripts/ingredients";
import {useAuthStore} from "@/stores/auth";
import {getLocale} from "@/scripts/localization";
import IngredientNutrionalData from "@/components/IngredientNutritionalData.vue";
import {getIngredientImageUrl} from "@/scripts/common";
const route = useRoute();
let ingredientId = ref(route.query.ingredient)
const ingredient = ref<object>({
  name: "",
})
const recipesCount = ref(null)

const authStore = useAuthStore()

console.log(ingredientId.value)

getIngredient(ingredientId.value).then((response) => {
  ingredient.value = response.data
  console.log(ingredient.value)
})

getIngredientRecipesCount(ingredientId.value).then(response => {
  recipesCount.value = response.data
})

</script>
