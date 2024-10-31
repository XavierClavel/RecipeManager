import {base_url} from "@/scripts/common";
import apiClient from '@/plugins/axios.js';

export{
  getUsers,
  deleteUser,
}



async function getUsers() {
  return await apiClient.get(`${base_url}/user`)
}

async function deleteUser(username) {
  return await apiClient.delete(`${base_url}/user/${username}`)
}
