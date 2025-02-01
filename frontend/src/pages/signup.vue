<template>
  <v-container class="mx-auto" align="center">
  <v-card
  class="rounded-xl pa-5 ma-5"
  max-width="1000px"
  >
    <form @submit.prevent="submit" class="mx-auto">
      <v-card-title class="text-primary text-h3">
        {{$t("signup")}}
      </v-card-title>
      <v-text-field
        v-model="user.username"
        prepend-icon="mdi-account"
        :label="`${$t('username')}`"
        class="mx-auto px-3"
        color="primary"
        :rules="[rules.required]"
      ></v-text-field>

      <v-text-field
        v-model="user.mail"
        prepend-icon="mdi-email-outline"
        :label="`${$t('mail')}`"
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
        :label="`${$t('password')}`"
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
        :label="`${$t('password_confirm')}`"
        counter
        @click:append="show2 = !show2"
        class="mx-3"
      ></v-text-field>

      <v-container>
        <v-row
          class="d-flex align-center justify-center mb-2 gx-16"
          dense
          justify="center"
          align="center"
        >
          <v-col cols="12" sm="auto" justify-center>
            <action-button
              icon="mdi-close-circle-outline"
              :text="`${$t('log_in')}`"
              :action="toLogin"
            ></action-button>
            <action-button
              icon="mdi-send"
              :text="`${$t('sign_up')}`"
              :action="submit"
            ></action-button>
          </v-col>
        </v-row>
      </v-container>

    </form>
  </v-card>
  </v-container>

</template>

<script lang="ts" setup>
import { ref } from 'vue';
import draggable from 'vuedraggable';
import { useRoute } from 'vue-router';
import {getRecipe, createRecipe} from "@/scripts/recipes";
import {signup, toLogin} from "@/scripts/common";
import {useI18n} from "vue-i18n";

// Get the route object
const route = useRoute();
const recipeId = route.query.id
const show1 = ref<boolean>(false)
const show2 = ref<boolean>(false)
const password2 = ref<string>('')
const { t } = useI18n();

const rules = {
    required: value => !!value || t('required'),
    min: v => v.length >= 8 || t('min_8_characters'),
    passwordMatch: () => user.value.password == password2.value || t('passwords_must_match'),
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
