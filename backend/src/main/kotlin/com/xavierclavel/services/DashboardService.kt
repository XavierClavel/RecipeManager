package com.xavierclavel.services

import common.dto.DataPoint
import common.dto.WeeklyUserCountDto
import common.infodto.DashboardReport
import io.ebean.DB
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate

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

    fun getUsersData() : List<WeeklyUserCountDto> {
        val sql = """
            SELECT 
                date_trunc('week', join_date)::date AS week_start,
                COUNT(*) AS user_count
            FROM users
            WHERE join_date >= current_date - INTERVAL '1 year'
            GROUP BY week_start
            ORDER BY week_start
        """.trimIndent()

        val query = DB.findDto(WeeklyUserCountDto::class.java, sql)
        return query.findList()
    }
}