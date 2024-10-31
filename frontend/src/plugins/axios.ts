// src/axios.js
import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/v1', // your Ktor server base URL
  withCredentials: true, // to include cookies for session-based auth
  headers: {
    'Content-Type': 'application/json',
  },
});

export default apiClient;
