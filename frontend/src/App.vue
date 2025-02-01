<template>
  <v-app>

    <v-navigation-drawer
      v-model="drawer"
      floating
      v-if="showSidebar"
    >
      <v-list>
        <v-list-item title="Cook&Co"></v-list-item>
        <v-list-item class="d-flex justify-center">
          <v-btn
            prepend-icon="mdi-pencil"
            rounded
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
    </v-navigation-drawer>

    <v-app-bar color="surface" scroll-behavior="hide" scroll-threshold="100">
      <template v-slot:prepend>
        <v-app-bar-nav-icon @click="toggleDrawer"></v-app-bar-nav-icon>
      </template>
      <template v-slot:append v-if="showSidebar">
        <v-menu>
          <template v-slot:activator="{ props }">
            <v-img
              color="surface-variant"
              :src="getUserIconUrl(userId)"
              height="50px"
              width="50px"
              rounded="circle"
              cover
              v-bind="props"
              class="clickable_image"
            ></v-img>
          </template>

          <v-list>
            <v-list-item prepend-icon="mdi-account-circle" rounded="xl" link :title="`${$t('profile')}`" @click="toMyProfile" ></v-list-item>
            <v-list-item prepend-icon="mdi-cog" rounded="xl" link :title="`${$t('settings')}`" @click="toMyProfile" ></v-list-item>
            <v-list-item prepend-icon="mdi-information-slab-circle-outline" rounded="xl" link :title="`${$t('about')}`" @click="toHome"></v-list-item>
            <v-list-item prepend-icon="mdi-logout" rounded="xl" link :title="`${$t('log_out')}`" @click="logout" ></v-list-item>
          </v-list>
        </v-menu>
      </template>
      <v-app-bar-title>Cook&Co</v-app-bar-title>
      <div class="d-flex flex-grow-1 justify-center align-center position-absolute"
           style ="
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        position: absolute;
        z-index: 1;"
           v-if="showSidebar"
      >
        <v-card
          min-width="500px"
          max-width="100%"
          height="48px"
          class="d-flex align-center rounded-xl"
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
          ></v-text-field>
        </v-card>
      </div>

    </v-app-bar>

    <v-main>
      <router-view />
    </v-main>

  </v-app>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router';
import {
  getUserIconUrl,
  logout,
  noLoginRedirect,
  toCreateRecipe,
  toHome,
  toListIngredient,
  toListRecipe, toMyCookbooks, toMyProfile,
  toUsers,
} from "@/scripts/common";
import {useAuthStore} from "@/stores/auth";
import {ICON_ADMIN, ICON_COOKBOOK, ICON_HOME, ICON_INGREDIENT, ICON_RECIPE} from "@/scripts/icons";

const authStore = useAuthStore()
const userId = ref(authStore.id)

const route = useRoute();

// Create a ref to control the visibility of the drawer
const drawer = ref(true)

// Function to toggle the drawer
const toggleDrawer = () => {
  drawer.value = !drawer.value
}

const showSidebar = computed(() => !noLoginRedirect.includes(route.name));

</script>
<style scoped>
.clickable_image {
  cursor: pointer
}
</style>
