<template>

  <v-card
    class="mx-auto rounded-xl pa-5 ma-auto my-5 d-flex flex-row"
    max-width="1000px"
  >
    <v-icon color="black" class="text-h3 mr-5 ml-3 mt-2" >mdi-alert</v-icon>
    <v-card-title prepend-icon="mdi-alert" class="text-black text-h4">
      This site is currently under maintenance
    </v-card-title>
  </v-card>

</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router';

// Get the route object
const route = useRoute();


</script>
