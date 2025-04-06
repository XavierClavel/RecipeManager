<template>
  <v-layout class="rounded rounded-md d-flex flex-wrap  justify-space-evenly mt-6">
    <v-card class="d-flex flex-column pa-0 ma-0" width="100%" color="transparent" variant="flat" style="border:0 !important">
    <v-card-title>{{$t("cookbooks")}}</v-card-title>

      <v-card class="d-flex pa-0 ma-0" color="transparent" variant="flat" style="border:0 !important">
        <v-row class="ma-1">
          <cookbook :cookbook="cookbook" v-for="cookbook in cookbooks"></cookbook>
        </v-row>
    <v-btn
      color="black"
      class="ma-4 text-h1 text-black"
      rounded="lg"
      height="324px"
      width="250px"
      @click="toCreateCookbook()"
      icon="mdi-plus"
      variant="outlined"
      style="border: 4px solid #0d1821 !important;"
    ></v-btn>
      </v-card>
    </v-card>


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
    console.log(response.data)
    cookbooks.value = response.data
  }
)

</script>
