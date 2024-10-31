import axios from "axios";
import router from "@/router";
import apiClient from '@/plugins/axios.js';
import {useAuthStore, declareLogin} from "@/stores/auth";

export {
  base_url,
  login,
  logout,
  toCreateRecipe,
  toEditRecipe,
  toViewRecipe,
  toListRecipe,
  toHome,
  toUsers,
  toLogin,
  toSignup,
  whoami,
  noLoginRedirect,
}

const toCreateRecipe = () => router.push(`/recipe/edit`)
const toEditRecipe = (id) => router.push(`/recipe/edit/?id=${id}`)
const toViewRecipe = (id) => router.push(`/recipe/view/?id=${id}`)
const toListRecipe = () => router.push(`/recipe/list`)
const toHome = () => router.push('/home')
const toUsers = () => router.push('/users')
const toLogin = () => router.push('/login')
const toSignup = () => router.push('/signup')

const noLoginRedirect = ['/login', '/logout', '/signup']

const base_url = "http://localhost:8080/v1"

async function login(user) {
  const result = await apiClient.post(`${base_url}/auth/login`, {}, {
    auth: {
      username: user.username,
      password: user.password,
    },
    withCredentials: true
  })
  if (result.status == 200) {
    router.push(`/home`)
    const authStore = useAuthStore();
    authStore.login()
  }
  return result
}

async function logout() {
  const result = await apiClient.post("/auth/logout", {}, {
    withCredentials: true
  })
  if (result.status == 200) {
    router.push(`/logout`)
    const authStore = useAuthStore();
    authStore.logout()
  }
  return result
}

async function whoami() {
  const result = await apiClient.get(`${base_url}/auth/me`).then( function (response) {
    authStore.login({ username: 'test_user' });
    }
  ).catch (function (error) {
    console.log("logged out")
    const authStore = useAuthStore();
    authStore.logout()
    toLogin()
  })

}
