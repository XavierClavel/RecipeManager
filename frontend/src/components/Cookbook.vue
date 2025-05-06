<script setup lang="ts">
import {getCookbookIconUrl, getUserIconUrl, toViewCookbook} from "@/scripts/common";

const props = defineProps({
  cookbook: {
    type: Object,
    required: true,
  },
})

console.log(props.cookbook.members.length)

for (i in props.cookbook.members.length) {
  console.log(i)
}
</script>

<template>
  <v-card
    class="ma-4"
    width="250px"
    @click="toViewCookbook(cookbook.id)"
  >
        <v-img
          color="surface-variant"
          height="250px"
          class="mt-n3"
          rounded="t-lg"
          :src="getCookbookIconUrl(cookbook.id, cookbook.version)"
          cover
        ></v-img>
        <v-card-title class="text-black text-h6 text-sm-h6 text-truncate font-weight-regular">{{ cookbook.title }}</v-card-title>
        <v-row class="px-6 pt-2 pb-5">
          <v-avatar
            start
            size="30"
            class="mx-n1"
            v-for="index in cookbook.members.length"
            style="border: 3px solid #0d1821 !important;"
            color="surface"
          >
            <v-img
              v-if="index < 10 || cookbook.members.length <= 10"
              :src="getUserIconUrl(cookbook.members[(index - 1)].id, cookbook.members[(index - 1)].version)"
            ></v-img>
            <v-icon v-else>mdi-dots-horizontal</v-icon>
          </v-avatar>
        </v-row>

    </v-card>

</template>

<style scoped>

</style>
