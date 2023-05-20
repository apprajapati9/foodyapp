package com.apprajapati.foody.util

class Constants {

    companion object {
        const val API_KEY = "c684670c93354f94a39f1fc81cffdbc0"
        const val BASE_URL = "https://api.spoonacular.com"

        //API query keys
        const val QUERY_NUMBER = "number"
        const val QUERY_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        //ROOM database
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"
    }
}