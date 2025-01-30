<template>
  <v-card
  class="mx-auto rounded-xl pa-5 ma-auto my-5"
  max-width="1000px"
  >

    <form @submit.prevent="submit" class="mx-auto">
      <v-text-field
        v-model="cookbook.title"
        label="Title"
        class="mx-auto px-3"
        color="primary"
      ></v-text-field>

      <v-textarea
        v-model="cookbook.description"
        label="Description"
        class="mx-auto px-3"
        color="primary"
      ></v-textarea>


      <v-select
        v-model="cookbook.visibility"
        label="Visibility"
        outlined
        class="flex-grow-1 mx-2"
        :items="['PRIVATE','PROTECTED','PUBLIC']"
        variant="outlined"
        max-width="200px"
        color="primary"
      ></v-select>

      <v-icon>
        <v-tooltip
          activator="parent"
          location="end"
        >Tooltip</v-tooltip>
        mdi-information
      </v-icon>



       <editable-picture
         path="image/cookbooks"
         :id="cookbookId"
         ref="editablePicture"
         width="600px"
         height="600px"
       ></editable-picture>

      <!-- Ingredients -->
      <h2 class="my-3" >Users</h2>
      <draggable v-model="cookbook.users" tag="div" ghost-class="ghost" item-key="index" handle=".drag-handle">
        <template #item="{ element, index }">
          <div class="d-flex align-center mb-2">
            <!-- Add a handle for dragging -->
            <v-icon
              class="mr-2 drag-handle mb-5"
              color="primary"
              small
            >mdi-drag</v-icon>

            <v-autocomplete
              v-model="cookbook.users[index].username"
              :label="`User ${index + 1}`"
              class="flex-grow-1"
              color="primary"
              :items="autocompleteList[index]"
              item-color="primary"
              item-title="name"
              item-value="id"
              @update:search="(query) => onIngredientAutocompleteChange(query, index)"
              :key="index"
            ></v-autocomplete>

            <v-select
              v-model="cookbook.users[index].role"
              label="Role"
              outlined
              class="flex-grow-1 mx-2"
              :items="['READER','WRITER','OWNER']"
              variant="outlined"
              max-width="200px"
              color="primary"
            ></v-select>

            <div>
              <v-btn
                @click="removeUser(index)"
                icon="mdi-delete"
                color="primary"
                class="rounded-lg ml-4 mb-5"
              ></v-btn>
            </div>


          </div>
        </template>
      </draggable>

      <!-- Button to add ingredient -->
      <v-btn
        @click="addUser"
        prepend-icon="mdi-plus-circle-outline"
        color="primary"
        flat
        rounded
        class="mb-10"
      >Add new user</v-btn>

      <span class="d-flex align-center justify-center mb-2 mt-16 ga-16" >
        <v-btn
          v-if="cookbookId != null"
          prepend-icon="mdi-close-circle-outline"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="300px"
          @click="toViewCookbookRecipes(cookbookId)"
        >Cancel</v-btn>
        <v-btn
          @click="submit"
          prepend-icon="mdi-content-save"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="300px"
        >Save</v-btn>
      </span>
    </form>
  </v-card>

</template>

<script lang="ts" setup>
import { ref } from 'vue';
import draggable from 'vuedraggable';
import { useRoute } from 'vue-router';
import {getRecipe, createRecipe, uploadRecipeImage, deleteRecipeImage, updateRecipe} from "@/scripts/recipes";
import {base_url, toViewCookbookRecipes, toViewRecipe} from "@/scripts/common";
import {searchIngredients} from "@/scripts/ingredients";
import EditablePicture from "@/components/EditablePicture.vue";
import {searchUsers} from "@/scripts/users";
import {addRecipeToCookbook, createCookbook, editCookbook, getCookbook} from "@/scripts/cookbooks";



// Get the route object
const route = useRoute();
let cookbookId = ref(route.query.cookbook)
let addRecipeId = route.query.addRecipe
const editablePicture = ref(null)


const autocompleteList = ref([])

const onIngredientAutocompleteChange = async (query, index) => {
  const response = await searchUsers(query, 0, 20);
  autocompleteList.value[index] = response.data.map(item => ({ id: item.id, name: item.username }));
}


const cookbook = ref<object>({
  title: "",
  description: "",
  users: [],
})



const addUser = () => {
  cookbook.value.users.push({});
}


const removeUser = (index) => {
  cookbook.value.users.splice(index,1);
}


async function submit() {
  const submitted = {}
  submitted["title"] = cookbook.value.title
  submitted["description"] = cookbook.value.description
  console.log(submitted)
  if (cookbookId.value == null) {
    const response = await createCookbook(submitted)
    cookbookId.value = response.data.id
    if (addRecipeId != null) {
      await addRecipeToCookbook(cookbookId.value, addRecipeId)
    }
    await nextTick()
  } else {
    await editCookbook(cookbookId.value, submitted)
  }

  await editablePicture.value.submitImage()

  toViewCookbookRecipes(cookbookId.value)
}

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
