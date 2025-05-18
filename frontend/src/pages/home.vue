<template>
  <v-layout class="rounded rounded-md">
    <v-card class="pl-sm-2 pt-4 d-flex flex-column" color="transparent" style="border:0 solid" variant="flat">
      <v-card-title
        class="text-h4 text-sm-h2 font-weight-bold text-left"
      >
        {{$t("welcome")}}, {{username}}! ✨
      </v-card-title>

      <v-card-text class="text-sm-h4 text-md-h3 mb-8 mt-n4 mt-sm-n6 font-weight-thin">
        {{$t("welcome_subtext")}}
      </v-card-text>

      <v-infinite-scroll
        @load="loadMore"
        :disabled="isLoading || allRecipesLoaded"
        side="end"
        margin="200"
        style="overflow:hidden"
      >
        <v-timeline
          class="my-2"
          align="start"
          side="end"
          line-color="black"
          line-thickness="2px"
          :density="xs ? 'compact' : 'comfortable'"
        >

        <v-timeline-item
          v-for="(timelineRecipe, date) in timelineRecipes"
          :key="date"
          size="large"
          side="end"
          dot-color="background"
          fill-dot
        >
        <template v-slot:icon>
          <v-avatar color="primary text-black" size="46" style="border: 3px solid #0d1821 !important;" >
            {{timelineRecipe.length}}
          </v-avatar>
        </template>
          <v-card-text class="mt-n2 ml-n6 text-wrap text-h6 text-sm-h6 font-weight-bold">{{ $t(date) }}</v-card-text>
          <v-row class="mt-1 mb-16">

            <recipe class="ma-2" :recipe="recipe" v-for="recipe in timelineRecipe"></recipe>
          </v-row>

        </v-timeline-item>
      </v-timeline>
        <template v-slot:loading>
          <v-progress-circular
            v-if="isLoading"
            indeterminate
            color="black"
            class="my-5 d-flex mx-auto"
          />
        </template>
        </v-infinite-scroll>
    </v-card>
  </v-layout>
</template>

<script lang="ts" setup>

import {listRecipes} from "@/scripts/recipes";
import {useAuthStore} from "@/stores/auth";
import {useI18n} from "vue-i18n";
import { useDisplay } from 'vuetify';

const recipes = ref([])
const timelineRecipes = ref()
const authStore = useAuthStore()
const userId = computed(() => authStore.id)
const username = computed(() => authStore.username)
const currentPage = ref(0)
const isLoading = ref(false)
const allRecipesLoaded = ref(false)

const { t } = useI18n()
const { xs, sm, md } = useDisplay();

const dayNames = ["sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"];



const loadMore = async ({ done }: { done: () => void }) => {
  if (isLoading.value || allRecipesLoaded.value) return
  isLoading.value = true
  try {
    await updateGrid()
    isLoading.value = false
    refreshing.value = false
  } catch (error) {
    console.error(error)
  } finally {
    isLoading.value = false
    done()
  }
}

const updateGrid = async() => {
  const response = await listRecipes(`?user=${userId.value}&followedBy=${userId.value}&sort=DATE_DESCENDING`, currentPage.value, 20)
  if (response.data.length === 0) {
    allRecipesLoaded.value = true
    noRecipes.value = recipes.value.length == 0
  } else {
    recipes.value.push(...response.data)
    currentPage.value++
    timelineRecipes.value = recipes.value.reduce((acc, recipe) => {
      let groupKey = new Date(recipe.creationDate * 1000).toLocaleDateString()
      switch (groupKey) {
        case new Date().toLocaleDateString():
          groupKey = "today"
          break

        case new Date((new Date()).valueOf() - 1000*60*60*24).toLocaleDateString():
          groupKey = "yesterday"
          break

        case new Date((new Date()).valueOf() - 2 * 1000*60*60*24).toLocaleDateString():
          groupKey = dayNames[Date(recipe.creationDate * 1000).getDay()]
          break

        case new Date((new Date()).valueOf() - 3 * 1000*60*60*24).toLocaleDateString():
          groupKey = dayNames[Date(recipe.creationDate * 1000).getDay()]
          break

        case new Date((new Date()).valueOf() - 4 * 1000*60*60*24).toLocaleDateString():
          groupKey = dayNames[Date(recipe.creationDate * 1000).getDay()]
          break

        case new Date((new Date()).valueOf() - 5 * 1000*60*60*24).toLocaleDateString():
          groupKey = dayNames[Date(recipe.creationDate * 1000).getDay()]
          break

        case new Date((new Date()).valueOf() - 6 * 1000*60*60*24).toLocaleDateString():
          groupKey = dayNames[Date(recipe.creationDate * 1000).getDay()]
          break
      }

      if (!acc[groupKey]) {
        acc[groupKey] = [];
      }

      acc[groupKey].push(recipe);
      return acc;
    }, {}); // Initialize with a Map
    console.log(timelineRecipes.value)
  }
}

updateGrid()

</script>
