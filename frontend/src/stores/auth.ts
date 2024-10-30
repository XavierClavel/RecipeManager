import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    isAuthenticated: false,
    user: null, // store other user data if needed
  }),
  actions: {
    login(user) {
      this.isAuthenticated = true;
      this.user = user; // store user info if you have it
      console.log("login")
      console.log(this.isAuthenticated)
    },
    logout() {
      this.isAuthenticated = false;
      this.user = null;
    },
  },
});
