<template>
  <v-layout class="rounded rounded-md">

    <v-main class="d-flex align-center justify-center" style="min-height: 300px;" >
      <v-card width="100%" max-width="1500px" class="ma-5">
        <v-card-title>
          Users
        </v-card-title>
          <v-table
            fixed-header
          >
            <thead>
            <tr>
              <th class="text-left">
                User
              </th>
              <th class="text-center">
                Role
              </th>
              <th class="text-center">
                Actions
              </th>
            </tr>
            </thead>
            <tbody>
            <tr
              v-for="user in users"
              :key="user.username"
            >
              <td>
                <v-avatar size="40" variant="elevated" class="mr-2" style="border:2px solid #0d1821 !important;">
                  <v-img
                    color="surface-variant"
                    :src="getUserIconUrl(user.id, user.version)"
                    cover
                    v-bind="props"
                    class="clickable_image"
                  ></v-img>
                </v-avatar>
                {{ user.username }}</td>
              <td>
                <div class="d-flex justify-center align-center" style="height: 100%;">
                  <div style="width: 130px">
                  <v-select
                    v-model="user.role"
                    :items="userRoles"
                    density="compact"
                    style="width: 130px"
                    @update:modelValue="() => updateRole(user)"
                  ></v-select>
                  </div>
                </div>
              </td>
              <td class="y-5 align-content-center justify-center align-center" >
                <div class="d-flex justify-center align-center" style="height: 100%;">
                <v-btn icon="mdi-pencil" color="primary" flat class="mr-1"></v-btn>
                <v-btn icon="mdi-delete" color="primary" flat @click="performDelete(user.username)"></v-btn>
                </div>
              </td>
            </tr>
            </tbody>
          </v-table>
        <v-row justify="center">
          <v-col cols="6">
            <v-container class="max-width">
              <v-pagination
                v-model="page"
                :length="pagesCount"
                class="my-4"
                active-color="primary"
                @update:modelValue="updateDisplay"
              ></v-pagination>
            </v-container>
          </v-col>
        </v-row>
      </v-card>

    </v-main>
  </v-layout>
</template>

<script lang="ts" setup>
import {getUsers, deleteUser, setRole} from "@/scripts/users";
import {getUserIconUrl} from "@/scripts/common";
let users = ref<string[]>([])

const userRoles = [
  "USER",
  "ADMIN",
]

const updateRole = (user) => {
  setRole(user.id, user.role)
}

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
