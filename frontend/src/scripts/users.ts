import axios from 'axios'
import {base_url} from "@/scripts/common";

export{
  getUsers,
  deleteUser,
}



async function getUsers() {
  return await axios.get(`${base_url}/user`)
}

async function deleteUser(username) {
  return await axios.delete(`${base_url}/user/${username}`)
}
