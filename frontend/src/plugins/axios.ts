// src/axios.js
import axios from 'axios';
import {toMaintenance} from "@/scripts/common";

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
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
    if (error.code == "ERR_NETWORK" || error.status == 502) {
      console.log("backend down")
      toMaintenance()
    } else {
      return Promise.reject(error)
    }
  }
);

export default apiClient;
