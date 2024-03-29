package com.apprajapati.foody.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apprajapati.foody.models.Result
import com.apprajapati.foody.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)