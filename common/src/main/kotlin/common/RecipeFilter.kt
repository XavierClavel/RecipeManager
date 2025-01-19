package common

import common.enums.DishClass

data class RecipeFilter(
    val owner: Long? = null,
    val likedBy: Long? = null,
    val cookbook: Long? = null,
    val cookbookUser: Long? = null,
    val dishClasses: Set<DishClass> = setOf(),
) {
    fun hasFilters(): Boolean = owner != null || cookbook != null || cookbookUser != null || dishClasses.isNotEmpty()
}