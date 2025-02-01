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
        :src="getUserIconUrl(user.id)"
        rounded="circle"
        height="200px"
        width="200px"
        cover
      ></v-img>
      <v-container>
        <v-card-title
          class="mx-auto px-3 text-primary text-h3"
        >{{ user.username }}</v-card-title>
        <v-card-text
          class="mx-auto px-3"
        > {{user.bio}} </v-card-text>
        <span class="d-flex flex-row">
          <interactible-picto-info :value="user.recipesCount" :icon="`${ICON_USER_RECIPES}`" :action="redirectRecipesOwned"></interactible-picto-info>
          <interactible-picto-info :value="user.likesCount" :icon="`${ICON_USER_LIKES}`" :action="redirectRecipesLiked"></interactible-picto-info>
          <interactible-picto-info :value="user.followsCount" :icon="`${ICON_USER_FOLLOWS}`"></interactible-picto-info>
          <interactible-picto-info :value="user.followersCount" :icon="`${ICON_USER_FOLLOWERS}`"></interactible-picto-info>
        </span>
      </v-container>

    </v-container>

    <span class="d-flex align-center justify-center mt-8 mb-8 ga-16"  >

      <action-button
        :icon="`${followsUser ? 'mdi-account-minus' : 'mdi-account-plus'}`"
        :text="`${followsUser ? $t('unfollow') : $t('follow')}`"
        :action="followUnfollow"
        v-if="userId != currentUserId"
      ></action-button>
      <action-button
        icon="mdi-pencil"
        :text="`${$t('edit')}`"
        :action="() => toEditUser(userId)"
        v-if="userId == currentUserId"
      ></action-button>
    </span>
    <error :error="errorMessage"></error>


  </v-card>

  <recipes-list></recipes-list>

</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router';
import {ref} from "vue";
import {base_url, getUserIconUrl, toEditUser, toListRecipe} from "@/scripts/common";
import {getUser} from "@/scripts/users";
import InteractiblePictoInfo from "@/components/InteractiblePictoInfo.vue";
import {follow, isFollowingUser, unfollow} from "@/scripts/follows";
import {useAuthStore} from "@/stores/auth";
import {ICON_USER_FOLLOWERS, ICON_USER_FOLLOWS, ICON_USER_LIKES, ICON_USER_RECIPES} from "@/scripts/icons";
import RecipesList from "@/components/RecipesList.vue";

// Get the route object
const route = useRoute();
const userId = route.query.user
const errorMessage = ref(null)
const authStore = useAuthStore();
const currentUserId = authStore.id

const user = ref<object>({})
const followsUser = ref(null)

isFollowingUser(userId).then (
  function (response) {
    console.log(response)
    followsUser.value = response.data
  }).catch(function (error) {
    errorMessage.value = error.response.data
})

const redirectRecipesOwned = () => {
  toListRecipe(`?owner=${userId}`)
}

const redirectRecipesLiked = () => {
  toListRecipe(`?likedBy=${userId}`)
}

async function followUnfollow() {
  if (followsUser.value) {
    await unfollow(userId).catch(function (error) {
      errorMessage.value = error.response.data
    })
  }
  else {
    await follow(userId).catch(function (error) {
      errorMessage.value = error.response.data
    })
  }
  followsUser.value = !followsUser.value
  updateUser()
}


const updateUser = () => {
  getUser(userId).then (
    function (response) {
      console.log(response)
      user.value = response.data
    }).catch(function (error) {
    errorMessage.value = error.response.data
  });
}

updateUser()
</script>
