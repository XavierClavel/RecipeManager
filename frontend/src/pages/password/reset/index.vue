<template>
  <v-container class="mx-auto" align="center">
  <v-card
  class="pa-5 ma-5"
  max-width="1000px"
  >
    <v-form @submit.prevent="submit" class="mx-auto" ref="loginForm" >
      <v-card-title>
        {{$t("reset_password")}}
      </v-card-title>
      <v-card-text>
        {{$t("reset_password_text")}}
      </v-card-text>
      <error :error="errorMessage"></error>
      <v-text-field
        v-model="mail"
        prepend-inner-icon="mdi-email-outline"
        :label="`${$t('mail')}`"
        color="primary"
        :rules="[requiredRule]"
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
              :text="`${$t('cancel')}`"
              :action="toLogin"
            ></action-button>
            <action-button
              icon="mdi-send"
              :text="`${$t('send')}`"
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
import {requestPasswordReset, signup, toLogin, toResetPasswordEmailSent} from "@/scripts/common";
import {useI18n} from "vue-i18n";
import {min8Rule, passwordRule, requiredRule} from "@/scripts/rules";

// Get the route object
const route = useRoute();
const { t } = useI18n();
const errorMessage = ref(null)
const loginForm = ref(null)

const mail = ref<string>()


const submit = async() => {
  errorMessage.value = null
  const {valid, errors} = await loginForm.value.validate()
  if (!valid) {
    return
  }
  requestPasswordReset(mail.value)
    .then(function (response) {
      toResetPasswordEmailSent()
    })
    .catch(function (error) {
      errorMessage.value = error.response.data
      console.log(error);
    })
}

</script>
