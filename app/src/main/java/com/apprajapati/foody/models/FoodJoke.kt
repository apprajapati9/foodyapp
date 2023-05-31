package com.apprajapati.foody.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodJoke(
    @SerializedName("text")
    val joke : String
    ): Parcelable