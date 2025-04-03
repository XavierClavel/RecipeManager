<template>
  <v-app class="d-flex" >

    <v-navigation-drawer
      v-model="drawer"
      floating
      v-if="showSidebar"
      class="custom-drawer"
      elevation="2"
      width="250"
      variant="elevated"
      color="#4f4193"
    >
      <v-list>
        <v-img src="/src/assets/logo.png" class="mx-12 my-8"></v-img>
        <v-list-item class="d-flex justify-center">
          <v-btn
            prepend-icon="mdi-pencil"
            rounded="pill"
            min-height="50px"
            min-width="200px"
            class="elevation-0"
            color="primary"
            @click="toCreateRecipe"
          >
            {{$t("new_recipe")}}
          </v-btn>
        </v-list-item>
        <v-list-item :prepend-icon="ICON_HOME" rounded="xl" link :title="`${$t('home')}`" @click="toHome"></v-list-item>
        <v-list-item :prepend-icon="ICON_RECIPE" rounded="xl" link :title="`${$t('recipes')}`" @click="toListRecipe(`?user=${userId}&likedBy=${userId}`)"></v-list-item>
        <v-list-item :prepend-icon="ICON_INGREDIENT" rounded="xl" link :title="`${$t('ingredients')}`" @click="toListIngredient"></v-list-item>
        <v-list-item :prepend-icon="ICON_COOKBOOK" rounded="xl" link :title="`${$t('cookbooks')}`" @click="toMyCookbooks"></v-list-item>
        <v-list-item :prepend-icon="ICON_ADMIN" rounded="xl" link :title="`${$t('admin')}`" @click="toUsers"></v-list-item>

      </v-list>
      <template v-slot:append>
        <v-card-text class="text-center">{{ `Version ${version}` }}</v-card-text>
      </template>
      </v-navigation-drawer>

    <v-main class="d-flex flex-grow-1">
    <v-container fluid class="d-flex">
    <v-card
      color="transparent"
      style="border: 0"
      class="custom-bar flex-grow-1 mt-n2  mb-n4"
      v-if="showSidebar"
      variant="flat"
    >
      <template v-slot:prepend>
        <v-app-bar-nav-icon @click="toggleDrawer" style="border: 3px solid #0d1821 !important;"></v-app-bar-nav-icon>
      </template>
      <template v-slot:append>
        <v-btn :icon="ICON_NOTIFICATION" flat class="mr-4 text-black" color="#0476a3" style="border: 3px solid #0d1821 !important;"></v-btn>
        <v-menu style="border: 0" >
          <template v-slot:activator="{ props }">
            <v-avatar size="50" variant="elevated" style="border:3px solid #0d1821 !important;">
              <v-img
                color="surface-variant"
                :src="getUserIconUrl(userId)"
                cover
                v-bind="props"
                class="clickable_image"
              ></v-img>
            </v-avatar>
          </template>

          <v-list base-color="black" bg-color="#0476a3" style="border: 3px solid #0d1821 !important;">
            <v-list-item prepend-icon="mdi-account-circle" rounded="xl" link :title="`${$t('profile')}`" @click="toMyProfile" ></v-list-item>
            <v-list-item prepend-icon="mdi-cog" rounded="xl" link :title="`${$t('settings')}`" @click="toSettings" ></v-list-item>
            <v-list-item prepend-icon="mdi-information-slab-circle-outline" rounded="xl" link :title="`${$t('about')}`" @click="toHome"></v-list-item>
            <v-list-item prepend-icon="mdi-logout" rounded="xl" link :title="`${$t('log_out')}`" @click="logout" ></v-list-item>
          </v-list>
        </v-menu>
      </template>

      <div class="d-flex flex-grow-1 justify-center align-center position-absolute"
           style ="
            left: 50%;
            top: 50%;
            transform: translate(-50%, -50%);
            width: 40%;
            position: absolute;
            z-index: 1;"
           v-if="showSidebar"
      >
        <v-card
          width="100%"
          height="48px"
          class="d-flex align-center"
        >
          <v-text-field
            class="mx-auto"
            prepend-inner-icon="mdi-magnify"
            hide-details
            single-line
            variant="solo"
            clearable
            flat
            :label="`${$t('search_recipe')}`"
            bg-color="primary"
            @update:modelValue="toSearch"
          ></v-text-field>
        </v-card>
      </div>

    </v-card>
    </v-container>
    </v-main>

    <v-main class="ma-2 ml-6 mr-4 mt-0">
      <router-view />
    </v-main>

  </v-app>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router';
import {
  getHealth,
  getUserIconUrl,
  logout,
  noLoginRedirect,
  toCreateRecipe,
  toHome,
  toListIngredient,
  toListRecipe, toMyCookbooks, toMyProfile, toSettings,
  toUsers,
} from "@/scripts/common";
import {useAuthStore} from "@/stores/auth";
import { debounce } from 'lodash'
import {ICON_ADMIN, ICON_COOKBOOK, ICON_HOME, ICON_INGREDIENT, ICON_NOTIFICATION, ICON_RECIPE} from "@/scripts/icons";
import {overrideLocaleFromCookie} from "@/scripts/localization";

const authStore = useAuthStore()
const userId = computed(() => authStore.id)
const router = useRouter()
const version = ref(null)


const route = useRoute();

// Create a ref to control the visibility of the drawer
const drawer = ref(true)

// Function to toggle the drawer
const toggleDrawer = () => {
  drawer.value = !drawer.value
}

const showSidebar = computed(() => !noLoginRedirect.includes(route.name));

const toSearch = debounce((query) => {
  if (!query) return; // Avoid empty redirects
  router.push({ name: '/search', query: { search: query } })
}, 500) // Buffer input for 500ms

overrideLocaleFromCookie()

getHealth().then((response) => {
  version.value = response.data.version
})

</script>

<style scoped>
.clickable_image {
  cursor: pointer
}
.custom-drawer {
  margin: 16px;
  max-height: calc(100% - 32px);
  overflow: hidden;
}

.custom-bar {
  //margin: 8px; /* Adjust margin to prevent overflow */
  //margin-left: 0px;
  //margin-right: 16px;
  //max-width: calc(100% - 296px); /* Reduce width to account for margin */
  height: 65px; /* Optional: Limit width */
  //overflow: hidden; /* Ensures child elements respect border-radius */
}
</style>
