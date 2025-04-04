<script setup lang="ts">
import {getRecipeThumbnailUrl, toViewIngredient, toViewRecipe} from "@/scripts/common";
import {getIngredientIcon} from "../scripts/icons";

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
            color="primary"
            height="150px"
            class="mt-n2"
            rounded="t-lg"
            :src="getIngredientIcon(ingredient.type)"
            cover
          ></v-img>
        <v-card-title class="text-black text-h6 font-weight-regular justify-end">{{ingredient.name}}</v-card-title>

    </v-card>

</template>

<style scoped>

</style>
