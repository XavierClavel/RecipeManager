import axios from "axios";
import router from "@/router";
import apiClient from '@/plugins/axios.js';
import {useAuthStore, declareLogin} from "@/stores/auth";

export {
  base_url,
  login,
  logout,
  signup,

  toCreateRecipe,
  toEditRecipe,
  toViewRecipe,
  toListRecipe,

  toViewUser,
  toEditUser,

  toListIngredient,

  toCreateCookbook,
  toListCookbooks,
  toMyCookbooks,
  toViewCookbookRecipes,

  toHome,
  toUsers,
  toLogin,
  toSignup,
  toMyProfile,
  whoami,

  uploadImage,
  deleteImage,

  noLoginRedirect,
}

const toCreateRecipe = () => router.push(`/recipe/edit`)
const toEditRecipe = (id) => router.push(`/recipe/edit/?id=${id}`)
const toViewRecipe = (id) => router.push(`/recipe/view/?id=${id}`)
const toListRecipe = (search) => router.push(`/recipe/list${search}`)

const toViewUser = (id) => router.push(`/user/view/?id=${id}`)
const toEditUser = (id) => router.push(`/user/edit/?id=${id}`)

const toListIngredient = () => router.push(`/ingredients`)

const toCreateCookbook = () => router.push(`/cookbook/edit`)
const toViewCookbookRecipes = (id) => router.push(`/cookbook/recipes?cookbook=${id}`)
const toListCookbooks = (id) => router.push(`/cookbook/list?user=${id}`)

const toMyCookbooks = () => {
  const authStore = useAuthStore();
  toListCookbooks(authStore.id)
}

const toHome = () => router.push('/home')
const toUsers = () => router.push('/users')
const toLogin = () => router.push('/login')
const toSignup = () => router.push('/signup')

const toMyProfile = () => {
  const authStore = useAuthStore();
  router.push(`/user/view?id=${authStore.id}`)
}

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

async function signup(user) {
  const result = await apiClient.post(`/auth/signup`, user)
  if (result.status == 200) {
    router.push(`/login`)
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

async function uploadImage(id, file, path) {
  let formData = new FormData()
  formData.append('file', file)
  return await apiClient.post( `/${path}/${id}`,
    formData,
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
  ).then(function(response){
    console.log('SUCCESS!!');
    console.log(response)
  })
    .catch(function(error){
      console.log('FAILURE!!');
      console.log(error)
    });
}

async function deleteImage(id, path) {
  return await apiClient.delete(`/${path}/${id}`)
}
