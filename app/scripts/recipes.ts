
export {
    getRecipe,
}

const getRecipe = async(id: number) => {
    const response = await fetch('https://cooknco.eu/api/v1/recipe/10?locale=FR')
    const json = await response.json()
    console.log(json)
}

const listRecipes = async() => {
    const response = await fetch('https://cooknco.eu/api/v1/recipe')
    const json = await response.json()
    console.log(json)
}