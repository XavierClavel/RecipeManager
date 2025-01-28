package common

import common.enums.DishClass

data class RecipeFilter(
    val owner: Long? = null,
    val likedBy: Long? = null,
    val cookbook: Long? = null,
    val cookbookUser: Long? = null,
    val ingredient: Set<Long> = setOf(),
    val dishClasses: Set<DishClass> = setOf(),
) {
    fun hasFilters(): Boolean =
        hasAdditiveFilters() ||
        dishClasses.isNotEmpty() ||
        ingredient.isNotEmpty()

    fun hasAdditiveFilters(): Boolean =
        owner != null ||
        likedBy != null ||
        cookbook != null ||
        cookbookUser != null
}