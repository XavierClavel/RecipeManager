<template>
  <v-app>

    <v-navigation-drawer
      v-model="drawer"
      color="#surface"
      floating
      v-if="showSidebar"
    >
      <v-list>
        <v-list-item title="Recipe Manager"></v-list-item>
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
            New recipe
          </v-btn>
        </v-list-item>
        <v-list-item prepend-icon="mdi-home" rounded="xl" link title="Home" @click="toHome"></v-list-item>
        <v-list-item prepend-icon="mdi-dots-grid" rounded="xl" link title="Recipes" @click="toListRecipe"></v-list-item>
        <v-list-item prepend-icon="mdi-heart-outline" rounded="xl" link title="Likes" @click="toHome"></v-list-item>
        <v-list-item prepend-icon="mdi-compass-outline" rounded="xl" link title="Browse" @click="toHome"></v-list-item>
        <v-list-item prepend-icon="mdi-security" rounded="xl" link title="Admin" @click="toUsers"></v-list-item>
        <v-list-item prepend-icon="mdi-information-slab-circle-outline" rounded="xl" link title="About" @click="toHome"></v-list-item>
        <v-list-item prepend-icon="mdi-logout" rounded="xl" link title="Logout" @click="logout"></v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar color="surface" scroll-behavior="hide" scroll-threshold="100">
      <template v-slot:prepend>
        <v-app-bar-nav-icon @click="toggleDrawer"></v-app-bar-nav-icon>
      </template>
      <template v-slot:append>
        <v-btn icon="mdi-account"></v-btn>
      </template>
      <v-app-bar-title>Recipe Manager</v-app-bar-title>
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
            label="Search a recipe"
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
import router from "@/router";
import {logout, noLoginRedirect, toCreateRecipe, toHome, toListRecipe, toUsers, whoami} from "@/scripts/common";

const route = useRoute();

// Create a ref to control the visibility of the drawer
const drawer = ref(true)

// Function to toggle the drawer
const toggleDrawer = () => {
  drawer.value = !drawer.value
}

const showSidebar = computed(() => !noLoginRedirect.includes(route.name));

</script>
