<script setup lang="ts">
import {searchIngredients} from "@/scripts/ingredients";
import router from "@/router";
import {useAuthStore} from "@/stores/auth";
import {useI18n} from "vue-i18n";
import {ICON_ALPHABETICAL, ICON_DATE, ICON_LIKES, ICON_RANDOM} from "@/scripts/icons";

const autocompleteList = ref([])

const selectedDishType = ref([0, 1, 2])
const selectedSource = ref([0,1])
const selectedIngredients = ref([])
const { t } = useI18n();

const authStore = useAuthStore()

const dishOptions = ref([
  {label: t('entree'), value: "ENTREE"},
  {label: t('plat'), value: "MAIN_DISH"},
  {label: t('desert'), value: "DESERT"},
  {label: t('other'), value: "OTHER"},
])

const sourceOptions = ref([
  {label: t('my_recipes'), value: "user"},
  {label: t('likes'), value: "likedBy"},
  {label: t('cookbooks'), value: "userCookbooks"},
  {label: t('follows'), value: "followedBy"},
])

const sortOptions = ref([
  { label: t('alphabetical'), value: "NAME", icon:ICON_ALPHABETICAL, ordered: true },
  { label: t('random'), value: "RANDOM", icon:ICON_RANDOM, ordered: false },
  { label: t('likes'), value: "LIKES", icon:ICON_LIKES, ordered: true },
  { label: t('date'), value: "DATE", icon:ICON_DATE, ordered: true },
]);

const selectedSort = ref(null); // Stores the selected sort field
const sortOrder = ref("desc"); // Default order is descending

const selectDishClass = (index) => {
  if (selectedDishType.value.includes(index)) {
    selectedDishType.value.splice(selectedDishType.value.indexOf(index),1)
  } else {
    selectedDishType.value.push(index)
  }
  updateUrl()
}

const toggleSort = (field) => {

  if (selectedSort.value === "RANDOM" && sortOrder.value === "desc") {
    selectedSort.value = null;
  } else if (selectedSort.value === field) {
    if (sortOrder.value === "desc") {
      sortOrder.value = "asc"; // Toggle to ascending
    } else {
      selectedSort.value = null; // Unselect the chip
    }
  } else {
    selectedSort.value = field;
    sortOrder.value = "desc"; // Default to descending when newly selected
  }
  updateUrl()
};

const getChipColor = (field) => {
  if (selectedSort.value === field) {
    return sortOrder.value === "desc" ? "primary" : "primary";
  }
  return "background"; // Default color when unselected
};

const getDishClassChipColor = (index) => {
  if (selectedDishType.value.includes(index)) {
    return sortOrder.value === "desc" ? "primary" : "primary";
  }
  return "background"; // Default color when unselected
};

const onIngredientAutocompleteChange = async (query) => {
  const response = await searchIngredients(query, 0, 20);
  autocompleteList.value = response.data.map(item => ({ id: item.id, name: item.name }));
}

const updateUrl = () => {
  const route = router.currentRoute
  let selectedSortOrder = sortOrder.value == "asc" ? "_ASCENDING" : "_DESCENDING"
  if (selectedSort.value == "RANDOM") {
    selectedSortOrder = ""
  }
  console.log(selectedSort.value)
  router.push({
    path: route.path, // Keep the current path
    query: Object.fromEntries(
      Object.entries({
        ...route.query,
        user: selectedSource.value.includes(0) ? authStore.id : undefined,
        likedBy: selectedSource.value.includes(1) ? authStore.id : undefined,
        cookbookUser: selectedSource.value.includes(2) ? authStore.id : undefined,
        followedBy: selectedSource.value.includes(3) ? authStore.id : undefined,
        dishClasses: selectedDishType.value.length > 0 ? selectedDishType.value.map(it => dishOptions.value[it].value).join(",") : undefined,
        ingredient: selectedIngredients.value.length > 0 ? selectedIngredients.value.map(it => it.id).join(",") : undefined,
        sort: selectedSort.value != null ? selectedSort.value + selectedSortOrder  : undefined
      }).filter(([_, value]) => value !== undefined) // Remove undefined values
    ),
  })
}
</script>

<template>
  <v-card
    class="ma-5 py-2 px-4"
    rounded="xl"
  >

    <v-row class="d-flex justify-space-between align-start">
      <!-- Left Section: Filters & Input -->
      <v-col class="d-flex flex-column">
        <v-chip-group
          v-model="selectedSource"
          column
          multiple
          @update:modelValue="updateUrl"
          color="surface"
          variant="flat"
        >
          <v-chip
            v-for="s in sourceOptions"
            :text="s.label"
            filter
          ></v-chip>
        </v-chip-group>

        <v-row class="d-flex flex-row ma-1">
          <v-chip
            v-for="(dish, index) in dishOptions"
            :text="dish.label"
            filter
            class="mx-1"
            :color="getDishClassChipColor(index)"
            variant="elevated"
            @click="selectDishClass(index)"
            :prepend-icon="selectedDishType.includes(index) ? 'mdi-check' : undefined"
          ></v-chip>
        </v-row>


        <v-combobox
          v-model="selectedIngredients"
          class="flex-grow-1 ma-1"
          color="primary"
          :items="autocompleteList"
          item-color="primary"
          item-title="name"
          item-value="id"
          @update:search="(query) => onIngredientAutocompleteChange(query)"
          return-object
          multiple
          chips
          closable-chips
          clearable
          @update:modelValue="updateUrl"
          :label="`${$t('ingredients')}`"
          max-width="400px"
          variant="solo-filled"
          rounded="lg"
        >
          <template v-slot:selection="data">
            <v-chip
              size="small"
              color="primary"
              variant="flat"
            >
              {{ data.item.title }}
            </v-chip>
          </template>
        </v-combobox>

      </v-col>

      <!-- Right Section: Sorting Chips -->
      <v-col class="d-flex flex-column align-end align-self-center" cols="auto">
        <v-chip
          v-for="option in sortOptions"
          :key="option.value"
          :color="getChipColor(option.value)"
          :variant="'elevated'"
          @click="toggleSort(option.value)"
          :prepend-icon="option.icon"
          class="ma-1"
        >
          {{ option.label }}
          <v-icon v-if="selectedSort === option.value && option.ordered">
            {{ sortOrder === 'desc' ? 'mdi-arrow-down' : 'mdi-arrow-up' }}
          </v-icon>
        </v-chip>
      </v-col>
    </v-row>
  </v-card>
</template>

<style scoped>



</style>
