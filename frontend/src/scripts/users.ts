import {base_url} from "@/scripts/common";
import apiClient from '@/plugins/axios.js';

export{
  getUser,
  updateUser,
  getUsers,
  deleteUser,
  searchUsers,
}

async function getUser(id) {
  return await apiClient.get(`${base_url}/user/${id}`)
}


async function updateUser(id, user) {
  return await apiClient.put(`${base_url}/user/${id}`, user)
}

async function getUsers() {
  return await apiClient.get(`${base_url}/user`)
}

async function deleteUser(id) {
  return await apiClient.delete(`${base_url}/user/${id}`)
}

async function searchUsers(query, page, size) {
  return await apiClient.get(`/user?search=${query}&page=${page}&size=${size}`)
}
