<script setup lang="ts">
import {getCookbookIconUrl, toViewCookbookRecipes} from "@/scripts/common";
import {ICON_COOKBOOK_RECIPES, ICON_COOKBOOK_USERS, ICON_RECIPE_YIELD} from "../scripts/icons";

defineProps({
  cookbook: {
    type: Object,
    required: true,
  },
})
</script>

<template>
  <v-btn
    :color="color"
    :variant="variant"
    class="ma-4"
    rounded="lg"
    min-height="300px"
    width="300px"
    @click="toViewCookbookRecipes(cookbook.id)"
  >
        <span class="flex-column">
          <v-img
            color="surface-variant"
            height="169px"
            width="300px"
            class="mt-n3"
            rounded="t-lg"
            :src="getCookbookIconUrl(cookbook.id)"
            cover
          ></v-img>
          <v-card-item>
            <div>
              <div class="text-h6 mb-1 text-primary">
                {{ cookbook.title }}
              </div>
              <div class="text-caption"></div>
            </div>

            <span class="d-flex flex-row">
              <picto-info :icon="`${ICON_COOKBOOK_RECIPES}`" :value="cookbook.recipesCount" icon-size="text-h4" value-size="text-h5"></picto-info>
              <picto-info :icon="`${ICON_COOKBOOK_USERS}`" :value="cookbook.usersCount" icon-size="text-h4" value-size="text-h5"></picto-info>
            </span>
          </v-card-item>
        </span>
    </v-btn>

</template>

<style scoped>

</style>
