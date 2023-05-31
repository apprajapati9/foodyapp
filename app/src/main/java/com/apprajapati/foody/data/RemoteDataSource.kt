package com.apprajapati.foody.data

import com.apprajapati.foody.models.FoodRecipe
import com.apprajapati.foody.data.netweork.FoodRecipeApi
import com.apprajapati.foody.models.FoodJoke
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor( private val foodRecipeApi: FoodRecipeApi) {

    suspend fun getRecipes(queries: Map<String, String>) : Response<FoodRecipe> {
        return foodRecipeApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>) : Response<FoodRecipe> {
        return foodRecipeApi.searchRecipes(searchQuery)
    }

    suspend fun getFoodJoke(apiKey : String) : Response<FoodJoke> {
        return foodRecipeApi.getFoodJoke(apiKey)
    }
}