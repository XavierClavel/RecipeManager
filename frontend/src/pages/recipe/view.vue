<template>
  <v-card
    class="mx-auto pa-5 ma-auto my-5 d-flex flex-row"
    max-width="1000px"
    v-if="displayError"
  >
    <v-icon color="black" class="text-h3 mr-5 ml-3 mt-2" >mdi-alert</v-icon>
    <v-card-title prepend-icon="mdi-alert" class="text-h4">
      {{ errorMessage }}
    </v-card-title>
  </v-card>

  <v-card
  class="mx-auto pa-5 ma-auto my-5"
  max-width="1000px"
  v-if="!displayError"
  >
    <v-card-title
      class="mx-auto px-3 text-black text-h2 font-weight-bold"
    >{{ recipe.title }}</v-card-title>

    <v-card-text
      class="mx-auto px-3"
    > {{recipe.description}} </v-card-text>

    <user-info :user="recipe.owner" class="mt-n8 mb-4"></user-info>

    <v-img
      style="border: 3px solid #0d1821 !important;"
      color="surface-variant"
      height="600px"
      width="800px"
      class="mt-n3 mx-auto"
      rounded="lg"
      :src="getRecipeImageUrl(recipe.id)"

      cover
    ></v-img>


    <span class="d-flex align-center justify-center mb-2 my-5 ga-10">
      <v-btn
        v-if="recipeLiked"
        :icon="recipeLiked ? 'mdi-heart' : 'mdi-heart-outline' "
        color="background"
        flat
        class="mb-10 text-h6"
        min-height="70px"
        min-width="70px"
        @click="onLikeButtonClick"
        elevation="2"
      ></v-btn>

      <v-menu class="mt-n16"
        :close-on-content-click="false"
      >
        <template v-slot:activator="{ props }">
          <v-btn
            color="background"
            :icon="userCookbooks.some((it) => it.hasRecipe) ? 'mdi-bookmark' : 'mdi-bookmark-outline'"
            height="70px"
            width="70px"
            v-bind="props"
            class="mt-n10 text-h6"
          ></v-btn>
        </template>

        <v-card >
          <v-list density="compact" style="border: 0 solid !important;">
          <v-list-item
            prepend-icon="mdi-plus"
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
            link
            :title="cookbook.title"
            @click="onSelectCookbook(cookbook)"
          ></v-list-item>
        </v-list>
        </v-card>
      </v-menu>

      <v-btn
        icon="mdi-share-variant"
        color="background"
        flat
        class="mb-10 text-h6"
        min-height="70px"
        min-width="70px"
        @click="onShareButtonClick"
        elevation="2"
      ></v-btn>
    </span>

    <span class="d-flex flex-row">
        <picto-info :value="`${recipe.preparationTime} min`" icon="mdi-chef-hat" v-if="recipe.preparationTime"></picto-info>
        <picto-info :value="`${recipe.cookingTime} min`" icon="mdi-stove" v-if="recipe.cookingTime"></picto-info>
        <picto-info :value="`${recipe.cookingTemperature} °C`" icon="mdi-thermometer" v-if="recipe.cookingTemperature"></picto-info>
      </span>

    <v-number-input
      v-model="selectedYield"
      min="1"
      max-width="200px"
      class="mx-auto"
      control-variant="split"
      precision="0"
    ></v-number-input>

    <v-container v-if="recipe?.ingredients?.length || recipe?.customIngredients?.length">

      <h2 class="my-3" >Ingredients</h2>

    <v-list v-for="(ingredient, index) in recipe.ingredients">
      <v-card color="background" rounded="lg">
      <v-list-item
        :key="index"
        :subtitle="`${ingredient.amount ? (ingredient.amount * coefficient).toFixed(2).replace(/[.,]00$/, '') : ''}${unitToReadable(ingredient.unit)}`"
        :title="`${ingredient.name} ${ingredient.complement ? '(' + ingredient.complement + ')' : ''}`"
      >
        <template v-slot:prepend>
          <v-avatar color="surface" class="mr-4">
            <v-icon color="white">{{getIngredientIcon(ingredient.type)}}</v-icon>
          </v-avatar>
        </template>

        <template v-slot:append>
          <v-btn
            color="surface"
            icon="mdi-information"
            variant="text"
            style="border: 0 !important;"
            @click="toViewIngredient(ingredient.id)"
          ></v-btn>
        </template>

      </v-list-item>
      </v-card>
    </v-list>

    <v-list v-for="(ingredient, index) in recipe.customIngredients">
      <v-card color="background" class="py-1">
        <v-list-item
          :key="index"
          :subtitle="`${ingredient.amount ? (ingredient.amount * coefficient).toFixed(2).replace(/[.,]00$/, '') : ''}${unitToReadable(ingredient.unit)}`"
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

      <v-container v-if="recipe?.steps?.length">

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

    <v-card-text
      v-if="recipe.tips"
      class="mx-auto px-3"
    > {{recipe.tips}} </v-card-text>

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
          v-if="false"
          icon="mdi-tray-arrow-down"
          :text="`${$t('download')}`"
          :action="() => downloadRecipe(recipeId)"
        ></action-button>
      </span>

    <v-snackbar
      v-model="snackbar"
      :timeout="2000"
      height="60px"
    >
      {{$t('successfully_copied')}}
      <template v-slot:actions>
        <v-btn
          variant="flat"
          color="primary"
          @click="snackbar = false"
          icon="mdi-close"
          height="40px"
          width="40px"
        >

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
import {getIngredientIcon} from "@/scripts/icons";
import {useI18n} from "vue-i18n";

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
const {t} = useI18n()
const selectedYield = ref(null)

const scaledAmount = computed((amount) => (amount * coefficient).toFixed(2).replace(/[.,]00$/, ''))

const coefficient = computed(() =>  selectedYield.value / recipe.value.yield)

const recipe = ref<object>({
  steps: [''],
  ingredients: [],
  owner: {}
})

getRecipe(recipeId).then (
  function (response) {
    recipe.value = response.data
    selectedYield.value = recipe.value.yield
    console.log("Recipe", recipe.value)
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
  getStatusInCookbooks(recipeId).then(
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
