<template>
  <v-container class="mx-auto" align="center">
  <v-card
  class="rounded-xl pa-5 ma-5"
  max-width="1000px"
  >


    <form @submit.prevent="submit" class="mx-auto">
      <v-card-title class="text-primary text-h3">
        Logout
      </v-card-title>
      <v-card-text>
        {{$t("logout_successful")}}
      </v-card-text>

      <v-container>
        <v-row
          class="d-flex align-center justify-center mb-2 mt-16 gx-16"
          dense
        >
          <v-col cols="12" sm="auto">
            <action-button
              icon="mdi-account-plus"
              :text="`${$t('signup')}`"
              :action="toSignup"
            ></action-button>
          </v-col>
          <v-col cols="12" sm="auto">
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

// Get the route object
const route = useRoute();


const user = ref<object>({
  username: '',
  password: '',
})



const submit = () => {
  console.log(user.password)
  console.log(user)
  login(user.value)
}



</script>
