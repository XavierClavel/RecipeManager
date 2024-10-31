import {base_url} from "@/scripts/common";
import apiClient from '@/plugins/axios.js';

export {
  getRecipe,
  listRecipes,
  createRecipe,
  deleteRecipe,
}

async function getRecipe(id) {
  return await apiClient.get(`/recipe/${id}`)
}

async function listRecipes() {
  return await apiClient.get(`/recipe`)
}

async function createRecipe(recipe) {
  return await apiClient.post(`/recipe`, recipe)
}

async function updateRecipe(id,recipe) {
  return await apiClient.put(`/recipe/${id}`, recipe)
}

async function deleteRecipe(id) {
  return await apiClient.delete(`/recipe/${id}`)
}
