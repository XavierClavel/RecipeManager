<template>
  <v-container class="mx-auto" align="center">
    <v-card
      class="pa-5 ma-5"
      max-width="1000px"
      min-width="300px"
    >


    <form @submit.prevent="submit">
      <v-card-title>
        Email verification
      </v-card-title>
      <error :error="errorMessage"></error>

      <v-card-text v-if="!errorMessage">Your email was successfully verified! Please click the button below to log in to your account</v-card-text>
      <v-container>
        <v-row
          class="d-flex align-center justify-center mb-2 mt-16 gx-16"
          dense
        >
          <v-col cols="12" sm="auto">
            <v-btn
              @click="submit"
              prepend-icon="mdi-send"
              color="primary"
              flat
              rounded
              class="mb-10 text-h6"
              min-height="70px"
              min-width="300px"
            >
              Log in
            </v-btn>
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
