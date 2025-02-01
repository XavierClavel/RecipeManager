<template>
  <v-card
    class="mx-auto rounded-xl pa-5 ma-auto my-5 d-flex flex-row"
    max-width="1000px"
    v-if="displayError"
  >
    <v-icon color="primary" class="text-h3 mr-5 ml-3 mt-2" >mdi-alert</v-icon>
    <v-card-title prepend-icon="mdi-alert" class="text-primary text-h4">
      {{ errorMessage }}
    </v-card-title>
  </v-card>

  <v-card
  class="mx-auto rounded-xl pa-5 ma-auto my-5"
  max-width="1000px"
  v-if="!displayError"
  >
    <v-container class="d-flex flex-row">
      <editable-picture
        path="image/users"
        :id="userId"
        rounded="circle"
        height="200px"
        width="200px"
        buttons-size="50px"
        buttons-icon-size="text-h7"
        buttons-spacing="ga-4"
        buttons-rounded="lg"
        ref="editablePicture"
      ></editable-picture>
      <v-container>
        <v-card-title
          class="mx-auto px-3 text-primary text-h3"
        >{{ user.username }}</v-card-title>
      </v-container>

    </v-container>

    <v-textarea
        v-model="user.bio"
        label="Description"
        class="mx-auto px-3"
        color="primary"
    ></v-textarea>

    <span class="d-flex align-center justify-center mb-6 mt-6 ga-16" >
      <action-button
        icon="mdi-close-circle-outline"
        :text="`${$t('cancel')}`"
        :action="() => toViewUser(userId)"
      ></action-button>
      <action-button
        icon="mdi-content-save"
        :text="`${$t('save')}`"
        :action="submit"
      ></action-button>
      </span>



  </v-card>

</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router';
import {ref} from "vue";
import {toViewUser} from "@/scripts/common";
import {getUser, updateUser} from "@/scripts/users";
import EditablePicture from "@/components/EditablePicture.vue";

// Get the route object
const route = useRoute();
const userId = route.query.user
let displayError = ref<Boolean>(false)
const errorMessage = ref<String>("This user does not exist")
const editablePicture = ref(null)

const user = ref<object>({})

getUser(userId).then (
  function (response) {
    console.log(response)
    user.value = response.data
  }).catch(function (error) {
    displayError.value = true
  console.log(error);
    console.log(displayError)
})

async function submit() {
  const submitted = {}
  submitted["username"] = user.value.username
  submitted["bio"] = user.value.bio
  console.log(submitted)
  await updateUser(userId, submitted)
  await editablePicture.value.submitImage()
  toViewUser(userId)
}

</script>
