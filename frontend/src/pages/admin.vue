<template>
    <v-navigation-drawer
      v-model="drawer"
      color="#surface"
      floating
    >
      <v-list>
        <v-list-item title="aaaa"></v-list-item>
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
        <v-list-item prepend-icon="mdi-dots-grid" rounded="xl" link title="Recipes" @click="toListRecipe('')"></v-list-item>
        <v-list-item prepend-icon="mdi-heart-outline" rounded="xl" link title="Likes" @click="toHome"></v-list-item>
        <v-list-item prepend-icon="mdi-food-apple-outline" rounded="xl" link title="Ingredients" @click="toListIngredient"></v-list-item>
        <v-list-item prepend-icon="mdi-notebook-outline" rounded="xl" link title="Cookbooks" @click="toMyCookbooks"></v-list-item>
        <v-list-item prepend-icon="mdi-security" rounded="xl" link title="Admin" @click="toUsers"></v-list-item>

      </v-list>
    </v-navigation-drawer>
</template>

<script lang="ts" setup>
import {getUsers, deleteUser} from "@/scripts/users";
import {toCreateRecipe, toHome, toListIngredient, toListRecipe, toMyCookbooks, toUsers} from "@/scripts/common";
let users = ref<string[]>([])

const performDelete = (username) => {
  deleteUser(username).then(
    function (response) {
      if (response.status == 200) {
        const index = users.value.findIndex((user) => user.username == username)
        users.value.splice(index,1)
      }
    }).catch(function (error) {
    console.log(error);
  }).finally(function () {
    // always executed
  });

};

getUsers().then (
  function (response) {
    console.log(response)
    users.value = response.data
}).catch(function (error) {
  console.log(error);
}).finally(function () {
    // always executed
  });

</script>
