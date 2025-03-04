import {base_url} from "@/scripts/common";
import apiClient from '@/plugins/axios.js';
import {useAuthStore} from "@/stores/auth";

export {
  addLike,
  removeLike,
  isLiked,
}

async function addLike(id) {
  return await apiClient.post(`/like/${id}`)
}

async function isLiked(id) {
  const response = await apiClient.get(`/like/${id}`)
  return response.data
}

async function removeLike(id) {
  return await apiClient.delete(`/like/${id}`)
}
