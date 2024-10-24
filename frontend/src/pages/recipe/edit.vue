<template>
  <v-card
  class="mx-auto rounded-xl pa-5 ma-auto my-5"
  max-width="1000px"
  >


    <form @submit.prevent="submit" class="mx-auto">
      <v-text-field
        v-model="title"
        label="Title"
        class="mx-auto px-3"
      ></v-text-field>

      <v-textarea
        v-model="description"
        label="Description"
        class="mx-auto px-3"
      ></v-textarea>

      <v-text-field
        v-model="servings"
        label="Yield"
        class="mx-auto px-3"
        type="number"
      ></v-text-field>

      <h2 class="my-3" >Ingredients</h2>

      <draggable v-model="ingredients" tag="div" ghost-class="ghost" item-key="index" handle=".drag-handle">
        <template #item="{ element, index }">
          <div class="d-flex align-center mb-2">
            <!-- Add a handle for dragging -->
            <v-icon
              class="mr-2 drag-handle mb-5"
              color="primary"
              small
            >mdi-drag</v-icon>

            <v-text-field
              v-model="ingredients[index].name"
              :label="`Ingredient ${index + 1}`"
              outlined
              class="flex-grow-1"
            ></v-text-field>

            <v-select
              v-model="ingredients[index].unit"
              label="Unit"
              outlined
              class="flex-grow-1 mx-2"
              :items="['unit','g','lb','teaspoon', 'sugarspoon', 'cup']"
              variant="outlined"
              max-width="200px"
            ></v-select>

            <v-text-field
              v-model="ingredients[index].amount"
              label="Amount"
              outlined
              class="flex-grow-1"
              type="number"
              max-width="100px"
            ></v-text-field>

            <div>
              <v-btn
                @click="removeIngredient(index)"
                icon="mdi-delete"
                color="primary"
                class="rounded-lg ml-4 mb-5"
              ></v-btn>
            </div>


          </div>
        </template>
      </draggable>

      <!-- Button to add a new item -->
      <v-btn
        @click="addIngredient"
        prepend-icon="mdi-plus-circle-outline"
        color="primary"
        flat
        rounded
        class="mb-10"
      >Add new ingredient</v-btn>

      <!-- Display the current list -->
      <div>
        <h3>Current Ingredients:</h3>
        <ul>
          <li v-for="(item, index) in ingredients" :key="index">{{ item.name }}</li>
        </ul>
      </div>

      <h2 class="my-3" >Steps</h2>

      <draggable v-model="steps" tag="div" ghost-class="ghost" item-key="index" handle=".drag-handle">
        <template #item="{ element, index }">
          <div class="d-flex align-center mb-2">
            <!-- Add a handle for dragging -->
            <v-icon
              class="mr-2 drag-handle mb-5"
              color="primary"
              small
            >mdi-drag</v-icon>

            <!-- Editable text field -->
            <v-text-field
              v-model="steps[index]"
              :label="`Step ${index + 1}`"
              outlined
              class="flex-grow-1"
            ></v-text-field>

            <v-btn
              @click="removeStep(index)"
              icon="mdi-delete"
              color="primary"
              class="rounded-lg ml-4 mb-5"
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
        rounded
        class="mb-10"
      >Add new step</v-btn>

      <!-- Display the current list -->
      <div>
        <h3>Current steps:</h3>
        <ul>
          <li v-for="(item, index) in steps" :key="index">{{ item }}</li>
        </ul>
      </div>

      <span class="d-flex align-center justify-center mb-2 ga-16">
        <v-btn
          @click="addStep"
          prepend-icon="mdi-close-circle-outline"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="300px"
        >Cancel</v-btn>
        <v-btn
          @click="addStep"
          prepend-icon="mdi-send"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="300px"
        >Validate</v-btn>
      </span>




    </form>
  </v-card>

</template>

<script lang="ts" setup>
import { ref } from 'vue';
import draggable from 'vuedraggable';

const title = ref<string>()
const description = ref<string>()
const servings = ref<number>()
const ingredients = ref<object[]>([
  {name:''}
]);
const steps = ref<string[]>(['']);

// Function to add a new item
const addStep = () => {
  steps.value.push('');
};

const addIngredient = () => {
  ingredients.value.push({name:''});
}

// Function to add a new item
const removeStep = (index) => {
  steps.value.splice(index,1);
};

const removeIngredient = (index) => {
  ingredients.value.splice(index,1);
}




</script>
<style scoped>
.ghost {
  opacity: 0.5;
}

.drag-handle {
  cursor: grab;
  display:flex;
  align-items: center;
}
</style>
