import apiClient from '@/plugins/axios.js';

export{
  getUser,
  updateUser,
  getUsers,
  deleteUser,
  searchUsers,
  updatePassword,
}

async function getUser(id) {
  return await apiClient.get(`/user/${id}`)
}


async function updateUser(id, user) {
  return await apiClient.put(`/user/${id}`, user)
}

async function getUsers() {
  return await apiClient.get(`/user`)
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
