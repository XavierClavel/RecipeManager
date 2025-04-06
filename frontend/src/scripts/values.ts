import {ICON_ALPHABETICAL, ICON_DATE, ICON_LIKES, ICON_RANDOM} from "@/scripts/icons";

export {
  dishClasses,
  sourceOptions,
  sortOptions,
}


const dishClasses = [
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
    value: "SNACKS",
    label: "snacks",
  },
  {
    value: "BREAKFAST",
    label: "breakfast",
  }
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
