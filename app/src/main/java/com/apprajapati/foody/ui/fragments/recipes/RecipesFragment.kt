package com.apprajapati.foody.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.apprajapati.foody.R
import com.apprajapati.foody.viewmodels.MainViewModel
import com.apprajapati.foody.adapters.RecipesAdapter
import com.apprajapati.foody.databinding.RecipeFragmentBinding
import com.apprajapati.foody.util.NetworkListener
import com.apprajapati.foody.util.NetworkResult
import com.apprajapati.foody.util.observeOnce
import com.apprajapati.foody.util.showSnackBar
import com.apprajapati.foody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {

    private val args by navArgs<RecipesFragmentArgs>()

    private var _binding: RecipeFragmentBinding? = null
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = RecipeFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        setUpMenu()
        setupRecyclerView()
        //readDatabase()

        recipesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipesViewModel.backOnline = it
        }

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect { status ->
                Log.d("NetworkListener", status.toString())
                recipesViewModel.networkStatus = status
                recipesViewModel.showNetworkStatus()
                readDatabase()
            }
        }

        // requestApiData()

        binding.floatingRecipeFilterButton.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipeFragment_to_recipeBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }

        }

        return binding.root

    }

    private fun setupRecyclerView() {
        _binding!!.recyclerView.adapter = mAdapter
        _binding!!.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d("RecipesFragment", "ReadDatabase Called.")
                    mAdapter.setData(database[0].foodRecipe)
                    hideLoadingEffect()
                } else {
                    requestApiData()
                }
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
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
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
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }
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
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}