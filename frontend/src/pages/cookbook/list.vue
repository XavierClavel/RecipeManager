<template>
  <v-layout class="rounded rounded-md d-flex flex-wrap  justify-space-evenly mt-6">
    <span v-for="cookbook in cookbooks" class="justify-start ">
      <cookbook :cookbook="cookbook"></cookbook>
    </span>
    <v-btn
      :color="color"
      :variant="variant"
      class="ma-4 text-h1 text-primary"
      rounded="lg"
      min-height="300px"
      width="300px"
      @click="toCreateCookbook()"
      icon="mdi-plus"
      variant="outlined"
    ></v-btn>


  </v-layout>
</template>

<script lang="ts" setup>
import {
  toCreateCookbook,
} from "@/scripts/common";
import {useRoute} from "vue-router";
import {listCookbooks} from "@/scripts/cookbooks";
import Cookbook from "@/components/Cookbook.vue";

const cookbooks = ref<object[]>([])

const route = useRoute();

listCookbooks(window.location.search).then(
  function (response) {
    console.log(response)
    cookbooks.value = response.data
  }
)

const toRecipe = (id) => toViewRecipe((id))
</script>
