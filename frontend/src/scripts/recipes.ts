import {base_url} from "@/scripts/common";
import apiClient from '@/plugins/axios.js';

export {
  getRecipe,
  createRecipe,
  deleteRecipe,
}

async function getRecipe(id) {
  return await apiClient.get(`${base_url}/recipe/${id}`)
}

async function createRecipe(recipe) {
  return await apiClient.post(`${base_url}/recipe`, recipe)
}

async function updateRecipe(id,recipe) {
  return await apiClient.put(`${base_url}/recipe/${id}`, recipe)
}

async function deleteRecipe(id) {
  return await apiClient.delete(`${base_url}/recipe/${id}`)
}
