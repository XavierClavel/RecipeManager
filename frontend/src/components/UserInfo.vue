<script setup lang="ts">
import {getUserIconUrl, toViewUser} from "@/scripts/common";

defineProps({
  user: {
    type: Object,
    required: true,
  },
})
</script>

<template>
    <v-container>
      <v-chip link pill @click="toViewUser(user.id)" color="surface">
        <v-avatar start>
          <v-img :src="getUserIconUrl(user.id, user.version)"></v-img>
        </v-avatar>
        {{ user.username }}
      </v-chip>
    </v-container>
</template>

<style scoped>

</style>
