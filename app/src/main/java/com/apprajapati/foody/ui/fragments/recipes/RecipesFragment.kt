package com.apprajapati.foody.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.apprajapati.foody.R
import com.apprajapati.foody.adapters.RecipesAdapter
import com.apprajapati.foody.databinding.RecipeFragmentBinding
import com.apprajapati.foody.ui.BaseFragment
import com.apprajapati.foody.util.NetworkListener
import com.apprajapati.foody.util.NetworkResult
import com.apprajapati.foody.util.observeOnce
import com.apprajapati.foody.viewmodels.MainViewModel
import com.apprajapati.foody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class RecipesFragment : BaseFragment<RecipeFragmentBinding>(RecipeFragmentBinding::inflate),
    SearchView.OnQueryTextListener {

    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner =
            viewLifecycleOwner // viewlifecycleowner instead of this - warning appears if you use this - can cause memory leak.
        binding.mainViewModel = mainViewModel
        setUpMenu()
        setupRecyclerView()
        //readDatabase()

        recipesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipesViewModel.backOnline = it
        }

        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                networkListener = NetworkListener()
                networkListener.checkNetworkAvailability(requireContext()).collect { status ->
                    Log.d("NetworkListener", status.toString())
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    readDatabase()
                }
            }
        }

        binding.floatingRecipeFilterButton.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipeFragment_to_recipeBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }

        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !recipesViewModel.backFromBottomSheet) {
                    Log.d("RecipesFragment", "ReadDatabase Called.")
                    mAdapter.setData(database[0].foodRecipe)
                    hideLoadingEffect()
                } else {
                    fetchApiKey()
                    Log.d("Ajay", "fetchApiKey() method call from readDatabase else.")
                    //requestApiData()
                    recipesViewModel.backFromBottomSheet = false
                }
            }
        }
    }

    fun fetchApiKey() {

        mainViewModel.apiKey.observe(viewLifecycleOwner) { key ->
            Log.d("Ajay", "apikey observer called.")
            if (!key.isEmpty()) {
                Log.d(
                    "Ajay", "apikey observer key not empty ${key}, calling requestApiData()"
                ) // TODO: remove log in release build.
                requestApiData()
            }
        }
    }

    private fun requestApiData() {
        Log.d("RecipesFragment", "RequestApiData Called.")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())

        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data.let { mAdapter.setData(it!!) }
                    hideLoadingEffect()
                    recipesViewModel.saveMealAndDietType()
                }

                is NetworkResult.Error -> {
                    hideLoadingEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(), response.message.toString(), Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    showLoadingEffect()
                }
            }
        }
    }

    private fun showLoadingEffect() {
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun hideLoadingEffect() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun loadDataFromCache() {
        mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
            if (database.isNotEmpty()) {
                mAdapter.setData(database.first().foodRecipe)
            }
        }
    }

    private fun searchApiData(searchQuery: String) {
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchRecipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val foodRecipe = response.data
                    foodRecipe?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(), response.message.toString(), Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {

                }
            }
        }
    }

    private fun setUpMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.recipes_menu, menu)

                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@RecipesFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}