<template>
  <v-container class="mx-auto" align="center">
    <v-card
    class="pa-5 ma-5"
    max-width="1000px"
    min-width="300px"
    >


      <form @submit.prevent="submit">
        <v-card-title>
          {{$t("update_password_success_title")}}
        </v-card-title>
        <v-card-text class="my-4">
          {{$t("update_password_success_description")}}
        </v-card-text>
        <v-container>
          <v-row
            class="d-flex align-center justify-center mb-2 gx-16"
            dense
            justify="center"
            align="center"
          >
            <v-col cols="12" sm="auto" justify-center>
              <action-button
                icon="mdi-send"
                :text="`${$t('login')}`"
                :action="toLogin"
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
import {login, toLogin, toSignup} from '@/scripts/common'
import {useI18n} from "vue-i18n";
import {ICON_ACCEPT, ICON_SAVE} from "@/scripts/icons";

// Get the route object
const route = useRoute();
const errorMessage = ref(null)
const { t } = useI18n();


</script>
