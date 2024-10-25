<template>
  <v-layout class="rounded rounded-md">

    <v-main class="d-flex align-center justify-center" style="min-height: 300px;" >
      <v-card height="250px" width="200px" s>
        <v-card-title class="text-primary">
          Friends
        </v-card-title>
        <v-list lines="one" style="overflow-y: auto" max-height="180px">
        <v-list-item v-for="friend in friends" class="my-n2">
          <v-btn class="" variant="plain" compact>{{friend}}</v-btn>
        </v-list-item>
        </v-list>
      </v-card>
      <v-card height="250px" width="200px" s>
        <v-card-title class="text-primary">
          Circles
        </v-card-title>
        <v-list lines="one" style="overflow-y: auto" max-height="180px">
          <v-list-item v-for="circle in circles" class="my-n2">
            <v-btn class="" variant="plain" compact>{{circle}}</v-btn>
          </v-list-item>
        </v-list>
      </v-card>

    </v-main>
  </v-layout>
</template>

<script lang="ts" setup>
const friends = [
  "Léo",
  "Alizée",
  "Edouard",
  "Côme",
  "Jasmine",
  "Sherryl"
]
const circles = [
  "Brazil Billard",
  "Famille Clavel",
]
</script>
