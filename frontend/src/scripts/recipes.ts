import {base_url} from "@/scripts/common";
import axios from "axios";

export {
  getRecipe,
  createRecipe,
  deleteRecipe,
}

async function getRecipe(id) {
  return await axios.get(`${base_url}/recipe/${id}`)
}

async function createRecipe(recipe) {
  return await axios.post(`${base_url}/recipe`, recipe)
}

async function updateRecipe(id,recipe) {
  return await axios.put(`${base_url}/recipe/${id}`, recipe)
}

async function deleteRecipe(id) {
  return await axios.delete(`${base_url}/recipe/${id}`)
}
