import axios from "axios";
import router from "@/router";
import apiClient from '@/plugins/axios.js';
import {useAuthStore, declareLogin} from "@/stores/auth";

export {
  ICON_RECIPE_YIELD,

  ICON_COOKBOOK_RECIPES,
  ICON_COOKBOOK_USERS,

  ICON_USER_RECIPES,
  ICON_USER_LIKES,
  ICON_USER_FOLLOWS,
  ICON_USER_FOLLOWERS,
}

const ICON_RECIPE_YIELD = "mdi-silverware-fork-knife"

const ICON_COOKBOOK_RECIPES = "mdi-account-multiple"
const ICON_COOKBOOK_USERS = "mdi-account-multiple"

const ICON_USER_LIKES = "mdi-heart"
const ICON_USER_RECIPES = "mdi-notebook"
const ICON_USER_FOLLOWS = "mdi-account-heart"
const ICON_USER_FOLLOWERS = "mdi-account-multiple"
