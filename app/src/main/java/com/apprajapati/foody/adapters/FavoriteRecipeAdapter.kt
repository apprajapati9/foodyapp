package com.apprajapati.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.apprajapati.foody.data.database.entities.FavoritesEntity
import com.apprajapati.foody.databinding.FavoriteRecipeItemLayoutBinding
import com.apprajapati.foody.util.RecipesDiffUtil

class FavoriteRecipeAdapter : RecyclerView.Adapter<FavoriteRecipeAdapter.FavRecipeViewHolder>() {

    private var favoriteRecipes = emptyList<FavoritesEntity>()

    class FavRecipeViewHolder(private val binding: FavoriteRecipeItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(favoritesEntity: FavoritesEntity) {
                binding.favoritesEntity = favoritesEntity
                binding.executePendingBindings()
            }

        companion object {
            fun from(parent : ViewGroup) : FavRecipeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipeItemLayoutBinding.inflate(layoutInflater, parent, false)
                return FavRecipeViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavRecipeViewHolder {
        return FavRecipeViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    override fun onBindViewHolder(holder: FavRecipeViewHolder, position: Int) {
        val currentFavRecipe = favoriteRecipes[position]
        holder.bind(currentFavRecipe)
    }

    fun setData(newFavRecipes : List<FavoritesEntity>) {
        val favRecipesDiffUtil = RecipesDiffUtil(favoriteRecipes, newFavRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favRecipesDiffUtil)
        favoriteRecipes = newFavRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }
}