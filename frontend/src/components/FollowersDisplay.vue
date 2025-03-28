<script setup lang="ts">
import {getUsers} from "@/scripts/users";
import {getUserIconUrl, toViewUser} from "../scripts/common";
import {ICON_ACCEPT, ICON_DENY, ICON_REMOVE} from "../scripts/icons";

const usersAccepted = ref(null)
const usersPending = ref(null)
getUsers().then((response) => {
  usersPending.value = response.data.filter((it) => it.pending)
  usersAccepted.value = response.data.filter((it) => !it.pending)
})

</script>

<template>
<v-card class="ml-2" max-width="500px">
  <v-list lines="two">
    <v-list-subheader inset v-if="usersPending.size > 0">Pending</v-list-subheader>

    <v-list-item
      v-for="user in usersPending"
      :key="user.username"
      :subtitle="user.subtitle"
      :title="user.username"
      @click="toViewUser(user.id)"
    >
      <template v-slot:prepend>
        <v-avatar color="grey-lighten-1">
          <v-img :src="getUserIconUrl(user.id)"></v-img>
        </v-avatar>
      </template>

      <template v-slot:append>
        <v-btn
          color="grey-lighten-1"
          :icon="ICON_ACCEPT"
          variant="text"
        ></v-btn>
        <v-btn
          color="grey-lighten-1"
          :icon="ICON_DENY"
          variant="text"
        ></v-btn>
      </template>
    </v-list-item>

    <v-divider inset v-if="usersPending.size > 0 && usersAccepted.size > 0"></v-divider>

    <v-list-subheader inset v-if="usersAccepted.size > 0">Accepted</v-list-subheader>

    <v-list-item
      v-for="user in usersAccepted"
      :key="user.username"
      :subtitle="user.subtitle"
      :title="user.username"
    >
      <template v-slot:prepend>
        <v-avatar color="grey-lighten-1">
          <v-img :src="getUserIconUrl(user.id)"></v-img>
        </v-avatar>
      </template>

      <template v-slot:append>
        <v-btn
          color="grey-lighten-1"
          :icon="ICON_REMOVE"
          variant="text"
        ></v-btn>
      </template>
    </v-list-item>
  </v-list>
</v-card>
</template>

<style scoped>

</style>
