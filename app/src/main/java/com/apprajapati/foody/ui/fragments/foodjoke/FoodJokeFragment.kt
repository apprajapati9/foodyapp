package com.apprajapati.foody.ui.fragments.foodjoke

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.apprajapati.foody.R
import com.apprajapati.foody.databinding.FragmentFoodJokeBinding
import com.apprajapati.foody.ui.BaseFragment
import com.apprajapati.foody.util.Constants.Companion.API_KEY
import com.apprajapati.foody.util.NetworkResult
import com.apprajapati.foody.util.showSnackBar
import com.apprajapati.foody.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : BaseFragment<FragmentFoodJokeBinding>(FragmentFoodJokeBinding::inflate) {

    private val mainModel: MainViewModel by viewModels()

    private var foodJoke = "No Food Joke!"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainModel
        setUpMenu()

        binding.textviewFoodJoke.movementMethod = ScrollingMovementMethod()
        getFoodJoke()
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

                menu.findItem(R.id.share_foodJoke_menu).icon?.setTint(
                    ContextCompat.getColor(
                        requireContext(), R.color.white
                    )
                )
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
}