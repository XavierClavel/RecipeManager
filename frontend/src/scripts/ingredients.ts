
import {base_url} from "@/scripts/common";
import apiClient from '@/plugins/axios.js';

export{
  getIngredients,
  deleteIngredient,
  getCount,
}



async function getIngredients(page) {
  return await apiClient.get(`${base_url}/ingredient?page=${page}&size=20`)
}

async function getCount() {
  return await apiClient.get(`${base_url}/ingredient/count`)
}

async function deleteIngredient(username) {
  return await apiClient.delete(`${base_url}/ingredient/${username}`)
}
