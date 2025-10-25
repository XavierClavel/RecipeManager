package shared

import shared.enums.DishClass

data class RecipeFilter(
    val user: Long? = null,
    val likedBy: Long? = null,
    val cookbook: Long? = null,
    val cookbookUser: Long? = null,
    val followedBy: Long? = null,
    val search: String? = null,
    val ingredient: Set<Long> = setOf(),
    val dishClasses: Set<DishClass> = setOf(),
) {
    fun hasFilters(): Boolean =
        hasAdditiveFilters() ||
        dishClasses.isNotEmpty() ||
        ingredient.isNotEmpty() ||
        !search.isNullOrBlank()

    fun hasAdditiveFilters(): Boolean =
        user != null ||
        likedBy != null ||
        cookbook != null ||
        cookbookUser != null ||
        followedBy != null
}