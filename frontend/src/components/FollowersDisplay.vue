<script setup lang="ts">
import {getUsers} from "@/scripts/users";
import {getUserIconUrl, toViewUser} from "../scripts/common";
import {ICON_ACCEPT, ICON_DENY, ICON_REMOVE} from "../scripts/icons";
import {removeFollower, unfollow} from "@/scripts/follows";

const props = defineProps({
  isFollowersTab: {
    type: Boolean,
    required: true,
  },
})

const usersAccepted = ref(null)
const usersPending = ref(null)

const refreshData = () => {
  getUsers().then((response) => {
    usersPending.value = response.data.filter((it) => it.pending)
    usersAccepted.value = response.data.filter((it) => !it.pending)
  })
}

refreshData()


const hasPending = computed(() => usersPending?.value?.length)
const hasAccepted = computed(() => usersAccepted?.value?.length)

async function denyFollowRequest(id) {

}

async function acceptFollowRequest(id) {

}

async function cancelFollowRequest(id) {
  await unfollow(id)
}

async function remove(id) {
  if (props.isFollowersTab) {
    await unfollow(id)
  } else {
    await removeFollower(id)
  }
  await refreshData()
}

</script>

<template>
<v-card class="ml-2" max-width="500px">
  <v-list lines="two">
    <v-list-subheader inset v-if="hasPending">Pending</v-list-subheader>

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

    <v-divider inset v-if="hasPending && hasAccepted"></v-divider>

    <v-list-subheader inset v-if="hasAccepted">Accepted</v-list-subheader>

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
