<template>
  <v-container class="mx-auto" align="center">
  <v-card
  class="pa-5 ma-5"
  max-width="1000px"
  >
    <v-form @submit.prevent="submit" class="mx-auto" ref="loginForm" >
      <v-card-title>
        {{$t("signup")}}
      </v-card-title>
      <error :error="errorMessage"></error>
      <v-text-field
        v-model="user.username"
        prepend-inner-icon="mdi-account"
        :label="`${$t('username')}`"
        color="primary"
        :rules="[requiredRule]"
      ></v-text-field>

      <v-text-field
        v-model="user.mail"
        prepend-inner-icon="mdi-email-outline"
        :label="`${$t('mail')}`"
        color="primary"
        :rules="[requiredRule]"
      ></v-text-field>

      <v-text-field
        v-model="user.password"
        prepend-inner-icon="mdi-lock-outline"
        :append-inner-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
        :rules="[requiredRule, min8Rule]"
        :type="show1 ? 'text' : 'password'"
        :label="`${$t('password')}`"
        counter
        @click:append-inner="show1 = !show1"
      ></v-text-field>

      <v-text-field
        v-model="password2"
        prepend-inner-icon="mdi-lock-check-outline"
        :append-inner-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
        :rules="[requiredRule, passwordRule(user.password, password2)]"
        :type="show2 ? 'text' : 'password'"
        :label="`${$t('password_confirm')}`"
        counter
        @click:append-inner="show2 = !show2"
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

    </v-form>
  </v-card>
  </v-container>

</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import {signup, toLogin} from "@/scripts/common";
import {useI18n} from "vue-i18n";
import {min8Rule, passwordRule, requiredRule} from "@/scripts/rules";

// Get the route object
const route = useRoute();
const show1 = ref<boolean>(false)
const show2 = ref<boolean>(false)
const password2 = ref<string>('')
const { t } = useI18n();
const errorMessage = ref(null)
const loginForm = ref(null)

const user = ref<object>({
  username: '',
  mail: '',
  password: '',
})


const submit = async() => {
  errorMessage.value = null
  const {valid, errors} = await loginForm.value.validate()
  if (!valid) {
    return
  }
  signup(user.value).catch(function (error) {
    errorMessage.value = error.response.data
    console.log(error);
  })
}

</script>
