import {base_url} from "@/scripts/common";
import apiClient from '@/plugins/axios.js';

export {
  listCookbooks,
  createCookbook,
  editCookbook,

  addRecipeToCookbook,
  removeRecipeFromCookbook,
}

async function listCookbooks(search) {
  return await apiClient.get(`/cookbook${search}`)
}

async function createCookbook(dto) {
  return await apiClient.post(`/cookbook`, dto)
}

async function editCookbook(id, dto) {
  return await apiClient.put(`/cookbook/${id}`, dto)
}

async function addRecipeToCookbook(cookbookId, recipeId) {
  return await apiClient.post(`cookbook/${cookbookId}/recipe/${recipeId}`)
}

async function removeRecipeFromCookbook(cookbookId, recipeId) {
  return await apiClient.delete(`cookbook/${cookbookId}/recipe/${recipeId}`)
}
