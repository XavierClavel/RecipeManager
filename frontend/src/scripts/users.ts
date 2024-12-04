import {base_url} from "@/scripts/common";
import apiClient from '@/plugins/axios.js';

export{
  getUser,
  updateUser,
  getUsers,
  getUserRecipesCount,
  deleteUser,
}

async function getUser(id) {
  return await apiClient.get(`${base_url}/user/${id}`)
}


async function updateUser(id, user) {
  return await apiClient.put(`${base_url}/user/${id}`, user)
}

async function getUserRecipesCount(id) {
  const response = await apiClient.get(`${base_url}/recipe?owner=${id}`)
  return response.data.length
}

async function getUsers() {
  return await apiClient.get(`${base_url}/user`)
}

async function deleteUser(id) {
  return await apiClient.delete(`${base_url}/user/${id}`)
}
