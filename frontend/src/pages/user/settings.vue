<template>
  <v-container class="mx-auto" align="center">
    <v-card
    class="lg pa-5 ma-5"
    max-width="1000px"
    min-width="300px"
    >


      <form @submit.prevent="submit">
        <v-card-title class="text-black text-h2 font-weight-bold" >
          {{$t("settings")}}
        </v-card-title>
        <error :error="errorMessage"></error>
        <v-card class="my-2">
          <v-select
          v-model="locale"
          prepend-inner-icon="mdi-translate"
          :items="locales"
          item-title="label"
          item-value="value"
          variant="solo"
          flat
          single-line
          hide-details
          bg-color="background"
          ></v-select>
        </v-card>

        <v-container>
          <v-row
            class="d-flex align-center justify-center mb-2 gx-16"
            dense
          >
            <v-col sm="auto" class="mx-5">
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
  </v-container>

</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import {login, toSignup} from '@/scripts/common'
import {useI18n} from "vue-i18n";
import {ICON_SAVE} from "@/scripts/icons";
import {forceLocale, getLocale} from "@/scripts/localization";

// Get the route object
const route = useRoute();
const show1 = ref<boolean>(false)
const errorMessage = ref(null)
const { t } = useI18n();

const locale = ref(getLocale())

const locales = [
  {label: "Français", value: "fr"},
  {label: "English", value: "en"},
]


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
  forceLocale(locale.value)
}



</script>
