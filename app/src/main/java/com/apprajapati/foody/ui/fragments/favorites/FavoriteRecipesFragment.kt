package com.apprajapati.foody.ui.fragments.favorites

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.apprajapati.foody.R
import com.apprajapati.foody.adapters.FavoriteRecipeAdapter
import com.apprajapati.foody.databinding.FragmentFavRecipeBinding
import com.apprajapati.foody.ui.BaseFragment
import com.apprajapati.foody.util.showSnackBar
import com.apprajapati.foody.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class FavoriteRecipesFragment :
    BaseFragment<FragmentFavRecipeBinding>(FragmentFavRecipeBinding::inflate) {

    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter: FavoriteRecipeAdapter by lazy {
        FavoriteRecipeAdapter(
            requireActivity(), mainViewModel
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter

        setUpMenu()

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.favRecipeRecyclerView.adapter = mAdapter
        binding.favRecipeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setUpMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.favorite_recipes_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.delete_all_fav_recipe_menu) {
                    mainViewModel.deleteAllFavoriteRecipe()
                    showSnackBar(
                        binding.root, getString(R.string.snack_bar_all_recipe_removed_message)
                    )
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAdapter.clearContextualActionMode()
    }
}