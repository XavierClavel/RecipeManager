// src/axios.js
import axios from 'axios';
import {toHome, toMaintenance, toSignup} from "@/scripts/common";

const instance = axios.create()

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/v1', // your Ktor server base URL
  withCredentials: true, // to include cookies for session-based auth
  headers: {
    'Content-Type': 'application/json',
  },
});

apiClient.interceptors.request.use(
  function (config) {
    console.log("here")
    return config
  },
  function (error) {
    console.log("here2")
    return Promise.reject(error)
  }
)



apiClient.interceptors.response.use(
  function (response){
    console.log(response.request.responseURL)
    console.log("intercepted !")
    return response
  },
  function (error) {
    console.log(error)
    console.log("intercepted error!")
    console.log(error.status)
    if (error.code == "ERR_NETWORK") {
      console.log("backend down")
      toMaintenance()
    } else {
      return Promise.reject(error)
    }
  }
);

export default apiClient;
