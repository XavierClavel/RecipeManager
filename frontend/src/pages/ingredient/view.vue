<template>

  <v-card
    class="rounded-xl pa-5 ma-5"
  >
    <v-container class="d-flex flex-row">

      <v-icon
        color="primary"
        :src="imageUrl"
        rounded="lg"
        height="200px"
        width="200px"
        cover
      >{{getIngredientIcon(ingredient.type)}} </v-icon>
      <v-container
      class="px-3 mx-auto"
      >
        <v-card-title
          class="text-primary text-h3"
        >{{ ingredient.name }}</v-card-title>
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
        <v-table
          min-height="300px"
          fixed-header
          class="rounded-0"
        >
          <tbody>
          <tr>
            <th class="text-left">
              Calories
            </th>
            <th class="text-left">
              {{ingredient.calories}}
            </th>
          </tr>

          <tr>
            <th class="text-left">
              Glucids
            </th>
            <th class="text-left">
              {{ingredient.glucids}}
            </th>
          </tr>

          </tbody>
        </v-table>
      </v-container>

    </v-container>
  </v-card>
  <recipes-list></recipes-list>
</template>

<script lang="ts" setup>
import {useRoute} from "vue-router";
import {ref} from "vue";
import InteractiblePictoInfo from "@/components/InteractiblePictoInfo.vue";
import {base_url, toEditCookbook, toEditUser} from "@/scripts/common";
import {getCookbook} from "@/scripts/cookbooks";
import {getIngredientIcon, ICON_COOKBOOK_RECIPES, ICON_COOKBOOK_USERS} from "@/scripts/icons";

const route = useRoute();
let ingredientId = ref(route.query.ingredient)
const imageUrl = computed(() => `${base_url}/image/cookbooks/${ingredientId.value}.webp`);

const ingredient = ref<object>({
  id: null,
  type: "MEAT",
  name: "My ingredient",
  recipesCount: 0,
  calories: 0,
  glucids: 0,
})


if (ingredientId.value != null) {
  // getCookbook(ingredientId.value).then (
  //   function (response) {
  //     cookbook.value.title = response.data.title
  //     cookbook.value.description = response.data.description
  //     cookbook.value.recipesCount = response.data.recipesCount
  //     cookbook.value.usersCount = response.data.usersCount
  //     console.log(response.data)
  //   }).catch(function (error) {
  //   console.log(error);
  // }).finally(function () {
  //   // always executed
  // });
}

</script>
