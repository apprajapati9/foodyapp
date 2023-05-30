package com.apprajapati.foody.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apprajapati.foody.data.database.entities.FavoritesEntity
import com.apprajapati.foody.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

//Data Access Object - DAO
@Dao
interface RecipesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(recipesEntity: RecipesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteRecipe(favoriteEntity: FavoritesEntity)

    @Query("SELECT * FROM favorite_recipes_table ORDER BY id ASC")
    fun readFavoritesRecipes(): Flow<List<FavoritesEntity>>

    @Delete
    fun deleteFavoriteRecipe(favoriteEntity: FavoritesEntity)

    @Query("DELETE FROM FAVORITE_RECIPES_TABLE")
    fun deleteAllFavoriteRecipes()
}