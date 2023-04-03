package com.apprajapati.foody.adapters

import com.apprajapati.foody.models.FoodRecipe
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apprajapati.foody.databinding.RecipeItemLayoutBinding
import com.apprajapati.foody.models.Result

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    private var recipe = emptyList<Result>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return recipe.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentResult = recipe[position]
        holder.bind(currentResult)
    }

    class RecipeViewHolder(private val binding: RecipeItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent : ViewGroup) : RecipeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipeItemLayoutBinding.inflate(layoutInflater, parent, false)
                return RecipeViewHolder(binding)
            }
        }

    }

    fun setData (newData : FoodRecipe) {
        recipe = newData.results
        notifyDataSetChanged() //this is bad for performance of the app because it updates whole list all over again
    }
}