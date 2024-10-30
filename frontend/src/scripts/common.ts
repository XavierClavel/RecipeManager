import axios from "axios";
import router from "@/router";
import app from "@/App.vue";
import {useAuthStore} from "@/stores/auth";

export {
  base_url,
  login,
  toCreateRecipe,
  toEditRecipe,
  toViewRecipe,
  toListRecipe,
  toHome,
  toUsers,
  toLogin,
  toSignup,
}

const toCreateRecipe = () => router.push(`/recipe/edit`)
const toEditRecipe = (id) => router.push(`/recipe/edit/?id=${id}`)
const toViewRecipe = (id) => router.push(`/recipe/view/?id=${id}`)
const toListRecipe = () => router.push(`/recipe/list`)
const toHome = () => router.push('/home')
const toUsers = () => router.push('/users')
const toLogin = () => router.push('/login')
const toSignup = () => router.push('/signup')

const base_url = "http://localhost:8080/v1"

async function login(user) {
  const result = await axios.post(`${base_url}/auth/login`, {}, {
    auth: {
      username: user.username,
      password: user.password,
    }
  })
  if (result.status == 200) {
    const authStore = useAuthStore();
    authStore.login({ username: 'test_user' });
    router.push(`/home`)
  }
  return result
}
