package com.xavierclavel.config

import com.xavierclavel.models.Ingredient
import com.xavierclavel.services.IngredientService
import com.xavierclavel.services.RecipeService
import com.xavierclavel.services.UserService
import org.koin.dsl.module

val appModules = module {
    single { RecipeService() }
    single { UserService() }
    single { IngredientService() }
}