package com.apprajapati.foody.data

import com.apprajapati.foody.data.database.RecipesDAO
import com.apprajapati.foody.data.database.entities.FavoritesEntity
import com.apprajapati.foody.data.database.entities.FoodJokeEntity
import com.apprajapati.foody.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LocalDataSource @Inject constructor(private val recipesDAO: RecipesDAO) {

    fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDAO.insertRecipes(recipesEntity)
    }

    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDAO.readRecipes()
    }

    fun readFavoriteRecipes(): Flow<List<FavoritesEntity>> {
        return recipesDAO.readFavoritesRecipes()
    }

    fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity) {
        recipesDAO.insertFavoriteRecipe(favoritesEntity)
    }

    fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) {
        recipesDAO.deleteFavoriteRecipe(favoritesEntity)
    }

    fun deleteAllFavoriteRecipes(){
        recipesDAO.deleteAllFavoriteRecipes()
    }

    fun readFoodJoke() : Flow<List<FoodJokeEntity>> {
        return recipesDAO.readFoodJoke()
    }

    fun insertFoodJoke(foodEntity: FoodJokeEntity) {
        return recipesDAO.insertFoodJoke(foodEntity)
    }

}