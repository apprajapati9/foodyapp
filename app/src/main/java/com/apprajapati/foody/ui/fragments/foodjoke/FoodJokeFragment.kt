package com.apprajapati.foody.ui.fragments.foodjoke

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.apprajapati.foody.R
import com.apprajapati.foody.databinding.FragmentFoodJokeBinding
import com.apprajapati.foody.util.Constants.Companion.API_KEY
import com.apprajapati.foody.util.NetworkResult
import com.apprajapati.foody.util.showSnackBar
import com.apprajapati.foody.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : Fragment() {

    private var _binding: FragmentFoodJokeBinding? = null

    private val binding get() = _binding!!

    private val mainModel: MainViewModel by viewModels()

    private var foodJoke = "No Food Joke!"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFoodJokeBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainModel
        setUpMenu()

        binding.textviewFoodJoke.movementMethod = ScrollingMovementMethod()
        getFoodJoke()

        return binding.root
    }

    private fun getFoodJoke() {
        mainModel.getFoodJoke(API_KEY)
        mainModel.foodJokeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val jokeText = response.data?.joke
                    if (jokeText != null) {
                        binding.textviewFoodJoke.text = jokeText
                        foodJoke = jokeText
                    }
                }

                is NetworkResult.Error -> {
                    showSnackBar(binding.root, response.message.toString())
                    loadDataFromCache()
                }

                is NetworkResult.Loading -> {
                    Log.d("FoodJokeFragment", "Loading..")
                }
            }

        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainModel.readFoodJoke.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && database != null) {
                    val currentFoodJoke = database.get(0).foodJoke.joke
                    binding.textviewFoodJoke.text = currentFoodJoke
                    foodJoke = currentFoodJoke
                }
            }
        }
    }

    private fun setUpMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.food_joke_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.share_foodJoke_menu) {
                    val shareIntent = Intent().apply {
                        this.action = Intent.ACTION_SEND
                        this.putExtra(Intent.EXTRA_TEXT, foodJoke)
                        this.type = "text/plain"
                    }
                    startActivity(shareIntent)

                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}