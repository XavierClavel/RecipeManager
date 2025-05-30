package com.xavierclavel.config

import com.xavierclavel.plugins.RedisService
import com.xavierclavel.services.CookbookService
import com.xavierclavel.services.CustomIngredientService
import com.xavierclavel.services.DashboardService
import com.xavierclavel.services.EncryptionService
import com.xavierclavel.services.ExportService
import com.xavierclavel.services.FollowService
import com.xavierclavel.services.ImageService
import com.xavierclavel.services.IngredientService
import com.xavierclavel.services.LikeService
import com.xavierclavel.services.MailService
import com.xavierclavel.services.RecipeIngredientService
import com.xavierclavel.services.RecipeService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.loadConfig
import org.koin.dsl.module

val config = loadConfig()
val appModules = module {
    single { RecipeService() }
    single { UserService() }
    single { IngredientService() }
    single { ImageService() }
    single { ExportService() }
    single { LikeService() }
    single { CookbookService() }
    single { DashboardService() }
    single { RecipeIngredientService() }
    single { CustomIngredientService() }
    single { FollowService() }
    single { MailService() }
    single { RedisService(getProperty("redis.url", "redis://:${System.getenv("REDIS_PASSWORD")}@cooknco-redis:6379")) }
    single { config }
    single { EncryptionService() }
}