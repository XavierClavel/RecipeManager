import apiClient from '@/plugins/axios.js';

export {
  getCookbook,
  listCookbooks,
  getStatusInCookbooks,
  createCookbook,
  editCookbook,
  searchCookbooks,

  addRecipeToCookbook,
  removeRecipeFromCookbook,

  getCookbookUsers,
  setCookbookUsers,
}

async function getCookbook(id) {
  return await apiClient.get(`/cookbook/${id}`)
}

async function listCookbooks(search) {
  return await apiClient.get(`/cookbook${search}`)
}

async function getStatusInCookbooks(recipe) {
  return await apiClient.get(`/cookbook/recipeStatus?recipe=${recipe}`)
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

async function searchCookbooks(search) {
  return await apiClient.get(`cookbook?search=${search}`)
}

async function getCookbookUsers(cookbookId) {
  return await apiClient.get(`/cookbook/${cookbookId}/users`)
}

async function setCookbookUsers(cookbookId, input) {
  return await apiClient.put(`/cookbook/${cookbookId}/users`, input)
}
