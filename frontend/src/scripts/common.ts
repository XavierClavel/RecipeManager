import axios from "axios";
import router from "@/router";

export {
  base_url,
  login,
  authenticated
}

const base_url = "http://localhost:8080/v1"

let authenticated = false

async function login(user) {
  const result = await axios.post(`${base_url}/auth/login`, {}, {
    auth: {
      username: user.username,
      password: user.password,
    }
  })
  if (result.status == 200) {
    authenticated = true
    router.push(`/home`)
  }
  return result
}
