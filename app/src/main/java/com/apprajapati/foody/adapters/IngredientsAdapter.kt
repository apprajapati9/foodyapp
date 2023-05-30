package com.apprajapati.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.apprajapati.foody.databinding.IngredientsItemLayoutBinding
import com.apprajapati.foody.models.ExtendedIngredient
import com.apprajapati.foody.util.Constants.Companion.BASE_IMAGE_URL
import com.apprajapati.foody.util.RecipesDiffUtil


class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientList = emptyList<ExtendedIngredient>()


    class MyViewHolder(val binding: IngredientsItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            IngredientsItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ingredient = ingredientList[position]
        holder.binding.imageViewIngredient.load(BASE_IMAGE_URL + ingredient.image)
        holder.binding.textViewIngredientName.text = ingredient.name.capitalize()
        holder.binding.textViewIngredientAmount.text = ingredient.amount.toString()
        holder.binding.textViewIngredientUnit.text = ingredient.unit
        holder.binding.textViewIngredientConsistency.text = ingredient.consistency
        holder.binding.textViewIngredientOriginal.text = ingredient.original
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    fun setData(newIngredients: List<ExtendedIngredient>) {
        val ingredientsDiffUtil = RecipesDiffUtil(ingredientList, newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientList = newIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }

}