<template>
  <v-app>
    <v-main>
      <router-view />
    </v-main>

    <v-navigation-drawer color="#254441" floating>
      <v-list>
        <v-list-item title="Recipe Manager"></v-list-item>
        <v-list-item prepend-icon="mdi-dots-grid" link title="List Item 1"></v-list-item>
        <v-list-item link title="List Item 2"></v-list-item>
        <v-list-item link title="List Item 3"></v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar color="#254441">
      <template v-slot:prepend>
        <v-app-bar-nav-icon></v-app-bar-nav-icon>
      </template>

      <v-app-bar-title>Recipe Manager</v-app-bar-title>
    </v-app-bar>

    <AppFooter />
  </v-app>
</template>

<script lang="ts" setup>
  //
</script>
