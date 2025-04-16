<template>
  <v-container class="mx-auto" align="center">
    <v-card
    class="pa-5 ma-5"
    max-width="1000px"
    min-width="300px"
    >
      <v-card-title>
        {{$t("reset_password_email_sent_title")}}
      </v-card-title>
      <v-card-text class="my-4">
        {{$t("reset_password_email_sent_description")}}
      </v-card-text>
    </v-card>
  </v-container>

</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import {login, toSignup} from '@/scripts/common'
import {useI18n} from "vue-i18n";
const errorMessage = ref(null)
const { t } = useI18n();

</script>
