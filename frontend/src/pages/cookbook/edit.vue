<template>
  <v-card
  class="mx-auto pa-5 ma-auto my-5"
  max-width="1000px"
  >

    <form @submit.prevent="submit" class="mx-auto">

      <v-card class="my-2">
        <v-text-field
          v-model="cookbook.title"
          :label="`${$t('title')}`"
          single-line
        ></v-text-field>
      </v-card>

      <v-card class="my-2">
        <v-textarea
          v-model="cookbook.description"
          :label="`${$t('description')}`"
          single-line
        ></v-textarea>
      </v-card>


      <v-card class="my-2 flex-grow-1" max-width="200px">
      <v-select
        v-model="cookbook.visibility"
        label="Visibility"
        :items="['PRIVATE','PROTECTED','PUBLIC']"
      ></v-select>
      </v-card>

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
      <h2 class="my-3" >{{$t("users")}}</h2>
      <draggable v-model="members" tag="div" ghost-class="ghost" item-key="index" handle=".drag-handle">
        <template #item="{ element, index }">
          <div class="d-flex align-center mb-2">
            <!-- Add a handle for dragging -->
            <v-icon
              class="mr-2 drag-handle"
              color="black"
              small
            >mdi-drag</v-icon>

            <v-avatar size="50" variant="elevated" style="border:3px solid #0d1821 !important;" class="mr-2">
              <v-img
                color="surface-variant"
                :src="getUserIconUrl(members[index].id)"
                cover
                v-bind="props"
                class="clickable_image"
              ></v-img>
            </v-avatar>

            <v-card class="flex-grow-1 mr-2">
            <v-autocomplete
              v-model="members[index].id"
              :label="`${$t('user')} ${index + 1}`"
              :items="autocompleteList[index]"
              item-color="primary"
              item-title="username"
              item-value="id"
              @update:search="(query) => onAutocompleteChange(query, index)"
              :key="index"
            ></v-autocomplete>
            </v-card>

            <v-card class="flex-grow-1" max-width="200px">
            <v-select
              v-model="members[index].role"
              :label="`${$t('role')}`"
              :items="['USER','ADMIN']"
            ></v-select>
            </v-card>

            <div>
              <v-btn
                @click="removeUser(index)"
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
        @click="addUser"
        prepend-icon="mdi-plus-circle-outline"
        color="primary"
        flat
        class="mb-10"
      >{{$t("users_add_new")}}</v-btn>

      <span class="d-flex align-center justify-center mb-6 mt-6 ga-16" >
        <action-button
          v-if="cookbookId"
          icon="mdi-close-circle-outline"
          :text="`${$t('cancel')}`"
          :action="() => toViewCookbook(cookbookId)"
        ></action-button>
        <action-button
          icon="mdi-content-save"
          :text="`${$t('save')}`"
          :action="submit"
        ></action-button>
      </span>
    </form>
  </v-card>

</template>

<script lang="ts" setup>
import { ref } from 'vue';
import draggable from 'vuedraggable';
import { useRoute } from 'vue-router';
import {getUserIconUrl, toViewCookbook} from "@/scripts/common";
import EditablePicture from "@/components/EditablePicture.vue";
import {searchUsers} from "@/scripts/users";
import {addRecipeToCookbook, createCookbook, editCookbook, getCookbook, getCookbookUsers} from "@/scripts/cookbooks";



// Get the route object
const route = useRoute();
let cookbookId = ref(route.query.cookbook)
let addRecipeId = route.query.addRecipe
const editablePicture = ref(null)


const autocompleteList = ref([])

const onAutocompleteChange = async (query, index) => {
  const response = await searchUsers(query, 0, 20);
  autocompleteList.value[index] = response.data;
}


const cookbook = ref<object>({
  title: "",
  description: "",
})

const members = ref([])



const addUser = () => {
  members.value.push({username: "",role: "USER"});
}


const removeUser = (index) => {
  members.value.splice(index,1);
}


async function submit() {
  console.log(members.value)
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

  toViewCookbook(cookbookId.value)
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

  getCookbookUsers(cookbookId.value).then(
    function (response) {
      members.value = response.data
    }
  )
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
