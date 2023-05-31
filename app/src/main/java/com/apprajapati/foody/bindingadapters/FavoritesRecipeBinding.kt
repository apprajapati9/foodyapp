package com.apprajapati.foody.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.apprajapati.foody.adapters.FavoriteRecipeAdapter
import com.apprajapati.foody.data.database.entities.FavoritesEntity
import com.apprajapati.foody.models.Result
import com.apprajapati.foody.ui.fragments.favorites.FavoriteRecipesFragmentDirections
import java.lang.Exception

class FavoritesRecipeBinding {

    companion object {

        @BindingAdapter("viewVisibility", "setData", requireAll = false )
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favoritesEntity: List<FavoritesEntity>?,
            mAdapter: FavoriteRecipeAdapter?
        ) {
            if (favoritesEntity.isNullOrEmpty()) {
                when(view) {
                    is TextView -> {
                        view.visibility = View.VISIBLE
                    }
                    is ImageView -> {
                        view.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            }else{
                when(view) {
                    is TextView -> {
                        view.visibility = View.GONE
                    }
                    is ImageView -> {
                        view.visibility = View.GONE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        mAdapter?.setData(favoritesEntity)
                    }
                }
            }
        }

        @BindingAdapter("onFavoriteRecipeClickListener")
        @JvmStatic
        fun onFavoriteRecipeClickListener(favRecipeRowLayout: ConstraintLayout, result: Result) {
            favRecipeRowLayout.setOnClickListener {
                try {
                    val action =
                        FavoriteRecipesFragmentDirections.actionFavRecipeFragmentToDetailsActivity(
                            result
                        )

                    favRecipeRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.d("onFavoriteRecipeClickListener", e.message.toString())
                }
            }
        }

    }
}