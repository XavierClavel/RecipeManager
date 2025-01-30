<template>
  <v-card
  class="rounded-xl pa-5 ma-5"
  max-width="1000px"
  >


    <form @submit.prevent="submit" class="mx-auto">
      <v-card-title class="text-primary text-h3">
        Signup
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
        v-model="user.mail"
        prepend-icon="mdi-email-outline"
        label="Email address"
        class="mx-auto px-3"
        color="primary"
        :rules="[rules.required]"
      ></v-text-field>

      <v-text-field
        v-model="user.password"
        prepend-icon="mdi-lock-outline"
        :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
        :rules="[rules.required, rules.min]"
        :type="show1 ? 'text' : 'password'"
        hint="At least 8 characters"
        label="Password"
        counter
        @click:append="show1 = !show1"
        class="mx-3"
      ></v-text-field>

      <v-text-field
        v-model="password2"
        prepend-icon="mdi-lock-check-outline"
        :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
        :rules="[rules.required, rules.passwordMatch]"
        :type="show2 ? 'text' : 'password'"
        hint="Passwords must match"
        label="Confirm password"
        counter
        @click:append="show2 = !show2"
        class="mx-3"
      ></v-text-field>

      <v-container>
        <v-row
          class="d-flex align-center justify-center mb-2 mt-16 gx-16"
          dense
          justify="center"
          align="center"
        >
          <v-col cols="12" sm="auto" justify-center>

            <v-btn
              prepend-icon="mdi-close-circle-outline"
              color="primary"
              flat
              rounded
              class="mb-10 text-h6"
              min-height="70px"
              min-width="300px"
              @click="toLogin"
            >Log in</v-btn>
            <v-btn
              @click="submit"
              prepend-icon="mdi-send"
              color="primary"
              flat
              rounded
              class="mb-10 text-h6"
              min-height="70px"
              min-width="300px"
            >Sign up</v-btn>
          </v-col>
        </v-row>
      </v-container>

    </form>
  </v-card>

</template>

<script lang="ts" setup>
import { ref } from 'vue';
import draggable from 'vuedraggable';
import { useRoute } from 'vue-router';
import {getRecipe, createRecipe} from "@/scripts/recipes";
import {signup, toLogin} from "@/scripts/common";

// Get the route object
const route = useRoute();
const recipeId = route.query.id
const show1 = ref<boolean>(false)
const show2 = ref<boolean>(false)
const password2 = ref<string>('')

const rules = {
    required: value => !!value || 'Required.',
    min: v => v.length >= 8 || 'Min 8 characters',
    passwordMatch: () => user.value.password == password2.value || `Passwords do not match`,
  }

const user = ref<object>({
  username: '',
  mail: '',
  password: '',
})



const submit = () => {
  console.log(password2)
  console.log(user.password)
  console.log(user)
  signup(user.value)
}

if (recipeId != undefined) {
  getRecipe(recipeId).then (
    function (response) {
      recipe.value.title = response.data.title
      recipe.value.description = response.data.description
      recipe.value.servings = response.data.portions
      recipe.value.ingredients = response.data.ingredients
      recipe.value.steps = response.data.steps
      console.log(recipe.value)
    }).catch(function (error) {
    console.log(error);
  }).finally(function () {
    // always executed
  });
}



</script>
