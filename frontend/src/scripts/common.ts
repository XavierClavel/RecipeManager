import axios from "axios";
import router from "@/router";
import apiClient from '@/plugins/axios.js';
import {useAuthStore, declareLogin} from "@/stores/auth";
import {deleteCookie, getCookie} from "@/scripts/cookies";

export {
  login,
  logout,
  signup,
  verifyEmail,
  resetPassword,
  toResetPasswordEmailSent,

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

  toResetPassword,
  toUpdatePassword,

  toHome,
  toUsers,
  toLogin,
  toSignup,
  toMaintenance,

  toMyProfile,
  toSettings,

  uploadImage,
  deleteImage,

  noLoginRedirect,
  noLoginRedirectStartsWith,
  allowNoLoginStartsWith,

  unitToReadable,

  getUserIconUrl,
  getCookbookIconUrl,
  getRecipeImageUrl,
  getRecipeThumbnailUrl,

  getHealth,
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

const toListIngredient = () => navigateTo(`/ingredient/list`)
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
const toResetPassword = () => navigateTo('/password/reset')
const toUpdatePassword = () => navigateTo('/password/update')
const toResetPasswordEmailSent = () => navigateTo('/password/reset/email')

function navigateTo(path) {
  nextTick(() => {
    router.push(path)
  })
}

const noLoginRedirect = [
  '/login',
  '/logout',
  '/signup',
  '/user/verify',
  '/maintenance',
  '/verification-email-sent',
]
const noLoginRedirectStartsWith = [
  '/password/reset'
]

const allowNoLoginStartsWith = [
  '/recipe/view',
]


const getUserIconUrl = (id) => id ? `${import.meta.env.VITE_API_URL}/image/users/${id}.webp` : `/image/users/placeholder.webp`
const getCookbookIconUrl = (id) => `${import.meta.env.VITE_API_URL}/image/cookbooks/${id}.webp`
const getRecipeImageUrl = (id) => `${import.meta.env.VITE_API_URL}/image/recipes/${id}.webp`
const getRecipeThumbnailUrl = (id) => `${import.meta.env.VITE_API_URL}/image/recipes-thumbnails/${id}.webp`


async function login(user) {
  const result = await apiClient.post(`/auth/login`, {}, {
    auth: {
      username: user.username,
      password: user.password,
    },
    withCredentials: true
  })
  if (result.status == 200) {
    const redirectPath = getCookie("redirectedFrom")
    if (redirectPath) {
      navigateTo(redirectPath)
      deleteCookie("redirectedFrom")
    } else {
      toHome()
    }
    const authStore = useAuthStore();
    authStore.login()
  }
  return result
}

async function signup(user) {
  const result = await apiClient.post(`/auth/signup`, user)
  if (result.status == 201) {
    navigateTo(`/verification-email-sent`)
  }
  return result
}

async function verifyEmail(token) {
  return await apiClient.post(`/auth/verify?token=${token}`)
}

async function resetPassword(mail) {
  return await apiClient.get(`/auth/password/reset/${mail}`)
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


async function getHealth() {
  return await apiClient.get(`/health`)
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


