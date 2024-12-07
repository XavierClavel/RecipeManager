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
    <v-container class="d-flex flex-row">
      <v-img
        color="surface-variant"
        :src="imageUrl"
        rounded="circle"
        height="200px"
        width="200px"
        cover
      ></v-img>
      <v-container>
        <v-card-title
          class="mx-auto px-3 text-primary text-h3"
        >{{ user.username }}</v-card-title>
        <span class="d-flex flex-row">
          <interactible-picto-info :value="user.recipesCount" icon="mdi-notebook" :action="redirectRecipesOwned"></interactible-picto-info>
          <interactible-picto-info :value="user.likesCount" icon="mdi-heart" :action="redirectRecipesLiked"></interactible-picto-info>
          <interactible-picto-info :value="-1" icon="mdi-account-heart"></interactible-picto-info>
          <interactible-picto-info :value="-1" icon="mdi-account-multiple"></interactible-picto-info>
        </span>
      </v-container>

    </v-container>

    <v-card-text
      class="mx-auto px-3"
    > {{user.bio}} </v-card-text>

    <span class="d-flex align-center justify-center mb-2 mt-16 ga-16" v-if="isOwner" >

      <v-btn
        prepend-icon="mdi-pencil"
        color="primary"
        flat
        rounded
        class="mb-10 text-h6"
        min-height="70px"
        min-width="300px"
        @click="toEditUser(userId)"
      >Edit</v-btn>
    </span>


  </v-card>

</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router';
import {deleteRecipe, getRecipe} from "@/scripts/recipes";
import {ref} from "vue";
import {base_url, toEditRecipe, toEditUser, toListRecipe} from "@/scripts/common";
import {getUser} from "@/scripts/users";
import InteractiblePictoInfo from "@/components/InteractiblePictoInfo.vue";

// Get the route object
const route = useRoute();
const userId = route.query.id
let displayError = ref<boolean>(false)
const errorMessage = ref<string>("This user does not exist")
const isOwner = true

const user = ref<object>({})
const imageUrl = computed(() => `${base_url}/image/users/${userId}.webp`);

const redirectRecipesOwned = () => {
  toListRecipe(`?owner=${userId}`)
}

const redirectRecipesLiked = () => {
  toListRecipe(`?likedBy=${userId}`)
}


getUser(userId).then (
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


const remove = (id) => {
  deleteRecipe(id)
  toListRecipe()
}

</script>
