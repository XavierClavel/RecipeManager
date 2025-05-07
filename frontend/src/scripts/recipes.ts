import apiClient from '@/plugins/axios.js';
import {getLocale} from "@/scripts/localization";

export {
  getRecipe,
  listRecipes,
  createRecipe,
  updateRecipe,
  deleteRecipe,
  uploadRecipeImage,
  deleteRecipeImage,
  downloadRecipe,
}

async function getRecipe(id) {
  return await apiClient.get(`/recipe/${id}?locale=${getLocale()}`)
}

async function listRecipes(search, page, size) {
  const query = new URLSearchParams(search)
  console.log(query)
  if (page != undefined) query.append('page', page)
  if (size != undefined) query.append('limit', size)
  console.log(query)
  return await apiClient.get(`/recipe?${query.toString()}`)
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
