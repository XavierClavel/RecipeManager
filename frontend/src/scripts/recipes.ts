import {base_url} from "@/scripts/common";
import apiClient from '@/plugins/axios.js';

export {
  getRecipe,
  listRecipes,
  createRecipe,
  deleteRecipe,
  uploadRecipeImage,
  deleteRecipeImage,
  downloadRecipe,
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

async function downloadRecipe(id) {
  return await apiClient.get(`/export/recipe/${id}`, {
    responseType: 'arraybuffer',
    headers: {
      'Accept': 'application/pdf'
    }
  })
    .then((response) => {
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'file.pdf'); //or any other extension
      document.body.appendChild(link);
      link.click();
    })
    .catch((error) => console.log(error));
}
