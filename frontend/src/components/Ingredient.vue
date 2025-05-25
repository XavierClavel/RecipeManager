<script setup lang="ts">
import {getIngredientImageUrl, getRecipeThumbnailUrl, toViewIngredient, toViewRecipe} from "@/scripts/common";
import {getIngredientIcon} from "../scripts/icons";
import {getLocale} from "@/scripts/localization";

defineProps({
  ingredient: {
    type: Object,
    required: true,
  },
})
</script>

<template>
      <v-card
        class="ma-3 pa-0"
        width="150px"
        @click="toViewIngredient(ingredient.id)"
      >
          <v-img
            height="150px"
            class="mt-n2"
            rounded="t-lg"
            :src="getIngredientImageUrl(ingredient.type)"
            cover
          ></v-img>
        <v-card-title class="text-black text-h6 text-sm-h6 text-truncate font-weight-regular justify-end">{{ingredient.name[getLocale().toUpperCase()]}}</v-card-title>

    </v-card>

</template>

<style scoped>

</style>
