import axios from 'axios'

export{
  getUsers,
  deleteUser,
}

const base_url = "http://localhost:8080/v1"

async function getUsers() {
  return await axios.get(`${base_url}/user`)
}

async function deleteUser(username) {
  return await axios.delete(`${base_url}/user/${username}`)
}
