import { defineStore } from 'pinia';
import axios from "axios";
import {base_url, toLogin} from "@/scripts/common";

export const useAuthStore = defineStore('auth', {
  state: () => ({
    isAuthenticated: false
  }),
  actions: {
    logout() {
      this.isAuthenticated = false;
    },
    login() {
      this.isAuthenticated = true
    },
    async checkAuth() {
      try {
        const response = await axios.get(`${base_url}/auth/me`, {
          withCredentials: true  // Ensure cookies are sent with the request
        });
        this.isAuthenticated = response.status === 200;
      } catch {
        this.isAuthenticated = false;
      }
    }
  },
});
