package com.xavierclavel.services

import common.infodto.DashboardReport
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DashboardService: KoinComponent {
    val recipeService: RecipeService by inject()
    val userService: UserService by inject()
    val cookbookService: CookbookService by inject()
    val likeService: LikeService by inject()
    val followService: FollowService by inject()


    fun buildGeneralReport() = DashboardReport(
        usersCount = userService.countAll(),
        activeUsersCount = userService.countActiveUsers(),
        recipesCount = recipeService.countAll(),
        cookbooksCount = cookbookService.countAll(),
        likesCount = likeService.countAll(),
        followsCount = followService.countAll(),
    )
}