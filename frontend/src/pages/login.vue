<template>
  <v-container class="mx-auto" align="center">
    <v-card
    class="pa-5 ma-5"
    max-width="1000px"
    min-width="300px"
    >


      <form @submit.prevent="submit">
        <v-card-title class="text-primary text-h3">
          {{$t("login")}}
        </v-card-title>
        <error :error="errorMessage"></error>
        <v-text-field
          v-model="user.username"
          prepend-icon="mdi-account"
          :label="`${$t('username')}`"
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
          :label="`${$t('password')}`"
          counter
          @click:append="show1 = !show1"
          class="mx-3"
          @keyup.enter="submit"
        ></v-text-field>

        <v-container>
          <v-row
            class="d-flex align-center justify-center mb-2 gx-16"
            dense
          >
            <v-col cols="12" sm="auto" class="mx-5">
              <action-button
                icon="mdi-close-circle-outline"
                :text="`${$t('sign_up')}`"
                :action="toSignup"
              ></action-button>
            </v-col>
            <v-col sm="auto" class="mx-5">
              <action-button
                icon="mdi-send"
                :text="`${$t('log_in')}`"
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
import { useRoute } from 'vue-router';
import {login, toSignup} from '@/scripts/common'
import {useI18n} from "vue-i18n";

// Get the route object
const route = useRoute();
const show1 = ref<boolean>(false)
const errorMessage = ref(null)
const { t } = useI18n();

console.log(import.meta.env.VITE_API_URL)

const rules = {
  required: value => !!value || t('required'),
  min: v => v.length >= 8 || t('min_8_characters'),
  passwordMatch: () => user.value.password == password2.value || t('passwords_must_match'),
}

const user = ref<object>({
  username: '',
  password: '',
})



const submit = () => {
  errorMessage.value = null
  login(user.value).catch(function (error) {
    errorMessage.value = error.response.data
    console.log(error);
  })
  //createRecipe(recipe)
}



</script>
