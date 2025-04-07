import {
  ICON_ALPHABETICAL,
  ICON_AMOUNT,
  ICON_DATE,
  ICON_LIKES, ICON_NONE,
  ICON_RANDOM, ICON_VISIBILITY_PRIVATE, ICON_VISIBILITY_PROTECTED, ICON_VISIBILITY_PUBLIC,
  ICON_VOLUME,
  ICON_WEIGHT
} from "@/scripts/icons";

export {
  dishOptions,
  sourceOptions,
  sortOptions,
  unitOptions,
  visibilityOptions,
}


const dishOptions = [
  {
    value: "ENTREE",
    label: "entree",
  },
  {
    value: "MAIN_DISH",
    label: "plat",
  },
  {
    value: "DESERT",
    label: "desert",
  },
  {
    value: "SALTY_SNACKS",
    label: "salty_snacks",
  },
  {
    value: "SUGARY_SNACKS",
    label: "sugary_snacks",
  },
  {
    value: "DRINKS",
    label: "drinks",
  },
]


const sourceOptions = ref([
  {label: 'my_recipes', value: "user"},
  {label: 'likes', value: "likedBy"},
  {label: 'cookbooks', value: "userCookbooks"},
  {label: 'follows', value: "followedBy"},
])

const sortOptions = ref([
  { label: 'alphabetical', value: "NAME", icon:ICON_ALPHABETICAL, ordered: true },
  { label: 'random', value: "RANDOM", icon:ICON_RANDOM, ordered: false },
  { label: 'likes', value: "LIKES", icon:ICON_LIKES, ordered: true },
  { label: 'date', value: "DATE", icon:ICON_DATE, ordered: true },
]);

const unitOptions = ref([
  { label: 'none', value: "NONE", icon: ICON_NONE},
  { label: 'unit', value: "UNIT", icon: ICON_AMOUNT},
  { label: 'gram', value: 'GRAM', icon: ICON_WEIGHT},
  { label: 'pound', value: 'POUND', icon: ICON_WEIGHT},
  { label: 'milliliters', value: 'MILLILITERS', icon: ICON_VOLUME},
  { label: 'teaspoon', value: 'TEASPOON', icon: ICON_VOLUME},
  { label: 'tablespoon', value: 'TABLESPOON', icon: ICON_VOLUME},
  { label: 'cup', value: 'CUP', icon: ICON_VOLUME},
])

const visibilityOptions = ref([
  {
    label: 'Private',
    value: 'PRIVATE',
    icon: ICON_VISIBILITY_PRIVATE,
  },
  {
    label: 'Protected',
    value: 'PROTECTED',
    icon: ICON_VISIBILITY_PROTECTED,
  },
  {
    label: 'Public',
    value: 'PUBLIC',
    icon: ICON_VISIBILITY_PUBLIC,
  },
])
