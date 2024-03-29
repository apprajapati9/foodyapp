package com.apprajapati.foody.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apprajapati.foody.models.FoodRecipe
import com.apprajapati.foody.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(var foodRecipe: FoodRecipe) {

    @PrimaryKey(autoGenerate = false)
    var id: Int = 0

}