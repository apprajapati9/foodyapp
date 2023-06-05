package com.apprajapati.foody.data.database

import androidx.room.TypeConverter
import com.apprajapati.foody.models.FoodRecipe
import com.apprajapati.foody.models.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipeTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe): String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipe {
        val listType = object : TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(data, listType) //check if FoodRecipe::class.java works. -It does work.
    }

    @TypeConverter
    fun resultToString(result : Result) : String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data : String)  : Result {
        val listType = object : TypeToken<Result>() {}.type
        return gson.fromJson(data, listType )
    }

}