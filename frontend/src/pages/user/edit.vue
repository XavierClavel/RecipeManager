<template>
  <v-card
  class="mx-auto pa-5 ma-auto my-5"
  max-width="1000px"
  >
    <v-container class="d-flex flex-wrap justify-center
      flex-sm-nowrap">
      <editable-picture
        path="users"
        :id="userId"
        rounded="circle"
        height="200px"
        width="200px"
        aspect-ratio="1/1"
        max-width="200px"
        buttons-size="50px"
        buttons-icon-size="text-h7"
        buttons-spacing="ga-4"
        buttons-rounded="lg"
        ref="editablePicture"
      ></editable-picture>
      <v-container>
        <v-card-title
          class="mx-auto px-3 text-black text-h2 font-weight-bold text-center text-sm-left"
        >{{ user.username }}</v-card-title>
      </v-container>

    </v-container>

    <error :error="errorMessage"></error>

    <v-textarea
        v-model="user.bio"
        label="Description"
        :rules="[max255]"
    ></v-textarea>

    <v-container>
      <v-row
        class="d-flex align-center justify-center align-content-center mb-2 ga-4"
        dense
      >
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
      </v-row>
    </v-container>



  </v-card>

</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router';
import {ref} from "vue";
import {toEditUser, toViewUser} from "@/scripts/common";
import {getUser, updateUser} from "@/scripts/users";
import EditablePicture from "@/components/EditablePicture.vue";
import {max255} from "@/scripts/rules";

// Get the route object
const route = useRoute();
const userId = route.query.user
const errorMessage = ref(null)
const editablePicture = ref(null)

const user = ref<object>({})

getUser(userId).then (
  function (response) {
    console.log(response)
    user.value = response.data
  }).catch(function (error) {
    errorMessage.value = error.response.data
})

async function submit() {
  const submitted = {}
  submitted["username"] = user.value.username
  submitted["bio"] = user.value.bio
  console.log(submitted)
  await updateUser(submitted)
  await editablePicture.value.submitImage()
  toViewUser(userId)
}

</script>
