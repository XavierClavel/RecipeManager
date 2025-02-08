<template>

  <v-card
  class="mx-auto rounded-xl pa-5 ma-auto my-5"
  max-width="1000px"
  >

    <form @submit.prevent="submit" class="mx-auto">
      <v-card-title class="text-primary text-h3">
        {{$t("update_password")}}
      </v-card-title>

      <error :error="errorMessage"></error>

      <v-text-field
        v-model="currentPassword"
        prepend-icon="mdi-lock-outline"
        :append-icon="showOld ? 'mdi-eye' : 'mdi-eye-off'"
        :rules="[rules.required, rules.min]"
        :type="show1 ? 'text' : 'password'"
        hint="At least 8 characters"
        :label="`${$t('current_password')}`"
        counter
        @click:append="show1 = !show1"
        class="mx-3"
      ></v-text-field>

    <v-text-field
      v-model="newPassword1"
      prepend-icon="mdi-lock-outline"
      :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
      :rules="[rules.required, rules.min]"
      :type="show1 ? 'text' : 'password'"
      hint="At least 8 characters"
      :label="`${$t('new_password')}`"
      counter
      @click:append="show1 = !show1"
      class="mx-3"
    ></v-text-field>

    <v-text-field
      v-model="newPassword2"
      prepend-icon="mdi-lock-check-outline"
      :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
      :rules="[rules.required, rules.passwordMatch]"
      :type="show2 ? 'text' : 'password'"
      hint="Passwords must match"
      :label="`${$t('confirm_new_password')}`"
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
              :icon="ICON_SAVE"
              :text="`${$t('save')}`"
              :action="submit"
            ></action-button>
          </v-col>
        </v-row>
      </v-container>

    </form>

  </v-card>

</template>

<script lang="ts" setup>
import {ref} from "vue";
import {useI18n} from "vue-i18n";
import {ICON_SAVE} from "@/scripts/icons";
import {toMyProfile} from "@/scripts/common";
import {updatePassword} from "@/scripts/users";

const showOld = ref(false)
const show1 = ref<boolean>(false)
const show2 = ref<boolean>(false)

const currentPassword = ref(null)
const newPassword1 = ref(null)
const newPassword2 = ref(null)

const errorMessage = ref(null)
const { t } = useI18n()


const rules = {
  required: value => !!value || t('required'),
  min: v => v.length >= 8 || t('min_8_characters'),
  passwordMatch: () => password1.value == password2.value || t('passwords_must_match'),
}

const submit = () => {
  updatePassword(currentPassword.value, newPassword1.value)
    .then(function (response) {
      toMyProfile()
    })
    .catch(function (error) {
    errorMessage.value = error.response.data
    console.log(error);
  })
}

</script>
