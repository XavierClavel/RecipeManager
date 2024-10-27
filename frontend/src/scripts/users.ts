import axios from 'axios'

export{
  getUsers
}

const base_url = "http://localhost:8080/v1"

async function getUsers() {
  return await axios.get(`${base_url}/user`)
}
