import apiClient from '@/plugins/axios.js';

export{
  getUser,
  updateUser,
  getUsers,
  getUsersCount,
  deleteUser,
  searchUsers,
  updatePassword,
  setRole,
}

class UserOverview {
  private readonly id: number
  private readonly version: number
  private readonly username: string
}


async function getUser(id) {
  return await apiClient.get(`/user/${id}`)
}

async function updateUser(user) {
  return await apiClient.put(`/user`, user)
}

async function getUsers(query, page, size) {
  return await apiClient.get(`/user?query=${query}&page=${page}&size=${size}`)
}

async function getUsersCount() {
  return await apiClient.get(`/user/count`)
}

async function setRole(id, role) {
  return await apiClient.put(`/user/${id}/role/${role}`)
}

async function deleteUser(id) {
  return await apiClient.delete(`/user/${id}`)
}

async function searchUsers(query, page, size) {
  return await apiClient.get(`/user?search=${query}&page=${page}&size=${size}`)
}

async function updatePassword(oldPassword, newPassword) {
  return await apiClient.put("/user/password",
    {
      old: oldPassword,
      new: newPassword
    }
  )
}
