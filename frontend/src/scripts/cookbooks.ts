import {base_url} from "@/scripts/common";
import apiClient from '@/plugins/axios.js';

export {
  listCookbooks,
  createCookbook,
  editCookbook,
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
