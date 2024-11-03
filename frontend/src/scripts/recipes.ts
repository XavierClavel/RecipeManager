import {base_url} from "@/scripts/common";
import apiClient from '@/plugins/axios.js';

export {
  getRecipe,
  listRecipes,
  createRecipe,
  deleteRecipe,
  uploadRecipeImage,
  deleteRecipeImage,
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

async function uploadRecipeImage(id, file) {
  let formData = new FormData()
  formData.append('file', file)
  return await apiClient.post( `/image/recipes/${id}`,
    formData,
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
  ).then(function(response){
    console.log('SUCCESS!!');
    console.log(response)
  })
    .catch(function(error){
      console.log('FAILURE!!');
      console.log(error)
    });
}

async function deleteRecipeImage(id) {
  return await apiClient.delete(`/image/recipes/${id}`)
}
