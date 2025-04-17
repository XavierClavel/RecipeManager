<template>

  <v-card
    class="pa-5"
  >
    <v-container class="d-flex flex-row">

      <v-img
        color="primary"
        rounded="lg"
        height="200px"
        width="200px"
        cover
        style="border: 3px solid #0d1821 !important;"
      ></v-img>
      <v-container
      class="px-3 mx-auto"
      >
        <v-card-title class="mt-n8"
        >{{ getLocale() == 'fr' ? ingredient.name_fr : ingredient.name_en }}</v-card-title>
        <v-row class="d-flex flex-row mx-4">
          <v-col class="d-inline-flex" cols="auto">
            <picto-info :icon="`${ICON_COOKBOOK_RECIPES}`" :value="ingredient.recipesCount" icon-size="text-h4" value-size="text-h5"></picto-info>
          </v-col>
        </v-row>
        <v-row class="px-3">
          <v-btn
          icon="mdi-cog"
          flat
          @click="toEditCookbook(cookbookId)"
          ></v-btn>
        </v-row>
      </v-container>

      <ingredient-nutrional-data :ingredient="ingredient" class="my-n6"></ingredient-nutrional-data>

    </v-container>
  </v-card>
  <recipes-list :query="`?ingredient=${ingredientId}`"></recipes-list>
</template>

<script lang="ts" setup>
import {useRoute} from "vue-router";
import {ref} from "vue";
import {toEditCookbook} from "@/scripts/common";
import {ICON_COOKBOOK_RECIPES} from "@/scripts/icons";
import {getIngredient} from "@/scripts/ingredients";
import {useAuthStore} from "@/stores/auth";
import {getLocale} from "@/scripts/localization";
import IngredientNutrionalData from "@/components/IngredientNutrionalData.vue";
const route = useRoute();
let ingredientId = ref(route.query.ingredient)
const ingredient = ref<object>({
  name: "",
  recipesCount: 0,
})

const authStore = useAuthStore()

console.log(ingredientId.value)

getIngredient(ingredientId.value).then((response) => {
  ingredient.value = response.data
  console.log(ingredient.value)
})

</script>
