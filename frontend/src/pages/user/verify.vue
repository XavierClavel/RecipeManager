<template>
  <v-container class="mx-auto" align="center">
    <v-card
      class="pa-5 ma-5"
      max-width="1000px"
      min-width="300px"
    >


    <form @submit.prevent="submit">
      <v-card-title>
        {{$t("email_verification")}}
      </v-card-title>
      <error :error="errorMessage"></error>

      <v-card-text v-if="!errorMessage">{{ $t("email_verified")}}</v-card-text>


      <v-container>
        <v-row
          class="d-flex align-center justify-center mb-2 gx-16"
          dense
        >
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
import {login, toLogin, toSignup, verifyEmail} from '@/scripts/common'

// Get the route object
const route = useRoute();
const errorMessage = ref(null)
let token = ref(route.query.token)


const user = ref<object>({
  username: '',
  password: '',
})

const verify = (token) => {
  verifyEmail(token).catch(function (error) {
    console.log(error.response)
    errorMessage.value = error.response.data
  })
}

verify(token.value)



const submit = () => {
  toLogin()
}



</script>
