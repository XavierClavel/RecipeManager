<template>
  <v-app>
    <v-main>
      <router-view />
    </v-main>

    <v-navigation-drawer
      v-model="drawer"
      color="#254441"
      floating
    >
      <v-list>
        <v-list-item title="Recipe Manager"></v-list-item>
        <v-list-item class="d-flex justify-center">
          <v-btn
            prepend-icon="mdi-pencil"
            link href="/"
            rounded
            min-height="50px"
            min-width="200px"
          >
            New recipe
          </v-btn>
        </v-list-item>
        <v-list-item prepend-icon="mdi-home" link title="Home" href="/"></v-list-item>
        <v-list-item prepend-icon="mdi-dots-grid" link title="Recipes" href="/recipes"></v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar color="#254441">
      <template v-slot:prepend>
        <v-app-bar-nav-icon @click="toggleDrawer"></v-app-bar-nav-icon>
      </template>
      <v-app-bar-title>Recipe Manager</v-app-bar-title>
      <div class="d-flex flex-grow-1 justify-center align-center">
        <v-card
          min-width="500px"
          height="48px"
          class="mx-auto d-flex justify-center rounded-xl elevation-0"
          color="#202020"
        >
          <v-text-field
            class="mx-auto elevation-0"
            prepend-inner-icon="mdi-magnify"
            hide-details
            single-line
            variant="solo"
            clearable
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
