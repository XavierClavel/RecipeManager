<script setup lang="ts">
import {useI18n} from "vue-i18n";

const {t} = useI18n()

const props = defineProps({
  ingredient: {
    type: Object,
    required: true,
  },
})

const getValueAmount = v => {
  if (!props.ingredient.allowWeight) {
    return v
  }
  else {
    return (v * props.ingredient.weightPerUnit / 100).toFixed(2).replace(/[.,]00$/, '')
  }
}

const getValueWeight = v => {
  return v
}

const getValueVolume = v => {
  if (!props.ingredient.allowWeight) {
    return v
  }
  else {
    return (v / props.ingredient.volumicMass).toFixed(2).replace(/[.,]00$/, '')
  }
}



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
      <th v-if="ingredient.allowAmount" class="text-center">{{ $t("per_unit") }}</th>
      <th v-if="ingredient.allowWeight" class="text-center">{{ $t("per_weight")}}</th>
      <th v-if="ingredient.allowVolume" class="text-center">{{ $t("per_volume")}}</th>
    </tr>
    </thead>
    <tbody>
    <tr>
      <th class="text-left">
        {{$t('calories')}}
      </th>
      <th class="text-center" v-if="ingredient.allowAmount">
        {{ getValueAmount(ingredient.calories)}}
      </th>
      <th class="text-center" v-if="ingredient.allowWeight">
        {{ getValueWeight(ingredient.calories)}}
      </th>
      <th class="text-center" v-if="ingredient.allowVolume">
        {{ getValueVolume(ingredient.calories)}}
      </th>
    </tr>

    <tr>
      <th class="text-left">
        {{$t('carbohydrates')}}
      </th>
      <th class="text-center" v-if="ingredient.allowAmount">
        {{ getValueAmount(ingredient.carbohydrates)}}
      </th>
      <th class="text-center" v-if="ingredient.allowWeight">
        {{ getValueWeight(ingredient.carbohydrates)}}
      </th>
      <th class="text-center" v-if="ingredient.allowVolume">
        {{ getValueVolume(ingredient.carbohydrates)}}
      </th>
    </tr>

    <tr>
      <th class="text-left">
        {{$t('sugars')}}
      </th>
      <th class="text-center" v-if="ingredient.allowAmount">
        {{ getValueAmount(ingredient.sugars)}}
      </th>
      <th class="text-center" v-if="ingredient.allowWeight">
        {{ getValueWeight(ingredient.sugars)}}
      </th>
      <th class="text-center" v-if="ingredient.allowVolume">
        {{ getValueVolume(ingredient.sugars)}}
      </th>
    </tr>

    <tr>
      <th class="text-left">
        {{$t('fibers')}}
      </th>
      <th class="text-center" v-if="ingredient.allowAmount">
        {{ getValueAmount(ingredient.fibers)}}
      </th>
      <th class="text-center" v-if="ingredient.allowWeight">
        {{ getValueWeight(ingredient.fibers)}}
      </th>
      <th class="text-center" v-if="ingredient.allowVolume">
        {{ getValueVolume(ingredient.fibers)}}
      </th>
    </tr>

    <tr>
      <th class="text-left">
        {{$t('cholesterol')}}
      </th>
      <th class="text-center" v-if="ingredient.allowAmount">
        {{ getValueAmount(ingredient.cholesterol)}}
      </th>
      <th class="text-center" v-if="ingredient.allowWeight">
        {{ getValueWeight(ingredient.cholesterol)}}
      </th>
      <th class="text-center" v-if="ingredient.allowVolume">
        {{ getValueVolume(ingredient.cholesterol)}}
      </th>
    </tr>

    <tr>
      <th class="text-left">
        {{$t('unsaturatedFat')}}
      </th>
      <th class="text-center" v-if="ingredient.allowAmount">
        {{ getValueAmount(ingredient.unsaturatedFat)}}
      </th>
      <th class="text-center" v-if="ingredient.allowWeight">
        {{ getValueWeight(ingredient.unsaturatedFat)}}
      </th>
      <th class="text-center" v-if="ingredient.allowVolume">
        {{ getValueVolume(ingredient.unsaturatedFat)}}
      </th>
    </tr>

    <tr>
      <th class="text-left">
        {{$t('saturatedFat')}}
      </th>
      <th class="text-center" v-if="ingredient.allowAmount">
        {{ getValueAmount(ingredient.saturatedFat)}}
      </th>
      <th class="text-center" v-if="ingredient.allowWeight">
        {{ getValueWeight(ingredient.saturatedFat)}}
      </th>
      <th class="text-center" v-if="ingredient.allowVolume">
        {{ getValueVolume(ingredient.saturatedFat)}}
      </th>
    </tr>

    <tr>
      <th class="text-left">
        {{$t('proteins')}}
      </th>
      <th class="text-center" v-if="ingredient.allowAmount">
        {{ getValueAmount(ingredient.proteins)}}
      </th>
      <th class="text-center" v-if="ingredient.allowWeight">
        {{ getValueWeight(ingredient.proteins)}}
      </th>
      <th class="text-center" v-if="ingredient.allowVolume">
        {{ getValueVolume(ingredient.proteins)}}
      </th>
    </tr>

    <tr>
      <th class="text-left">
        {{$t('sodium')}}
      </th>
      <th class="text-center" v-if="ingredient.allowAmount">
        {{ getValueAmount(ingredient.sodium)}}
      </th>
      <th class="text-center" v-if="ingredient.allowWeight">
        {{ getValueWeight(ingredient.sodium)}}
      </th>
      <th class="text-center" v-if="ingredient.allowVolume">
        {{ getValueVolume(ingredient.sodium)}}
      </th>
    </tr>

    </tbody>
  </v-table>
</template>

<style scoped>

</style>
