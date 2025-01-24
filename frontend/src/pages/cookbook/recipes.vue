<template>

  <v-card
    class="rounded-xl pa-5 ma-5"
    v-if="!displayError"
  >
    <v-container class="d-flex flex-row">
      <v-img
        color="surface-variant"
        :src="imageUrl"
        rounded="lg"
        height="200px"
        width="200px"
        cover
      ></v-img>
      <v-container
      class="px-3 mx-auto"
      >
        <v-card-title
          class="text-primary text-h3"
        >{{ cookbook.title }}</v-card-title>
        <v-card-text>
          {{cookbook.description}}
        </v-card-text>
        <v-row class="px-3">
          <v-btn
          icon="mdi-cog"
          flat
          @click="toEditCookbook(cookbookId)"
          ></v-btn>
        </v-row>
      </v-container>

    </v-container>
  </v-card>
  <recipes-list></recipes-list>
</template>

<script lang="ts" setup>
import {useRoute} from "vue-router";
import {ref} from "vue";
import InteractiblePictoInfo from "@/components/InteractiblePictoInfo.vue";
import {base_url, toEditCookbook, toEditUser} from "@/scripts/common";
import {getCookbook} from "@/scripts/cookbooks";

const route = useRoute();
let cookbookId = ref(route.query.cookbook)
const imageUrl = computed(() => `${base_url}/image/cookbooks/${cookbookId.value}.webp`);

const cookbook = ref<object>({
  title: "",
  description: "",
  users: [],
})


if (cookbookId.value != null) {
  getCookbook(cookbookId.value).then (
    function (response) {
      cookbook.value.title = response.data.title
      cookbook.value.description = response.data.description
      console.log(response.data)
    }).catch(function (error) {
    console.log(error);
  }).finally(function () {
    // always executed
  });
}

</script>
