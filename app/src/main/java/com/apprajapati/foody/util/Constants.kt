package com.apprajapati.foody.util

class Constants {

    companion object {
        var API_KEY = ""
        const val BASE_URL = "https://api.spoonacular.com"
        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
        const val FOOD_JOKE_ENDPOINT = BASE_URL + "/food/jokes/random"

        const val SERVER_API_KEY_URL = "https://drive.google.com/uc?id=1ZBC_k41sjurvymqm07vdDQp9ENkAWyUx"

        //recipe details safe arg bundle name
        const val RECIPE_BUNDLE_KEY = "recipeBundle"

        //API query keys
        const val QUERY_SEARCH = "query"
        const val QUERY_NUMBER = "number"
        const val QUERY_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        //ROOM database
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"
        const val FAVORITE_RECIPES_TABLE = "favorite_recipes_table"
        const val FOOD_JOKE_TABLE = "food_joke_table"

        //bottomSheet preferences
        const val PREFERENCES_NAME = "foody_preferences"
        const val DEFAULT_RECIPE_NUMBER = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "vegetarian"

        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCES_DIET_TYPE = "dietType"
        const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"

        const val PREFERENCE_BACK_ONLINE = "backOnline"
    }
}

//To test API key url =  https://api.spoonacular.com/food/jokes/random?apiKey=47ca10b38f6b4cb1b8bc2beb51d1e286