<template>
  <v-container class="d-flex flex-row ga-2">
  <v-card width="200px">
    <v-card-title class="text-center">{{report.usersCount}}</v-card-title>
    <v-card-text class="text-center"
    >{{$t('users')}}</v-card-text>
  </v-card>
  <v-card width="200px">
    <v-card-title class="text-center">{{report.recipesCount}}</v-card-title>
    <v-card-text class="text-center"
    >{{$t('recipes')}}</v-card-text>
  </v-card>
  <v-card width="200px">
    <v-card-title class="text-center">{{report.likesCount}}</v-card-title>
    <v-card-text class="text-center"
    >{{$t('likes')}}</v-card-text>
  </v-card>
  <v-card width="200px">
    <v-card-title class="text-center">{{report.cookbooksCount}}</v-card-title>
    <v-card-text class="text-center"
    >{{$t('cookbooks')}}</v-card-text>
  </v-card>
    <v-card width="200px">
      <v-card-title class="text-center">{{report.followsCount}}</v-card-title>
      <v-card-text class="text-center"
      >{{$t('follows')}}</v-card-text>
    </v-card>
  </v-container>
</template>

<script lang="ts" setup>
import {getReport} from "@/scripts/dashboard";
import {useI18n} from "vue-i18n";
const {t} = useI18n()

const report = ref<object>({
  usersCount: null,
  recipesCount: null,
  likesCount: null,
  cookbooksCount: null,
  followsCount: null,
})

getReport().then(response => {
  report.value = response.data
  console.log(report.value)
})
</script>
