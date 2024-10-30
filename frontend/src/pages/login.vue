<template>
  <v-card
  class="mx-auto rounded-xl pa-5 ma-auto my-5"
  max-width="1000px"
  >


    <form @submit.prevent="submit" class="mx-auto">
      <v-card-title class="text-primary text-h3">
        Login
      </v-card-title>
      <v-text-field
        v-model="user.username"
        prepend-icon="mdi-account"
        label="Username"
        class="mx-auto px-3"
        color="primary"
        :rules="[rules.required]"
      ></v-text-field>

      <v-text-field
        v-model="user.password"
        prepend-icon="mdi-lock-outline"
        :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
        :rules="[rules.required]"
        :type="show1 ? 'text' : 'password'"
        hint="At least 8 characters"
        label="Password"
        counter
        @click:append="show1 = !show1"
        class="mx-3"
      ></v-text-field>

      <span class="d-flex align-center justify-center mb-2 mt-16 ga-16" >
        <v-btn
          prepend-icon="mdi-close-circle-outline"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="300px"
          :href="`/signup`"
        >Sign up</v-btn>
        <v-btn
          @click="submit"
          prepend-icon="mdi-send"
          color="primary"
          flat
          rounded
          class="mb-10 text-h6"
          min-height="70px"
          min-width="300px"
        >Log in</v-btn>
      </span>




    </form>
  </v-card>

</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import {login} from '@/scripts/common'

// Get the route object
const route = useRoute();
const show1 = ref<boolean>(false)

const rules = {
    required: value => !!value || 'Required.',
    min: v => v.length >= 8 || 'Min 8 characters',
    passwordMatch: () => user.value.password == password2.value || `Passwords do not match`,
  }

const user = ref<object>({
  username: '',
  password: '',
})



const submit = () => {
  console.log(user.password)
  console.log(user)
  login(user.value)
  //createRecipe(recipe)
}



</script>
