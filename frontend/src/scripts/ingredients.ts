
import {base_url} from "@/scripts/common";
import apiClient from '@/plugins/axios.js';

export{
  getIngredients,
  deleteIngredient,
}



async function getIngredients() {
  return await apiClient.get(`${base_url}/ingredient`)
}

async function deleteIngredient(username) {
  return await apiClient.delete(`${base_url}/ingredient/${username}`)
}
