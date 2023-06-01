package com.apprajapati.foody.bindingadapters

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.apprajapati.foody.data.database.entities.FoodJokeEntity
import com.apprajapati.foody.models.FoodJoke
import com.apprajapati.foody.util.NetworkResult
import com.google.android.material.card.MaterialCardView

class FoodJokeBinding {

    companion object {

        @BindingAdapter("readApiResponseFoodJoke", "readDatabaseFoodJoke", requireAll = false)
        @JvmStatic
        fun setCardAndProgressVisibility(
            view: View,
            readApiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            when (readApiResponse) {
                is NetworkResult.Loading -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = View.VISIBLE
                        }

                        is MaterialCardView -> {
                            view.visibility = View.INVISIBLE
                        }
                    }
                }

                is NetworkResult.Error -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }

                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                            if (database != null) {
                                if (database.isEmpty()) {
                                    view.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }
                }

                is NetworkResult.Success -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }

                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                        }
                    }
                }

                else -> {}
            }
        }

        @BindingAdapter("readApiResponseError", "readDatabaseError", requireAll = false)
        @JvmStatic
        fun setErrorViewsVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            if(database != null){
                if (database.isEmpty()) {
                    view.visibility = View.VISIBLE
                    if (view is TextView) {
                        if (apiResponse != null) {
                            view.text = apiResponse.message.toString()
                        }
                    }
                }
            }

            if (apiResponse is NetworkResult.Success) {
                view.visibility = View.INVISIBLE
            }
        }

    }
}