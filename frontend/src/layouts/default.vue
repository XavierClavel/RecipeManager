<template>
  <v-app>
    <v-main>
      <router-view />
    </v-main>

    <v-navigation-drawer
      v-model="drawer"
      color="#surface"
      floating
    >
      <v-list>
        <v-list-item title="Recipe Manager"></v-list-item>
        <v-list-item class="d-flex justify-center">
          <v-btn
            prepend-icon="mdi-pencil"
            link href="/recipe/edit"
            rounded
            min-height="50px"
            min-width="200px"
            class="elevation-0"
            color="primary"
          >
            New recipe
          </v-btn>
        </v-list-item>
        <v-list-item prepend-icon="mdi-home" link title="Home" href="/"></v-list-item>
        <v-list-item prepend-icon="mdi-dots-grid" link title="Recipes" href="/recipe/list"></v-list-item>
        <v-list-item prepend-icon="mdi-account-multiple" link title="Friends" href="/"></v-list-item>
        <v-list-item prepend-icon="mdi-shaker" link title="Ingredients" href="/"></v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar color="surface">
      <template v-slot:prepend>
        <v-app-bar-nav-icon @click="toggleDrawer"></v-app-bar-nav-icon>
      </template>
      <template v-slot:append>
        <v-btn icon="mdi-cog"></v-btn>
      </template>
      <v-app-bar-title>Recipe Manager</v-app-bar-title>
      <div class="d-flex flex-grow-1 justify-center align-center position-absolute"
       style ="
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        position: absolute;
        z-index: 1;"
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
            hint="Search a recipe"
            bg-color="primary"
          ></v-text-field>
        </v-card>
      </div>

    </v-app-bar>

    <AppFooter />
  </v-app>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

// Create a ref to control the visibility of the drawer
const drawer = ref(false)

// Function to toggle the drawer
const toggleDrawer = () => {
  drawer.value = !drawer.value
}
</script>
