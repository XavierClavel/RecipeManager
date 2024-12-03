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
      <editable-picture
        path="image/recipes"
        :id="recipeId"
        rounded="circle"
        height="200px"
        width="200px"
        buttons-size="50px"
        buttons-icon-size="text-h7"
        buttons-spacing="ga-4"
        buttons-rounded="lg"
      ></editable-picture>
      <v-container>
        <v-card-title
          class="mx-auto px-3 text-primary text-h3"
        >{{ user.username }}</v-card-title>
      </v-container>

    </v-container>

    <v-textarea
        v-model="user.bio"
        label="Description"
        class="mx-auto px-3"
        color="primary"
    ></v-textarea>

    <span class="d-flex align-center justify-center mb-2 mt-16 ga-16" >
        <v-btn
            prepend-icon="mdi-close-circle-outline"
            color="primary"
            flat
            rounded
            class="mb-10 text-h6"
            min-height="70px"
            min-width="300px"
            @click="toViewUser(userId)"
        >Cancel</v-btn>
        <v-btn
            @click="submit"
            prepend-icon="mdi-content-save"
            color="primary"
            flat
            rounded
            class="mb-10 text-h6"
            min-height="70px"
            min-width="300px"
        >Save</v-btn>
      </span>



  </v-card>

</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router';
import {
  createRecipe,
  deleteRecipe,
  deleteRecipeImage,
  getRecipe,
  updateRecipe,
  uploadRecipeImage
} from "@/scripts/recipes";
import {ref} from "vue";
import {toEditRecipe, toListRecipe, toViewRecipe, toViewUser} from "@/scripts/common";
import {getUser, getUserRecipesCount, updateUser} from "@/scripts/users";
import InteractiblePictoInfo from "@/components/InteractiblePictoInfo.vue";
import EditablePicture from "@/components/EditablePicture.vue";

// Get the route object
const route = useRoute();
const userId = route.query.id
let displayError = ref<Boolean>(false)
const errorMessage = ref<String>("This user does not exist")
const isOwner = true

const user = ref<object>({})
const recipesOwned = ref<number>(0)

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

async function submit() {
  const submitted = {}
  submitted["username"] = user.value.username
  submitted["bio"] = user.value.bio
  console.log(submitted)
  await updateUser(userId, submitted)
  toViewUser(userId)
}

</script>
