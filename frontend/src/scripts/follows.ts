import {base_url} from "@/scripts/common";
import apiClient from '@/plugins/axios.js';
import {useAuthStore} from "@/stores/auth";

export{
  follow,
  unfollow,
  getFollows,
  getFollowers,
  isFollowingUser,
}

async function follow(id) {
  return await apiClient.post(`${base_url}/follow/${id}`)
}

async function unfollow(id) {
  return await apiClient.delete(`${base_url}/follow/${id}`)
}

async function getFollows(id) {
  return await apiClient.get(`${base_url}/follow/${id}/follows`)
}

async function getFollowers(id) {
  return await apiClient.delete(`${base_url}/follow/${id}/followers`)
}

async function isFollowingUser(id) {
  const authStore = useAuthStore()
  const userId = authStore.id
  return await apiClient.get(`${base_url}/follow/${userId}/follows/${id}`)
}
