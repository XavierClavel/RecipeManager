<template>
  <v-card
  class="mx-auto pa-5 ma-auto my-5"
  max-width="1000px"
  style="border: 4px solid #0d1821 !important;"
  >


    <v-form @submit.prevent="submit" class="mx-auto" ref="form">

      <v-text-field
        v-model="recipe.title"
        :label="`${$t('title')}`"
        :rules="[requiredRule, max100]"
      ></v-text-field>

      <v-textarea
        v-model="recipe.description"
        :label="`${$t('description')}`"
        :rules="[max255]"
      ></v-textarea>

          <v-btn-toggle
            v-model="recipe.dishClass"
            color="primary"
            bg-color="background"
            base-color="background"
            rounded="lg"
            group
            mandatory
            class="ga-1 my-1 ml-n3 flex-wrap"
            style="width: 100%;"
          >
            <v-btn
              v-for="dishClass in dishOptions"
              :key="dishClass.value"
              :value="dishClass.value"
              height="45px"
              class="ma-1"
            >
              {{$t(dishClass.label)}}
            </v-btn>
          </v-btn-toggle>
       <editable-picture
         v-if="ready"
         path="recipes"
         :id="recipeId"
         :version="recipe.version"
         ref="editablePicture"
         width="100%"
         aspect-ratio="4/3"
       ></editable-picture>


      <v-number-input
        v-model="recipe.yield"
        :label="`${$t('yield')}`"
        type="number"
        color="primary"
        :min=1
        :rules="[requiredRule]"
      ></v-number-input>

      <v-number-input
        v-model="recipe.preparationTime"
        :label="`${$t('time_preparation')}`"
        type="number"
        :step="5"
        :min=0
      ></v-number-input>

      <v-number-input
        v-model="recipe.cookingTime"
        :label="`${$t('time_cooking')}`"
        type="number"
        :step="5"
        :min=0
      ></v-number-input>

      <v-number-input
        v-model="recipe.cookingTemperature"
        :label="`${$t('cooking_temperature')}`"
        type="number"
        :step="5"
        :min=0
      >
      </v-number-input>

      <!-- Ingredients -->
      <h2 class="my-3 mt-12" >{{$t("ingredients")}}</h2>
      <draggable v-model="recipe.ingredients" tag="div" ghost-class="ghost" item-key="index" handle=".drag-handle">
        <template #item="{ element, index }">
          <div class="d-flex flex-wrap align-center mb-2">
            <!-- Handle for dragging -->
            <v-icon
              class="mr-2 drag-handle"
              color="black"
              small
            >mdi-drag</v-icon>

            <v-autocomplete
              v-model="recipe.ingredients[index].ingredient"
              :label="`${$t('ingredient')} ${index + 1}`"
              v-model:search="queryList[index]"
              color="primary"
              :items="autocompleteList[index]"
              item-color="primary"
              item-title="name"
              item-value="id"
              @update:search="(query) => onIngredientAutocompleteChange(query, index)"
              :key="index"
              return-object
              @keydown.enter.prevent="selectFirstMatch(index)"
              class="ma-1"
              min-width="150px"
            ></v-autocomplete>

            <v-select
              v-model="recipe.ingredients[index].unit"
              :label="`${$t('unit')}`"
              :items="getUnitOptions(recipe.ingredients[index])"
              color="primary"
              :item-title="getLocalizedLabel"
              item-value="value"
              return-object
              min-width="120px"
              class="ma-1"
            >
              <!-- Customize how items appear in the dropdown -->
              <template v-slot:item="{ props, item }">
                <v-list-item v-bind="props">
                  <template #prepend>
                    <v-icon>{{ item.raw.icon }}</v-icon>
                  </template>
                </v-list-item>
              </template>

              <!-- Customize how selected item appears -->
              <template v-slot:selection="{ item }">
                <v-icon start class="mr-2">{{ item.raw.icon }}</v-icon>
                {{ $t(item.raw.label) }}
              </template>
            </v-select>

            <v-number-input
              v-model="recipe.ingredients[index].amount"
              :label="`${$t('amount')}`"
              type="number"
              color="primary"
              control-variant="stacked"
              min=0
              item-title="amount"
              return-object
              class="ma-1"
              max-width="150px"
              v-if="recipe.ingredients[index].unit?.value != 'NONE'"
            ></v-number-input>

            <v-text-field
              v-model="recipe.ingredients[index].complement"
              :label="`${$t('complement')}`"
              :rules="[max50]"
              class="ma-1"
            ></v-text-field>

            <div>
              <v-btn
                @click="removeIngredient(index)"
                icon="mdi-delete"
                color="primary"
                class="ml-4"
              ></v-btn>
            </div>


          </div>
        </template>
      </draggable>

      <!-- Custom ingredients -->
      <h2 class="my-3 mt-12" v-if="recipe.customIngredients && recipe.customIngredients.length > 0">{{$t('custom_ingredients')}}</h2>
      <draggable v-model="recipe.customIngredients" ghost-class="ghost" item-key="index" handle=".drag-handle">
        <template #item="{ element, index }">
          <div class="d-flex flex-wrap align-center mb-2">
            <!-- Add a handle for dragging -->
            <v-icon
              class="mr-2 drag-handle"
              color="black"
              small
            >mdi-drag</v-icon>

            <v-text-field
              v-model="recipe.customIngredients[index].name"
              :label="`${$t('custom_ingredient')} ${index + 1}`"
              color="primary"
              item-color="primary"
              :key="index"
              :rules="[max50]"
              class="ma-1"
            ></v-text-field>

            <v-select
              v-model="recipe.customIngredients[index].unit"
              :label="`${$t('unit')}`"
              :items="unitOptions"
              color="primary"
              :item-title="getLocalizedLabel"
              item-value="value"
              return-object
              class="ma-1"
              max-width="200px"
            >
              <!-- Customize how items appear in the dropdown -->
              <template v-slot:item="{ props, item }">
                <v-list-item v-bind="props">
                  <template #prepend>
                    <v-icon>{{ item.raw.icon }}</v-icon>
                  </template>
                </v-list-item>
              </template>

              <!-- Customize how selected item appears -->
              <template v-slot:selection="{ item }">
                <v-icon start class="mr-2">{{ item.raw.icon }}</v-icon>
                {{ $t(item.raw.label) }}
              </template>
            </v-select>

            <v-number-input
              v-model="recipe.customIngredients[index].amount"
              :label="`${$t('amount')}`"
              type="number"
              color="primary"
              control-variant="stacked"
              min=0
              class="ma-1"
              max-width="150px"
              v-if="recipe.customIngredients[index].unit?.value != 'NONE'"
            >
            </v-number-input>

            <div>
              <v-btn
                @click="removeCustomIngredient(index)"
                icon="mdi-delete"
                color="primary"
                class="ml-4"
              ></v-btn>
            </div>


          </div>
        </template>
      </draggable>

      <!-- Button to add ingredient -->
      <v-btn
        @click="addIngredient"
        prepend-icon="mdi-plus-circle-outline"
        color="primary"
        flat
        class="mb-2 mr-2"
      >{{$t("ingredient")}}</v-btn>

      <!-- Button to add custom ingredient -->
      <v-tooltip :text="`${$t('custom_ingredients_warning')}`"
      location="bottom">
        <template v-slot:activator="{ props }">
          <v-btn
            v-bind="props"
            @click="addCustomIngredient"
            prepend-icon="mdi-plus-circle-outline"
            color="black"
            flat
            class="mb-2"
            variant="outlined"
          >{{$t("custom_ingredient")}}</v-btn>
        </template>
      </v-tooltip>


      <!-- Steps -->
      <h2 class="my-3" >{{$t("steps")}}</h2>
      <draggable v-model="recipe.steps" tag="div" ghost-class="ghost" item-key="index" handle=".drag-handle">
        <template #item="{ element, index }">
          <div class="d-flex align-center">
            <!-- Add a handle for dragging -->
            <v-icon
              class="mr-2 drag-handle"
              color="black"
              small
            >mdi-drag</v-icon>

            <v-text-field
              v-model="recipe.steps[index]"
              :label="`${$t('step')} ${index + 1}`"
              :id="`step_${index}`"
              :rules="[max255]"
              @keyup.enter="addStepAt(index)"
              @keyup.delete="deleteStepAt(index)"
            ></v-text-field>

            <v-btn
              @click="removeStep(index)"
              icon="mdi-delete"
              color="primary"
              class="ml-4"
              tabindex="-1"
            ></v-btn>
          </div>
        </template>
      </draggable>

      <!-- Button to add a new item -->
      <v-btn
        @click="addStep"
        prepend-icon="mdi-plus-circle-outline"
        color="primary"
        flat
        class="mb-10 mt-2"
      >{{$t("steps_add_new")}}</v-btn>

        <v-textarea
          v-model="recipe.tips"
          :label="`${$t('tips')}`"
          :rules="[max511]"
        ></v-textarea>

      <v-container>
        <v-row
          class="d-flex align-center justify-center align-content-center mb-2 ga-4"
          dense
        >
            <action-button
              v-if="recipeId"
              icon="mdi-close-circle-outline"
              :text="`${$t('cancel')}`"
              :action="() => toViewRecipe(recipeId)"
            ></action-button>
            <action-button
              icon="mdi-content-save"
              :text="`${$t('save')}`"
              :action="submit"
            ></action-button>
        </v-row>
      </v-container>
    </v-form>
  </v-card>

</template>

<script lang="ts" setup>
import { ref } from 'vue';
import draggable from 'vuedraggable';
import { useRoute } from 'vue-router';
import {getRecipe, createRecipe, updateRecipe} from "@/scripts/recipes";
import {defaultImageRecipe, toViewRecipe} from "@/scripts/common";
import {searchIngredients} from "@/scripts/ingredients";
import EditablePicture from "@/components/EditablePicture.vue";
import {dishOptions, unitOptions} from "@/scripts/values";
import {useI18n} from "vue-i18n";
import {getLocale} from "@/scripts/localization";
import {max100, max255, max50, max511, requiredRule} from "@/scripts/rules";
const {t} = useI18n()
const form = ref(null)


// Get the route object
const route = useRoute();
let recipeId = ref(route.query.id)
const ready = ref(false)
const editablePicture = ref(null)

const autocompleteList = ref([])
const queryList = ref([])

function getLocalizedLabel(item: any) {
  return t(item.label)
}

const onIngredientAutocompleteChange = async (query, index) => {
  queryList.value[index] = query
  const response = await searchIngredients(query, 0, 20);
  autocompleteList.value[index] = response.data.map(item => ({
    id: item.id,
    name: getLocale() == 'fr' ? item.name_fr : item.name_en,
    allowWeight: item.allowWeight,
    allowVolume: item.allowVolume,
    allowAmount: item.allowAmount,
  }));
}

function selectFirstMatch(index) {
  const query = queryList.value[index]
  const match = autocompleteList.value[index].find(opt =>
    opt.name.toLowerCase().includes(query?.toLowerCase())
  )

  if (match) {
    recipe.value.ingredients[index].ingredient = match
    queryList.value[index] = match.name // Update displayed input text
  }
}



const recipe = ref<object>({
  title: "",
  description: "",
  steps: [''],
  dishClass: "MAIN_DISH",
  ingredients: [],
  customIngredients: [],
  tips: "",
})

const getUnitOptions = (v) => {
  console.log(v)
  if (v == null) return unitOptions
  return unitOptions.value.filter(it =>
    it.type == "NONE" ||
    (v.ingredient.allowAmount && it.type == "AMOUNT") ||
    (v.ingredient.allowWeight && it.type == "WEIGHT") ||
    (v.ingredient.allowVolume && it.type == "VOLUME")
  )
}


// Function to add a new item
const addStep = () => {
  console.log(recipe.value)
  recipe.value.steps.push('');
};

const addIngredient = () => {
  recipe.value.ingredients.push({ingredient: '' ,complement: ""});
}

async function deleteStepAt(index) {
  console.log("called")
  console.log(recipe.value.steps[index])
  if (recipe.value.steps[index]) {
    return
  }
  recipe.value.steps.splice(index, 1);
  await nextTick()
  document.getElementById(`step_${index-1}`).focus()
}

async function addStepAt(index) {
  recipe.value.steps.splice(index+1, 0, "");
  await nextTick()
  document.getElementById(`step_${index + 1}`).focus()
}

const addCustomIngredient = () => {
  recipe.value.customIngredients.push({});
}

// Function to add a new item
const removeStep = (index) => {
  recipe.value.steps.splice(index,1);
};

const removeIngredient = (index) => {
  recipe.value.ingredients.splice(index,1);
}

const removeCustomIngredient = (index) => {
  recipe.value.customIngredients.splice(index,1);
}

async function submit() {
  const {valid, errors} = await form.value.validate()
  if (!valid) {
    return
  }
  const submitted = JSON.parse(JSON.stringify(recipe.value))
  submitted.ingredients = recipe.value.ingredients
    .filter((it) => it.ingredient )
    .map(item => ({
    id: item.ingredient.id,
    amount: item.unit == null || item.unit.value == "NONE" ? null : item.amount,
    unit: item.unit ? item.unit.value : "NONE",
    complement: item.complement}))
  submitted.customIngredients = submitted.customIngredients.filter((it) => "name" in it)
  submitted.steps = submitted.steps.filter((it) => it)
  console.log(submitted)
  if (recipeId.value == null) {
    const response = await createRecipe(submitted)
    recipeId.value = response.data.id
    await nextTick()
  } else {
    await updateRecipe(recipeId.value, submitted)
  }
  await editablePicture.value.submitImage()

  toViewRecipe(recipeId.value)
}

if (recipeId.value != null) {
  getRecipe(recipeId.value).then (
    function (response) {
      recipe.value.title = response.data.title
      recipe.value.description = response.data.description
      recipe.value.dishClass = response.data.dishClass
      recipe.value.steps = response.data.steps
      recipe.value.ingredients = response.data.ingredients.map(item => ({
        ingredient: {
          id: item.id,
          name: item.name,
          allowWeight: item.allowWeight,
          allowVolume: item.allowVolume,
          allowAmount: item.allowAmount,
        },
        amount: item.amount,
        unit: unitOptions.value.find(it => it.value == item.unit)
      }))
      recipe.value.customIngredients = response.data.customIngredients
      recipe.value.yield = response.data.yield
      recipe.value.preparationTime = response.data.preparationTime
      recipe.value.cookingTime = response.data.cookingTime
      recipe.value.cookingTemperature = response.data.cookingTemperature
      recipe.value.tips = response.data.tips
      recipe.value.version = response.data.version
      console.log(recipe.value)
      ready.value = true
    }).catch(function (error) {
    console.log(error);
  }).finally(function () {
    // always executed
  });
} else {
  ready.value = true
}



</script>

<style scoped>

.drag-handle {
  cursor: grab;
  display:flex;
  align-items: center;
}


</style>
