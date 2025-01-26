import axios from "axios";
import router from "@/router";
import apiClient from '@/plugins/axios.js';
import {useAuthStore, declareLogin} from "@/stores/auth";

export {
  ICON_SAVE,
  ICON_DELETE,

  ICON_RECIPE_YIELD,

  ICON_COOKBOOK_RECIPES,
  ICON_COOKBOOK_USERS,

  ICON_USER_RECIPES,
  ICON_USER_LIKES,
  ICON_USER_FOLLOWS,
  ICON_USER_FOLLOWERS,

  getIngredientIcon,
}

const ICON_SAVE = "mdi-save"
const ICON_DELETE = "mdi-delete"

const ICON_RECIPE_YIELD = "mdi-silverware-fork-knife"

const ICON_COOKBOOK_RECIPES = "mdi-silverware-fork-knife"
const ICON_COOKBOOK_USERS = "mdi-account-multiple"

const ICON_USER_LIKES = "mdi-heart"
const ICON_USER_RECIPES = "mdi-notebook"
const ICON_USER_FOLLOWS = "mdi-account-heart"
const ICON_USER_FOLLOWERS = "mdi-account-multiple"

const ICON_INGREDIENT_CHEESE = "mdi-cheese"
const ICON_INGREDIENT_MEAT = "mdi-food-drumstick"
const ICON_INGREDIENT_VEGETABLE = "mdi-carrot"
const ICON_INGREDIENT_FRUIT = "mdi-food-apple"
const ICON_INGREDIENT_FISH = "mdi-fish"
const ICON_INGREDIENT_SPICE = "mdi-shaker-outline"

const getIngredientIcon = (ingredientType) => {
  switch (ingredientType) {
    case "VEGETABLE":
      return ICON_INGREDIENT_VEGETABLE
    case "FRUIT":
      return ICON_INGREDIENT_FRUIT
    case "MEAT":
      return ICON_INGREDIENT_MEAT
    case "FISH":
      return ICON_INGREDIENT_FISH
    case "CHEESE":
      return ICON_INGREDIENT_CHEESE
  }
}

