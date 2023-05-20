package com.apprajapati.foody.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apprajapati.foody.MainViewModel
import com.apprajapati.foody.adapters.RecipesAdapter
import com.apprajapati.foody.databinding.RecipeFragmentBinding
import com.apprajapati.foody.util.Constants.Companion.API_KEY
import com.apprajapati.foody.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private var _binding: RecipeFragmentBinding? = null
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mainViewModel: MainViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = RecipeFragmentBinding.inflate(inflater, container, false)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        setupRecyclerView()
        requestApiData()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    private fun setupRecyclerView() {
        _binding!!.recyclerView.adapter = mAdapter
        _binding!!.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(applyQueries())

        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data.let { mAdapter.setData(it!!) }
                }

                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                }

                is NetworkResult.Loading -> {
                    // show loading effect.
                }
            }
        })
    }

    private fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries["number"] = "50"
        queries["apiKey"] = API_KEY
        queries["type"] = "snack"
        queries["diet"] = "vegan"
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"

        return queries
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}