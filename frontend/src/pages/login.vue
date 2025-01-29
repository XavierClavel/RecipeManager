<template>
  <v-card
  class="rounded-xl pa-5 ma-5"
  max-width="1000px"
  >


    <form @submit.prevent="submit">
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
        label="Password"
        counter
        @click:append="show1 = !show1"
        class="mx-3"
        @keyup.enter="submit"
      ></v-text-field>

      <v-container>
        <v-row
          class="d-flex align-center justify-center mb-2 mt-16 gx-16"
          dense
        >
          <v-col cols="12" sm="auto">
            <v-btn
              prepend-icon="mdi-close-circle-outline"
              color="primary"
              flat
              rounded
              class="mb-10 text-h6"
              min-height="70px"
              min-width="300px"
              @click="toSignup"
            >
              Sign up
            </v-btn>
          </v-col>
          <v-col cols="12" sm="auto">
            <v-btn
              @click="submit"
              prepend-icon="mdi-send"
              color="primary"
              flat
              rounded
              class="mb-10 text-h6"
              min-height="70px"
              min-width="300px"
            >
              Log in
            </v-btn>
          </v-col>
        </v-row>
      </v-container>




    </form>
  </v-card>

</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import {login, toSignup} from '@/scripts/common'

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
