package com.apprajapati.foody.data.network

import com.apprajapati.foody.models.FoodJoke
import com.apprajapati.foody.models.FoodRecipe
import com.apprajapati.foody.util.Constants.Companion.SERVER_API_KEY_URL
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipeApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(@QueryMap queries: Map<String, String>): Response<FoodRecipe>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipe>

    @GET("/food/jokes/random")
    suspend fun getFoodJoke(@Query("apiKey") apiKey : String) : Response<FoodJoke>

    @GET(SERVER_API_KEY_URL)
    suspend fun getApiKey() : Response<ResponseBody>
}