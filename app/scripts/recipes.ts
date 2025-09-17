
export {
    getRecipe,
    RecipeInfo,
}
type UserOverview = {
    id: number,
    role: string,
    version: number,
    username: string,
}

type CustomIngredientInfo = {
    name: string,
    amount: number,
    unit: string,
}

type RecipeIngredientInfo = {
    id: number,
    name: string,
    amount: number,
    unit: string,
    complement: string,
    type: string,
    allowAmount: boolean,
    allowWeight: boolean,
    allowVolume: boolean,
}

class RecipeInfo {
    constructor(
        public id: number =-1,
        public version: number = -1,
        public title: string = '',
        public dishClass: string = '',
        public owner: UserOverview,
        public description: string,

        public preparationTime: number,
        public cookingTime: number,
        public cookingTemperature: number,

        public ingredients: RecipeIngredientInfo[],
        public customIngredients: CustomIngredientInfo[],
        public steps: string[],
        public tips: string,

        public creationDate: number,
        public editionDate: number,

        public likesCount: number,
    ) {}
}

async function getRecipe(id: number) {
    console.log("making rest call")
    const response = await fetch(`https://cooknco.eu/api/v1/recipe/${id}?locale=FR`)
    const json = await response.json()
    console.log(json)
    return json as RecipeInfo
}

const listRecipes = async() => {
    const response = await fetch('https://cooknco.eu/api/v1/recipe')
    const json = await response.json()
    console.log(json)
}