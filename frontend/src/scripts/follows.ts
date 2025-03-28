import apiClient from '@/plugins/axios.js';
import {useAuthStore} from "@/stores/auth";

export{
  follow,
  unfollow,
  removeFollower,
  getFollows,
  getFollowers,
  isFollowingUser,
}

async function follow(id) {
  return await apiClient.post(`/follow/${id}`)
}

async function unfollow(id) {
  return await apiClient.delete(`/follow/${id}`)
}

async function removeFollower(id) {

}

async function getFollows(id) {
  return await apiClient.get(`/follow/${id}/follows`)
}

async function getFollowers(id) {
  return await apiClient.delete(`/follow/${id}/followers`)
}

async function isFollowingUser(id) {
  const authStore = useAuthStore()
  const userId = authStore.id
  return await apiClient.get(`/follow/${userId}/follows/${id}`)
}
