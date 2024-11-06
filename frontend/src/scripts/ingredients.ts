
import {base_url} from "@/scripts/common";
import apiClient from '@/plugins/axios.js';

export{
  searchIngredients,
  deleteIngredient,
  getCount,
}



async function searchIngredients(query, page, size) {
  return await apiClient.get(`=/ingredient?search=${query}&page=${page}&size=${size}`)
}

async function getCount() {
  return await apiClient.get(`/ingredient/count`)
}

async function deleteIngredient(username) {
  return await apiClient.delete(`/ingredient/${username}`)
}

async function createIngredient(ingredient) {
  return await apiClient.post(`/ingredient`, ingredient)
}

async function updateIngredient(id, ingredient) {
  return await apiClient.put(`/ingredient/${id}`, ingredient)
}
