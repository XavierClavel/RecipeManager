import apiClient from '@/plugins/axios.js';
import {getLocale} from "@/scripts/localization";

export{
  getIngredient,
  searchIngredients,
  deleteIngredient,
  getCount,
  getIngredientRecipesCount,
  createIngredient,
  updateIngredient,
}

async function getIngredient(id) {
  return await apiClient.get(`/ingredient/${id}`)
}

async function searchIngredients(query, page, size) {
  return await apiClient.get(`/ingredient?query=${query}&page=${page}&size=${size}&locale=${getLocale()}`)
}

async function getCount() {
  return await apiClient.get(`/ingredient/count`)
}

async function getIngredientRecipesCount(id) {
  return await apiClient.get(`/ingredient/count/recipes/${id}`)
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
