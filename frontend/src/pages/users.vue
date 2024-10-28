<template>
  <v-layout class="rounded rounded-md">

    <v-main class="d-flex align-center justify-center" style="min-height: 300px;" >
      <v-card width="100%" max-width="1500px" class="ma-5" rounded="xl">
        <v-card-title class="text-primary">
          Users
        </v-card-title>
          <v-table
            height="300px"
            fixed-header
          >
            <thead>
            <tr>
              <th class="text-left">
                Username
              </th>
              <th class="text-left">
                Role
              </th>
              <th class="text-left">
                Actions
              </th>
            </tr>
            </thead>
            <tbody>
            <tr
              v-for="user in users"
              :key="user.username"
            >
              <td>{{ user.username }}</td>
              <td>{{ user.role }}</td>
              <td class="y-5">
                <v-btn icon="mdi-pencil" color="primary" rounded="e" flat class="mr-1"></v-btn>
                <v-btn icon="mdi-delete" color="primary" rounded="s" flat @click="performDelete(user.username)"></v-btn>
              </td>
            </tr>
            </tbody>
          </v-table>
      </v-card>

    </v-main>
  </v-layout>
</template>

<script lang="ts" setup>
import {getUsers, deleteUser} from "@/scripts/users";
let users = ref<string[]>([])

const performDelete = (username) => {
  deleteUser(username).then(
    function (response) {
      if (response.status == 200) {
        const index = users.value.findIndex((user) => user.username == username)
        users.value.splice(index,1)
      }
    }).catch(function (error) {
    console.log(error);
  }).finally(function () {
    // always executed
  });

};

getUsers().then (
  function (response) {
    console.log(response)
    users.value = response.data
}).catch(function (error) {
  console.log(error);
}).finally(function () {
    // always executed
  });

</script>
