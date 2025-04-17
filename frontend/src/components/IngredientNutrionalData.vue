<script setup lang="ts">
import {useI18n} from "vue-i18n";

const {t} = useI18n()

const props = defineProps({
  ingredient: {
    type: Object,
    required: true,
  },
})



</script>

<template>
  <v-table
    style="width: 1000px"
    fixed-header
    class="rounded-0"
    density="compact"
  >
    <thead>
    <tr>
      <th class="text-left">Value</th>
      <th v-if="ingredient.allowAmount" class="text-left">Per unit</th>
      <th v-if="ingredient.allowWeight" class="text-left">Per 100g</th>
      <th v-if="ingredient.allowVolume" class="text-left">Per 100mL</th>
    </tr>
    </thead>
    <tbody>
    <tr>
      <th class="text-left">
        {{$t('calories')}}
      </th>
      <th class="text-center">
        {{ingredient.calories}}
      </th>
    </tr>

    <tr>
      <th class="text-left">
        {{$t('glucids')}}
      </th>
      <th class="text-center">
        {{ingredient.glucids}}
      </th>
    </tr>

    <tr>
      <th class="text-left">
        {{$t('cholesterol')}}
      </th>
      <th class="text-center">
        {{ingredient.cholesterol}}
      </th>
    </tr>

    <tr>
      <th class="text-left">
        {{$t('lipids')}}
      </th>
      <th class="text-center">
        {{ingredient.lipids}}
      </th>
    </tr>

    <tr>
      <th class="text-left">
        {{$t('fibers')}}
      </th>
      <th class="text-center">
        {{ingredient.fibers}}
      </th>
    </tr>

    <tr>
      <th class="text-left">
        {{$t('proteins')}}
      </th>
      <th class="text-center">
        {{ingredient.proteins}}
      </th>
    </tr>

    <tr>
      <th class="text-left">
        {{$t('sodium')}}
      </th>
      <th class="text-center">
        {{ingredient.sodium}}
      </th>
    </tr>

    </tbody>
  </v-table>
</template>

<style scoped>

</style>
