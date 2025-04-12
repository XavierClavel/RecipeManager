<template>

  <error :error="errors"></error>
  <v-card
    v-if="!errors"
    class="pa-5"
  >
    <v-container class="d-flex flex-row">
      <v-img
        color="surface-variant"
        :src="imageUrl"
        rounded="lg"
        height="200px"
        width="200px"
        cover
        style="border: 3px solid #0d1821 !important;"
      ></v-img>
      <v-container
      class="px-3 mx-auto"
      >
        <v-card-title
          class="text-black text-h2 font-weight-bold my-n8"
        >{{ cookbook.title }}</v-card-title>
        <v-card-text>
          {{cookbook.description}}
        </v-card-text>
        <v-row class="d-flex flex-row mx-4">
          <v-col class="d-inline-flex" cols="auto">
            <picto-info :icon="`${ICON_COOKBOOK_RECIPES}`" :value="cookbook.recipesCount" icon-size="text-h4" value-size="text-h5"></picto-info>
          </v-col>
          <v-col class="d-inline-flex" cols="auto">
            <picto-info :icon="`${ICON_COOKBOOK_USERS}`" :value="cookbook.usersCount" icon-size="text-h4" value-size="text-h5"></picto-info>
          </v-col>
        </v-row>
        <v-row class="px-3" v-if="isAdmin">
          <action-button
            icon="mdi-cog"
            :text="`${$t('edit')}`"
            :action="() => toEditCookbook(cookbookId)"
          ></action-button>
          <action-button
            icon="mdi-door-open"
            :text="`${$t('leave')}`"
            :action="() => leave()"
          ></action-button>
        </v-row>
      </v-container>

    </v-container>
  </v-card>
  <recipes-list v-if="!errors"></recipes-list>
</template>

<script lang="ts" setup>
import {useRoute} from "vue-router";
import {ref} from "vue";
import {toEditCookbook, toEditUser, toListCookbooks, toViewIngredient} from "@/scripts/common";
import {getCookbook, isAdminOfCookbook, leaveCookbook} from "@/scripts/cookbooks";
import {ICON_COOKBOOK_RECIPES, ICON_COOKBOOK_USERS} from "@/scripts/icons";
import {useAuthStore} from "@/stores/auth";
const route = useRoute();
let cookbookId = ref(route.query.cookbook)
const imageUrl = computed(() => `${import.meta.env.VITE_API_URL}/image/cookbooks/${cookbookId.value}.webp`);
const isAdmin = ref(false)
const cookbook = ref<object>({
  title: "",
  description: "",
  users: [],
  recipesCount: null,
  usersCount: null,
})
const errors = ref(null)


if (cookbookId.value != null) {
  getCookbook(cookbookId.value).then (
    function (response) {
      cookbook.value.title = response.data.title
      cookbook.value.description = response.data.description
      cookbook.value.recipesCount = response.data.recipesCount
      cookbook.value.usersCount = response.data.usersCount
      console.log(response.data)
    }).catch(function (error) {
      console.log(error);
      errors.value = error.response.data;
  })
  isAdminOfCookbook(cookbookId.value).then(
    function (response) {
      isAdmin.value = response.data
    }
  )
}

async function leave() {
  await leaveCookbook(cookbookId.value)
  const authStore = useAuthStore()
  toListCookbooks(authStore.id)
}
</script>
