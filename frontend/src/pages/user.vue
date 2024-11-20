<template>
  <v-card
    class="mx-auto rounded-xl pa-5 ma-auto my-5 d-flex flex-row"
    max-width="1000px"
    v-if="displayError"
  >
    <v-icon color="primary" class="text-h3 mr-5 ml-3 mt-2" >mdi-alert</v-icon>
    <v-card-title prepend-icon="mdi-alert" class="text-primary text-h4">
      {{ errorMessage }}
    </v-card-title>
  </v-card>

  <v-card
  class="mx-auto rounded-xl pa-5 ma-auto my-5"
  max-width="1000px"
  v-if="!displayError"
  >
    <v-card-title
      class="mx-auto px-3 text-primary text-h3"
    >{{ user.username }}</v-card-title>

    <span>
      <v-container>
        <v-card-text>
          {{recipesOwned}}
        </v-card-text>
        <v-card-text>
          Recipes
        </v-card-text>
      </v-container>
    </span>

  </v-card>

</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router';
import {deleteRecipe, getRecipe} from "@/scripts/recipes";
import {ref} from "vue";
import {toEditRecipe, toListRecipe} from "@/scripts/common";
import {getUser, getUserRecipesCount} from "@/scripts/users";

// Get the route object
const route = useRoute();
const username = route.query.username
let displayError = ref<Boolean>(false)
const errorMessage = ref<String>("This user does not exist")
const isOwner = true

const user = ref<object>({})
const recipesOwned = ref<number>(0)

getUser(username).then (
  function (response) {
    console.log(response)
    user.value = response.data
  }).catch(function (error) {
    displayError.value = true
  console.log(error);
    console.log(displayError)
}).finally(function () {
  // always executed
});

getUserRecipesCount(username).then (
  function (response) {
    console.log(response)
    recipesOwned.value = response.data
  }).catch(function (error) {
    displayError.value = true
    console.log(error);
    console.log(displayError);
  }
)

const remove = (id) => {
  deleteRecipe(id)
  toListRecipe()
}

</script>
