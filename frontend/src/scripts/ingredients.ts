
import {base_url} from "@/scripts/common";
import apiClient from '@/plugins/axios.js';

export{
  searchIngredients,
  deleteIngredient,
  getCount,
}



async function searchIngredients(query, page, size) {
  return await apiClient.get(`${base_url}/ingredient?search=${query}&page=${page}&size=${size}`)
}

async function getCount() {
  return await apiClient.get(`${base_url}/ingredient/count`)
}

async function deleteIngredient(username) {
  return await apiClient.delete(`${base_url}/ingredient/${username}`)
}
