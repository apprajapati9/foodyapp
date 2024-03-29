package com.apprajapati.foody.adapters

import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.apprajapati.foody.R
import com.apprajapati.foody.data.database.entities.FavoritesEntity
import com.apprajapati.foody.databinding.FavoriteRecipeItemLayoutBinding
import com.apprajapati.foody.ui.fragments.favorites.FavoriteRecipesFragmentDirections
import com.apprajapati.foody.util.RecipesDiffUtil
import com.apprajapati.foody.util.showSnackBar
import com.apprajapati.foody.viewmodels.MainViewModel
import com.google.android.material.card.MaterialCardView
class FavoriteRecipeAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) :
    RecyclerView.Adapter<FavoriteRecipeAdapter.FavRecipeViewHolder>(),
    ActionMode.Callback {

    private var multiSelection = false
    private var selectedFoodRecipes = arrayListOf<FavoritesEntity>()
    private var myViewHolders = arrayListOf<FavRecipeViewHolder>()

    private var favoriteRecipes = emptyList<FavoritesEntity>()

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    class FavRecipeViewHolder(val binding: FavoriteRecipeItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoritesEntity: FavoritesEntity) {
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FavRecipeViewHolder {
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
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView

        val currentFavRecipe = favoriteRecipes[position]
        holder.bind(currentFavRecipe)

        saveItemStateOnScroll(currentFavRecipe, holder)

        /*
        * This is another way to handle onClick method, BindingAdapter method - \
        * onFavoriteRecipeClickListener in FavoritesRecipeBinding does the same thing.
        *
        * Single Click Listener
        *  */
        holder.binding.favoriteRecipeRawLayout
            .setOnClickListener {

                if (multiSelection) {
                    applySelection(holder, currentFavRecipe)
                } else {
                    val action =
                        FavoriteRecipesFragmentDirections.actionFavRecipeFragmentToDetailsActivity(
                            currentFavRecipe.result
                        )
                    holder.itemView.findNavController().navigate(action)
                }
            }
        /*
        * Long click listener
        * */
        holder.binding.favoriteRecipeRawLayout
            .setOnLongClickListener {
                if (!multiSelection) {
                    multiSelection = true
                    requireActivity.startActionMode(this)
                    applySelection(holder, currentFavRecipe)
                    true
                } else {
                    applySelection(holder, currentFavRecipe)
                    true
                }

            }
    }

    private fun saveItemStateOnScroll(currentRecipe: FavoritesEntity, holder: FavRecipeViewHolder) {
        if (selectedFoodRecipes.contains(currentRecipe)) {
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
        } else {
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
    }


    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorite_contextual_menu, menu)
        mActionMode = mode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    private fun applyActionModeTitle() {
        when (selectedFoodRecipes.size) {
            0 -> {
                mActionMode.finish()
                multiSelection = false
            }

            1 -> {
                mActionMode.title = requireActivity.getString(
                    R.string.one_item_selected_message,
                    selectedFoodRecipes.size
                )
            }

            else -> {
                mActionMode.title = requireActivity.getString(
                    R.string.more_item_selected_message,
                    selectedFoodRecipes.size
                )

            }
        }
    }

    private fun applySelection(holder: FavRecipeViewHolder, currentRecipe: FavoritesEntity) {
        if (selectedFoodRecipes.contains(currentRecipe)) {
            selectedFoodRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedFoodRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(
        holder: FavRecipeViewHolder,
        backgroundColor: Int,
        strokeColor: Int
    ) {
        val constraintView =
            holder.itemView.findViewById<ConstraintLayout>(R.id.favorite_recipe_raw_layout)

        constraintView.setBackgroundColor(ContextCompat.getColor(requireActivity, backgroundColor))

        val cardView = holder.itemView.findViewById<MaterialCardView>(R.id.favorite_item_cardView)
        cardView.strokeColor = ContextCompat.getColor(requireActivity, strokeColor)
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_fav_recipe) {
            selectedFoodRecipes.forEach {
                mainViewModel.deleteFavoriteRecipe(it)
            }
            showSnackBar(rootView,
                requireActivity.getString(
                    R.string.recipe_remove_counter_message,
                    selectedFoodRecipes.size
                )
            )

            multiSelection = false
            selectedFoodRecipes.clear()
            mActionMode.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        multiSelection = false
        selectedFoodRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)

        myViewHolders.forEach { holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
    }


    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }

    fun setData(newFavRecipes: List<FavoritesEntity>) {
        val favRecipesDiffUtil = RecipesDiffUtil(favoriteRecipes, newFavRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favRecipesDiffUtil)
        favoriteRecipes = newFavRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }
}