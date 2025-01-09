<template>
  <v-layout class="rounded rounded-md d-flex flex-wrap  justify-space-evenly mt-6">
    <span v-for="cookbook in cookbooks" class="justify-start ">
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
            :src="`${base_url}/image/cookbooks/${cookbook.id}.webp`"
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
              <span class="d-flex row align-center justify-center mx-auto">
                <v-icon
                  class="mx-auto px-3 text-h4"
                  color="primary"
                > mdi-silverware-fork-knife </v-icon>
                <v-card-text
                  class="mx-auto px-3  align-center text-h5"
                > {{ cookbook.recipesCount }} </v-card-text>
              </span>

              <span class="d-flex row align-center justify-center mx-auto">
                <v-icon
                  class="mx-auto px-3 text-h4"
                  color="primary"
                > mdi-account-multiple </v-icon>
                <v-card-text
                  class="mx-auto px-3  align-center text-h5"
                > {{ cookbook.usersCount }} </v-card-text>
              </span>
            </span>
          </v-card-item>
        </span>

    </v-btn>
    </span>
    <v-btn
      :color="color"
      :variant="variant"
      class="ma-4 text-h1 text-primary"
      rounded="lg"
      min-height="300px"
      width="300px"
      @click="toCreateCookbook()"
      icon="mdi-plus"
      variant="outlined"
    ></v-btn>


  </v-layout>
</template>

<script lang="ts" setup>
import {listRecipes} from "@/scripts/recipes";
import {base_url, toCreateCookbook, toCreateRecipe, toViewCookbookRecipes, toViewRecipe} from "@/scripts/common";
import {useRoute} from "vue-router";
import {listCookbooks} from "@/scripts/cookbooks";

const cookbooks = ref<object[]>([])

const route = useRoute();

listCookbooks(window.location.search).then(
  function (response) {
    console.log(response)
    cookbooks.value = response.data
  }
)

const toRecipe = (id) => toViewRecipe((id))
</script>
