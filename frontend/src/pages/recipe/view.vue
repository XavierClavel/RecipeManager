<template>
  <v-card
    class="mx-auto pa-5 ma-auto my-5 d-flex flex-row"
    max-width="1000px"
    v-if="displayError"
  >
    <v-icon color="primary" class="text-h3 mr-5 ml-3 mt-2" >mdi-alert</v-icon>
    <v-card-title prepend-icon="mdi-alert" class="text-primary text-h4">
      {{ errorMessage }}
    </v-card-title>
  </v-card>

  <v-card
  class="mx-auto pa-5 ma-auto my-5"
  max-width="1000px"
  v-if="!displayError"
  >
    <v-card-title
      class="mx-auto px-3 text-primary text-h3"
    >{{ recipe.title }}</v-card-title>

    <v-card-text
      class="mx-auto px-3"
    > {{recipe.description}} </v-card-text>

    <user-info :user="recipe.owner" class="mt-n8 mb-4"></user-info>



<!--
    <v-card-text
      class="mx-auto px-3"
    > Created on : {{new Date(recipe.creationDate * 1000).toLocaleDateString()}} </v-card-text>

    <v-card-text
      class="mx-auto px-3"
    > Last edited on : {{new Date(recipe.editionDate * 1000).toLocaleDateString()}} </v-card-text>
-->

    <v-img
      color="surface-variant"
      height="600px"
      width="800px"
      class="mt-n3 mx-auto"
      rounded="lg"
      :src="getRecipeImageUrl(recipe.id)"

      cover
    ></v-img>

      <span class="d-flex flex-row">
        <picto-info :value="recipe.yield" icon="mdi-silverware-fork-knife" v-if="recipe.yield"></picto-info>
        <picto-info :value="recipe.conservationTime" icon="mdi-fridge" v-if="recipe.conservationTime"></picto-info>
        <picto-info :value="`${recipe.preparationTime} min`" icon="mdi-chef-hat" v-if="recipe.preparationTime"></picto-info>
        <picto-info :value="`${recipe.cookingTime} min`" icon="mdi-stove" v-if="recipe.cookingTime"></picto-info>
        <picto-info :value="`${recipe.cookingTemperature} °C`" icon="mdi-thermometer" v-if="recipe.cookingTemperature"></picto-info>
      </span>


    <span class="d-flex align-center justify-center mb-2 my-5 ga-10">
      <v-btn
        v-if="recipeLiked != null"
        :icon="recipeLiked ? 'mdi-heart' : 'mdi-heart-outline' "
        color="primary"
        flat
        rounded="circle"
        class="mb-10 text-h6"
        min-height="70px"
        min-width="70px"
        @click="onLikeButtonClick"
        variant="outlined"
      ></v-btn>

      <v-menu class="mt-n16"
        :close-on-content-click="false"
      >
        <template v-slot:activator="{ props }">
          <v-btn
            color="primary"
            :icon="userCookbooks.some((it) => it.hasRecipe) ? 'mdi-bookmark' : 'mdi-bookmark-outline'"
            height="70px"
            width="70px"
            rounded="circle"
            v-bind="props"
            class="mt-n10 text-h6"
            variant="outlined"
          ></v-btn>
        </template>

        <v-card>
          <v-list density="compact">
          <v-list-item
            prepend-icon="mdi-plus"
            rounded="xl"
            link
            title="New cookbook"
            @click="toCreateCookbookAddRecipe(recipeId)"
          ></v-list-item>
          <v-divider></v-divider>
          <v-list-item
            v-for="cookbook in userCookbooks"
            :key="cookbook.id"
            density="compact"
            :prepend-icon="cookbook.hasRecipe ? 'mdi-check-circle' : 'mdi-plus-circle-outline'"
            rounded="xl"
            link
            :title="cookbook.title"
            @click="onSelectCookbook(cookbook)"
          ></v-list-item>
        </v-list>
        </v-card>


      </v-menu>

      <v-btn
        icon="mdi-share-variant"
        color="primary"
        flat
        rounded="circle"
        class="mb-10 text-h6"
        min-height="70px"
        min-width="70px"
        @click="onShareButtonClick"
        variant="outlined"
      ></v-btn>
    </span>

    <v-container>

      <h2 class="my-3" >Ingredients</h2>

    <v-list v-for="(ingredient, index) in recipe.ingredients">
      <v-card color="background" rounded="lg">
      <v-list-item
        :key="index"
        :subtitle="`${ingredient.amount ? ingredient.amount : ''}${unitToReadable(ingredient.unit)}`"
        :title="`${ingredient.name} ${ingredient.complement ? '(' + ingredient.complement + ')' : ''}`"
      >
        <template v-slot:prepend>
          <v-avatar color="surface">
            <v-icon color="white">{{getIngredientIcon(ingredient.type)}}</v-icon>
          </v-avatar>
        </template>

        <template v-slot:append>
          <v-btn
            color="surface"
            icon="mdi-information"
            variant="text"
            @click="toViewIngredient(ingredient.id)"
          ></v-btn>
        </template>

      </v-list-item>
      </v-card>
    </v-list>

    <v-list v-for="(ingredient, index) in recipe.customIngredients">
      <v-card color="background">
        <v-list-item
          :key="index"
          :subtitle="`${ingredient.amount ? ingredient.amount : ''}${unitToReadable(ingredient.unit)}`"
          :title="ingredient.name"
        >
          <template v-slot:prepend>
            <v-avatar color="surface">
              <v-icon color="white">mdi-carrot</v-icon>
            </v-avatar>
          </template>


        </v-list-item>
      </v-card>
    </v-list>

    </v-container>

      <v-container v-if="recipe.steps.length > 0">

      <h2 class="my-3" >Steps</h2>

        <v-list v-for="(step, index) in recipe.steps">
          <v-card color="background" rounded="lg">
            <v-list-item>
              <v-card-text class="text-wrap">{{step}}</v-card-text>
              <template v-slot:prepend>
                <v-avatar color="surface">
                  {{index + 1}}
                </v-avatar>
              </template>

            </v-list-item>
          </v-card>
        </v-list>

      </v-container>

      <span class="d-flex align-center justify-center mb-10 mt-16 ga-16" >
        <action-button
          icon="mdi-delete"
          :text="`${$t('delete')}`"
          :action="() => remove(recipeId)"
          v-if="isOwner"
        ></action-button>
        <action-button
          icon="mdi-pencil"
          :text="`${$t('edit')}`"
          :action="() => toEditRecipe(recipeId)"
          v-if="isOwner"
        ></action-button>
        <action-button
          icon="mdi-tray-arrow-down"
          :text="`${$t('download')}`"
          :action="() => downloadRecipe(recipeId)"
        ></action-button>
      </span>

    <v-snackbar
      v-model="snackbar"
      :timeout="2000"
    >
      Link successfully copied !
      <template v-slot:actions>
        <v-btn
          color="surface"
          variant="text"
          @click="snackbar = false"
        >
          Close
        </v-btn>
      </template>
    </v-snackbar>



  </v-card>

</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router';
import {deleteRecipe, downloadRecipe, getRecipe} from "@/scripts/recipes";
import {ref} from "vue";
import {
  getRecipeImageUrl,
  toCreateCookbookAddRecipe,
  toEditRecipe,
  toListRecipe, toViewIngredient, toViewUser,
  unitToReadable
} from "@/scripts/common";
import {useAuthStore} from "@/stores/auth";
import {addLike, isLiked, removeLike} from "@/scripts/likes";
import {addRecipeToCookbook, getStatusInCookbooks, listCookbooks, removeRecipeFromCookbook} from "@/scripts/cookbooks";
import {getIngredientIcon} from "../../scripts/icons";

// Get the route object
const route = useRoute();
const recipeId = route.query.id
let displayError = ref<boolean>(false)
const errorMessage = ref<string>("This recipe does not exist")
const isOwner = ref<boolean>(false)
const recipeLiked = ref<string>(null)
const snackbar = ref(false)

const userCookbooks = ref([])
const authStore = useAuthStore()
const userId = authStore.id

const recipe = ref<object>({
  steps: [''],
  ingredients: [],
  owner: {}
})

getRecipe(recipeId).then (
  function (response) {
    recipe.value = response.data
    console.log("Recipe", recipe.value)
    const authStore = useAuthStore()
    isOwner.value = response.data.owner.id == authStore.id
    console.log("Recipe owner", recipe.value.owner)
  }).catch(function (error) {
    displayError.value = true
  console.log(error);
    console.log(displayError)
}).finally(function () {
  // always executed
});

isLiked(recipeId).then (
  function (response) {
    recipeLiked.value = response
    console.log(response)
  }
)


const updateCookbook = () => {
  getStatusInCookbooks(userId, recipeId).then(
    function (response) {
      userCookbooks.value = [...response.data]
      console.log("status in cookbooks",response.data)
    }
  )
}

updateCookbook()

const remove = (id) => {
  deleteRecipe(id)
  toListRecipe()
}

const onLikeButtonClick = () => {
  if (recipeLiked.value) {
    removeLike(recipeId)
  } else {
    addLike(recipeId)
  }
  recipeLiked.value = !recipeLiked.value
}

const onShareButtonClick = () => {
  navigator.clipboard.writeText(window.location.href);
  snackbar.value = true
}

const onSelectCookbook = (cookbook) => {
  if (cookbook.hasRecipe) {
    removeRecipeFromCookbook(cookbook.id, recipeId).then(
      function(response) {
      updateCookbook()
    })
  } else {
    addRecipeToCookbook(cookbook.id, recipeId).then(
      function(response) {
        updateCookbook()
      })
  }
}

</script>
