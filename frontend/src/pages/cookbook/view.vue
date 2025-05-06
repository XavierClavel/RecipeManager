<template>

  <error :error="errors"></error>
  <v-card
    v-if="!errors"
    class="pa-5"
  >
    <v-container class="d-flex
      flex-wrap justify-center
      flex-sm-nowrap">
      <v-img
        color="surface-variant"
        :src="getCookbookIconUrl(cookbook.id, cookbook.version)"
        rounded="lg"
        height="200px"
        width="200px"
        max-width="200px"
        aspect-ratio="1/1"
        cover
        style="border: 3px solid #0d1821 !important;"
      ></v-img>
        <v-container class="d-flex flex-column">
        <v-card-title
          class="text-black text-h2 font-weight-bold my-n8
          text-center text-sm-left"
        >{{ cookbook.title }}</v-card-title>
        <v-card-text class="text-center text-sm-left">
          {{cookbook.description}}
        </v-card-text>
      <v-row class="d-flex flex-row
          justify-center justify-sm-start">
        <v-col class="d-inline-flex" cols="auto">
          <picto-info :icon="`${ICON_COOKBOOK_RECIPES}`" :value="cookbook.recipesCount" icon-size="text-h4" value-size="text-h5"></picto-info>
        </v-col>
        <v-col class="d-inline-flex" cols="auto">
          <picto-info :icon="`${ICON_COOKBOOK_USERS}`" :value="cookbook.usersCount" icon-size="text-h4" value-size="text-h5"></picto-info>
        </v-col>
      </v-row>
        </v-container>
      </v-container>
      <v-container class="d-flex flex-wrap ga-2">

        <v-row class="px-3 my-1" v-if="isAdmin">
          <action-button
            icon="mdi-cog"
            :text="`${$t('edit')}`"
            :action="() => toEditCookbook(cookbookId)"
          ></action-button>

          <v-dialog max-width="500">
            <template v-slot:activator="{ props: activatorProps }">
              <action-button
                v-bind="activatorProps"
                icon="mdi-door-open"
                :text="`${$t('leave')}`"
              ></action-button>
            </template>

            <template v-slot:default="{ isActive }">
              <v-card>
                <v-card-title class="text-h5">{{$t("leaveCookbookTitle")}}</v-card-title>
                <v-card-text>
                  {{$t("leaveCookbookDescription")}}
                </v-card-text>

                <v-card-actions>
                  <v-spacer></v-spacer>

                  <v-btn
                    :text="`${$t('cancel')}`"
                    @click="isActive.value = false"
                  ></v-btn>
                  <v-btn
                    :text="`${$t('leave')}`"
                    @click="() => leave()"
                  ></v-btn>
                </v-card-actions>
              </v-card>
            </template>
          </v-dialog>
        </v-row>
      </v-container>
  </v-card>
  <recipes-list v-if="!errors"></recipes-list>
</template>

<script lang="ts" setup>
import {useRoute} from "vue-router";
import {ref} from "vue";
import {getCookbookIconUrl, getUserIconUrl, toEditCookbook, toListCookbooks} from "@/scripts/common";
import {getCookbook, isAdminOfCookbook, leaveCookbook} from "@/scripts/cookbooks";
import {ICON_COOKBOOK_RECIPES, ICON_COOKBOOK_USERS} from "@/scripts/icons";
import {useAuthStore} from "@/stores/auth";
const route = useRoute();
let cookbookId = ref(route.query.cookbook)
const isAdmin = ref(false)
const cookbook = ref<object>({
  title: "",
  description: "",
  users: [],
  recipesCount: null,
  usersCount: null,
  version: null,
})
const errors = ref(null)


if (cookbookId.value != null) {
  getCookbook(cookbookId.value).then (
    function (response) {
      cookbook.value = response.data
      console.log(cookbook.value)
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
