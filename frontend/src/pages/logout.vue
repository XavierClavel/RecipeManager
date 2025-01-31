<template>
  <v-card
  class="rounded-xl pa-5 ma-5"
  max-width="1000px"
  >


    <form @submit.prevent="submit" class="mx-auto">
      <v-card-title class="text-primary text-h3">
        Logout
      </v-card-title>
      <v-card-text>
        You have successfully logged out.
      </v-card-text>

      <v-container>
        <v-row
          class="d-flex align-center justify-center mb-2 mt-16 gx-16"
          dense
        >
          <v-col cols="12" sm="auto">
            <v-btn
              prepend-icon="mdi-account-plus"
              color="primary"
              flat
              rounded
              class="mb-10 text-h6"
              min-height="70px"
              min-width="300px"
              @click="toSignup"
            >Sign up</v-btn>
          </v-col>
          <v-col cols="12" sm="auto">
            <v-btn
              @click="toLogin"
              prepend-icon="mdi-send"
              color="primary"
              flat
              rounded
              class="mb-10 text-h6"
              min-height="70px"
              min-width="300px"
            >Log in</v-btn>
          </v-col>
        </v-row>
      </v-container>

    </form>
  </v-card>

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
