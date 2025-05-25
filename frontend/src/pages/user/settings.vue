<template>
  <v-container class="mx-auto" align="center">
    <v-card
    class="lg pa-5 ma-5"
    max-width="1000px"
    min-width="300px"
    >


      <form @submit.prevent="submit">
        <v-card-title >
          {{$t("settings")}}
        </v-card-title>
        <error :error="errorMessage"></error>
        <v-select
        v-model="locale"
        :prepend-inner-icon="ICON_LOCALIZATION"
        :items="locales"
        item-title="label"
        item-value="value"
        ></v-select>

        <v-card color="background" class="mb-2">
          <v-checkbox
            v-model="settings.isAccountPublic"
            :label="`${$t('public_account')}`"
            color="black"
            base-color="black"
            bg-color="background"
            variant="elevated"
            class="mx-2 my-0 mb-n6"
          ></v-checkbox>
        </v-card>

        <v-card color="background" class="mb-2">
          <v-checkbox
            v-model="settings.autoAcceptFollowRequests"
            :label="`${$t('auto_accept_follow_requests')}`"
            color="black"
            base-color="black"
            variant="elevated"
            class="mx-2 my-0 mb-n6"
          ></v-checkbox>
        </v-card>

        <v-container>
          <v-row
            class="d-flex align-center justify-center mb-2 ga-4"
            dense
          >
              <action-button
                icon="mdi-lock-reset"
                :text="`${$t('password')}`"
                :action="toUpdatePassword"
              ></action-button>
              <action-button
                :icon="ICON_SAVE"
                :text="`${$t('save')}`"
                :action="submit"
              ></action-button>
          </v-row>
        </v-container>




      </form>
    </v-card>
  </v-container>

</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import {login, toMyProfile, toSignup, toUpdatePassword} from '@/scripts/common'
import {useI18n} from "vue-i18n";
import {ICON_LOCALIZATION, ICON_SAVE} from "@/scripts/icons";
import {forceLocale, getLocale} from "@/scripts/localization";
import {getSettings, updateSettings} from "@/scripts/settings";

const errorMessage = ref(null)
const { t } = useI18n();

const locale = ref(getLocale())

const locales = [
  {label: "Français", value: "fr"},
  {label: "English", value: "en"},
]

const settings = ref({})

getSettings().then(response => {
  settings.value = response.data
})

const submit = () => {
  forceLocale(locale.value)
  updateSettings(settings.value).then(response => {
    toMyProfile()
  })
}



</script>
