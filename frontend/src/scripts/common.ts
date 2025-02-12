import axios from "axios";
import router from "@/router";
import apiClient from '@/plugins/axios.js';
import {useAuthStore, declareLogin} from "@/stores/auth";

export {
  base_url,
  login,
  logout,
  signup,
  verifyEmail,

  toCreateRecipe,
  toCreateCookbookAddRecipe,
  toEditRecipe,
  toViewRecipe,
  toListRecipe,

  toViewUser,
  toEditUser,

  toViewIngredient,
  toListIngredient,

  toCreateCookbook,
  toEditCookbook,
  toListCookbooks,
  toMyCookbooks,
  toViewCookbook,

  toHome,
  toUsers,
  toLogin,
  toSignup,
  toMaintenance,

  toMyProfile,
  whoami,
  toSettings,

  uploadImage,
  deleteImage,

  noLoginRedirect,

  unitToReadable,

  getUserIconUrl,
  getCookbookIconUrl,
  getRecipeImageUrl,
  getRecipeThumbnailUrl,
}


const toCreateRecipe = () => navigateTo(`/recipe/edit`)
const toEditRecipe = (id) => navigateTo(`/recipe/edit/?id=${id}`)
const toViewRecipe = (id) => navigateTo(`/recipe/view/?id=${id}`)
const toListRecipe = (search) => navigateTo(`/recipe/list${search}`)

const toViewUser = (id) => navigateTo(`/user/view/?user=${id}`)
const toMyProfile = () => {
  const authStore = useAuthStore();
  navigateTo(`/user/view?user=${authStore.id}`)
}
const toEditUser = (id) => navigateTo(`/user/edit?user=${id}`)
const toSettings = () => navigateTo(`/user/settings`)

const toListIngredient = () => navigateTo(`/ingredients`)
const toViewIngredient = (id) => navigateTo(`/ingredient/view?ingredient=${id}`)

const toCreateCookbook = () => navigateTo(`/cookbook/edit`)
const toEditCookbook = (id) => navigateTo(`/cookbook/edit?cookbook=${id}`)
const toCreateCookbookAddRecipe = (id) => navigateTo(`/cookbook/edit?addRecipe=${id}`)
const toViewCookbook = (id) => navigateTo(`/cookbook/view?cookbook=${id}`)
const toListCookbooks = (id) => navigateTo(`/cookbook/list?user=${id}`)

const toMyCookbooks = () => {
  const authStore = useAuthStore();
  toListCookbooks(authStore.id)
}

const toHome = () => navigateTo('/home')
const toUsers = () => navigateTo('/users')
const toLogin = () => navigateTo('/login')
const toSignup = () => navigateTo('/signup')
const toMaintenance = () => navigateTo('/maintenance')

function navigateTo(path) {
  nextTick(() => {
    router.push(path)
  })
}

const noLoginRedirect = ['/login', '/logout', '/signup', '/user/verify', '/maintenance']

const base_url = "http://localhost:8080/v1"
const getUserIconUrl = (id) => id ? `${base_url}/image/users/${id}.webp` : `${base_url}/image/users/placeholder.webp`
const getCookbookIconUrl = (id) => `${base_url}/image/cookbooks/${id}.webp`
const getRecipeImageUrl = (id) => `${base_url}/image/recipes/${id}.webp`
const getRecipeThumbnailUrl = (id) => `${base_url}/image/recipes-thumbnails/${id}.webp`


async function login(user) {
  const result = await apiClient.post(`${base_url}/auth/login`, {}, {
    auth: {
      username: user.username,
      password: user.password,
    },
    withCredentials: true
  })
  if (result.status == 200) {
    navigateTo(`/home`)
    const authStore = useAuthStore();
    authStore.login()
  }
  return result
}

async function signup(user) {
  const result = await apiClient.post(`/auth/signup`, user)
  if (result.status == 200) {
    navigateTo(`/login`)
  }
  return result
}

async function verifyEmail(token) {
  return await apiClient.post(`/auth/verify?token=${token}`)
}

async function logout() {
  const result = await apiClient.post("/auth/logout", {}, {
    withCredentials: true
  })
  if (result.status == 200) {
    navigateTo(`/logout`)
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
    console.log(error)
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

const unitToReadable = (unit) => {
  switch (unit) {
    case "UNIT":
      return ""
    case "GRAM":
      return "g"
    case "NONE":
      return ""
    case null:
      return ""
  }
}
