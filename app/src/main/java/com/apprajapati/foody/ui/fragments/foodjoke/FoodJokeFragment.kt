package com.apprajapati.foody.ui.fragments.foodjoke

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFoodJokeBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainModel

        getFoodJoke()

        return binding.root
    }

    private fun getFoodJoke() {
        mainModel.getFoodJoke(API_KEY)
        mainModel.foodJokeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.textviewFoodJoke.text = response.data?.joke
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
                    binding.textviewFoodJoke.text = database.get(0).foodJoke.joke
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}