<template>
  <v-layout class="mt-6">

      <v-card class="d-flex flex-column pa-0 ma-0" width="100%" color="transparent" variant="flat" style="border:0 !important">
        <v-card-title class="text-h7">
          Ingredients
          <v-btn
            color="black"
            class="ma-4 ml-6 text-h2 text-black"
            rounded="lg"
            height="100px"
            width="100px"
            @click="newIngredient"
            icon="mdi-plus"
            variant="outlined"
            style="border: 5px solid #0d1821 !important;"
          ></v-btn>
        </v-card-title>

      <v-card width="100%" max-width="1500px"  class="ma-5">

        <v-slide-x-reverse-transition v-show="isPanelOpen">
          <v-sheet class="pa-4 right-panel" elevation="3" z-index="50" style='z-index:20001;'>
            <v-btn icon @click="isPanelOpen = false">
              <v-icon>mdi-close</v-icon>
            </v-btn>
            <v-card-title>
              {{ action }}
            </v-card-title>
            <v-card class="my-2">
            <v-text-field
              v-model="selectedIngredient.name_fr"
              label="Name FR"
            ></v-text-field>
            </v-card>

            <v-card class="my-2">
              <v-text-field
                v-model="selectedIngredient.name_en"
                label="Name EN"
              ></v-text-field>
            </v-card>

            <v-card class="my-2">
            <v-select
              v-model="selectedIngredient.type"
              label="Type"
              :items="ingredientTypes"
            ></v-select>
            </v-card>

            <v-card class="my-2">
            <v-number-input
              v-model="selectedIngredient.calories"
              label="Calories"
              type="number"
              :min=0
              :step="10"
            ></v-number-input>
            </v-card>

            <v-card class="my-2">
            <v-number-input
              v-model="selectedIngredient.glucids"
              label="Glucids"
              type="number"
              color="primary"
              :min=0
              :step="1"
            ></v-number-input>
            </v-card>

            <v-card class="my-2">
            <v-number-input
              v-model="selectedIngredient.lipids"
              label="Lipids"
              type="number"
              color="primary"
              :min=0
              :step="1"
            ></v-number-input>
            </v-card>

            <v-card class="my-2">
            <v-number-input
              v-model="selectedIngredient.fibers"
              label="Fibers"
              type="number"
              color="primary"
              :min=0
              :step="1"
            ></v-number-input>
            </v-card>

            <v-card class="my-2">
            <v-number-input
              v-model="selectedIngredient.cholesterol"
              label="Cholesterol"
              type="number"
              color="primary"
              :min=0
              :step="1"
            ></v-number-input>
            </v-card>

            <v-card class="my-2">
            <v-number-input
              v-model="selectedIngredient.sodium"
              label="Sodium"
              type="number"
              color="primary"
              :min=0
              :step="1"
            ></v-number-input>
            </v-card>
            <span class="d-flex flex-row align-center justify-center mb-2 mt-16 ga-16" >
        <v-btn
          icon="mdi-close-circle-outline"
          color="primary"
          flat
          rounded="lg"
          class="mb-10 text-h6"
          min-height="70px"
          min-width="70px"
          @click="cancel"
        ></v-btn>
        <v-btn
          v-if="selectedIngredient.id != undefined"
          :icon="`${ICON_DELETE}`"
          color="primary"
          flat
          rounded="lg"
          class="mb-10 text-h6"
          min-height="70px"
          min-width="70px"
          @click="performDelete(selectedIngredient.id)"
        ></v-btn>
        <v-btn
          @click="save"
          icon="mdi-content-save"
          color="primary"
          flat
          rounded="lg"
          class="mb-10 text-h6"
          min-height="70px"
          min-width="70px"
        ></v-btn>
      </span>
          </v-sheet>
        </v-slide-x-reverse-transition>

        <v-table
            min-height="300px"
            fixed-header
            class="rounded-0"
          >
            <thead>
            <tr>
              <th class="text-left">
                Ingredient
              </th>
              <th class="text-left">
                Type
              </th>
              <th class="text-left">
                Calories
              </th>
            </tr>
            </thead>
            <tbody>
            <tr
              v-for="ingredient in ingredients"
              :key="ingredient.username"
              @click="editIngredient(ingredient)"
              :class="{'primary': ingredient.id == 3  }"
              :style="{
                backgroundColor: isPanelOpen && ingredient && ingredient.id === selectedIngredient.id ? theme.current.value.colors.secondary : '',
              }"
            >
              <td>{{ ingredient.name_en }}</td>
              <td>{{ ingredient.type }}</td>
              <td>{{ ingredient.calories }}</td>
            </tr>
            </tbody>
          </v-table>
        <v-container>
          <v-row justify="center">
            <v-col cols="6">
              <v-container class="max-width">
                <v-pagination
                  v-model="page"
                  :length="pagesCount"
                  class="my-4"
                  active-color="primary"
                  @update:modelValue="updateDisplay"
                ></v-pagination>
              </v-container>
            </v-col>
          </v-row>
        </v-container>
      </v-card>
      </v-card>
  </v-layout>
</template>

<script lang="ts" setup>
import {createIngredient, deleteIngredient, getCount, searchIngredients, updateIngredient} from "@/scripts/ingredients";
import { useTheme } from 'vuetify'
import {ICON_DELETE} from "@/scripts/icons";
import {toCreateCookbook} from "@/scripts/common";

const theme = useTheme()

const ingredients = ref<object[]>([])
const selectedIngredient = ref({})
const page = ref<number>(1)
const pagesCount = ref<number>(1)
const isPanelOpen = ref<boolean>(false)
const ingredientTypes = [
  "CHEESE",
  "VEGETABLE",
  "MEAT",

]
const action = ref<string>("Create ingredient")

async function save() {
  const { id, ...ingredientToUpdate} = selectedIngredient.value
  if (selectedIngredient.value.id == undefined) {
    await createIngredient(ingredientToUpdate)
  } else {
    await updateIngredient(selectedIngredient.value.id, ingredientToUpdate)
  }
  updateDisplay()
  cancel()
}

const cancel = () => {
  if (selectedIngredient != {}) {
    let {...copy} = selectedIngredient.value;
    copy.id = -1
    selectedIngredient.value = copy
  }


  isPanelOpen.value = false
}

const newIngredient = () => {
  isPanelOpen.value = true
  selectedIngredient.value = {}
  action.value = "Create ingredient"
}

const editIngredient = (ingredientToEdit) => {
  if (selectedIngredient.value.id == ingredientToEdit.id) {
    cancel()
  } else {
    isPanelOpen.value = true
    selectedIngredient.value = ingredientToEdit
    action.value = `Edit ingredient "${ingredientToEdit.name_en}"`
  }

}

const performDelete = (id) => {
  deleteIngredient(id).then(
    function (response) {
      if (response.status == 200) {
        const index = ingredients.value.findIndex((user) => user.id == id)
        ingredients.value.splice(index,1)
      }
    }).catch(function (error) {
    console.log(error);
  }).finally(function () {
    // always executed
  });

};

const updateDisplay = () => {
  searchIngredients("",page.value - 1, 20).then (
    function (response) {
      ingredients.value = response.data
    }).catch(function (error) {
    console.log(error);
  })
}

updateDisplay()


getCount().then (
  function (response) {
    pagesCount.value = Math.ceil(response.data / 20)
  }).catch(function (error) {
  console.log(error);
})


</script>

<style scoped>
/* Styling to make the overlay appear as a fixed panel on the right */
.right-panel {
  position: fixed;
  top: 0;
  right: 0;         /* Sticks the panel to the right side */
  height: 100vh;    /* Full vertical height */
  width: 400px;
  overflow-y: auto; /* Enables scrolling within the panel if content is long */
}
</style>
