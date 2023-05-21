package com.apprajapati.foody.data

import com.apprajapati.foody.data.database.RecipesDAO
import com.apprajapati.foody.data.database.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LocalDataSource @Inject constructor(private val recipesDAO: RecipesDAO){

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDAO.insertRecipes(recipesEntity)
    }

    suspend fun readDatabase() : Flow<List<RecipesEntity>> {
        return recipesDAO.readRecipes()
    }
}