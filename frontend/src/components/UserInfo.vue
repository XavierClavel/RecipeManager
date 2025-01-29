<script setup lang="ts">
import {getRecipeIconUrl, getUserIconUrl, toViewRecipe, toViewUser} from "@/scripts/common";

defineProps({
  user: {
    type: Object,
    required: true,
  },
})
</script>

<template>
    <v-container>
      <v-chip v-bind="props" link pill @click="toViewUser(user.id)" class="">
        <v-avatar start>
          <v-img :src="getUserIconUrl(user.id)"></v-img>
        </v-avatar>
        {{ user.username }}
      </v-chip>
    </v-container>
</template>

<style scoped>

</style>
