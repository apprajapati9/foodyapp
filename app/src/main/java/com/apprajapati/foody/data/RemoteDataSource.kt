package com.apprajapati.foody.data

import com.apprajapati.foody.models.FoodRecipe
import com.apprajapati.foody.data.netweork.FoodRecipeApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor( private val foodRecipeApi: FoodRecipeApi) {

    suspend fun getRecipes(queries: Map<String, String>) : Response<FoodRecipe> {
        return foodRecipeApi.getRecipes(queries)
    }
}