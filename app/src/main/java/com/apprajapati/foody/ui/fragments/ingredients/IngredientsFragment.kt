package com.apprajapati.foody.ui.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apprajapati.foody.util.Constants.Companion.RECIPE_BUNDLE_KEY
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.apprajapati.foody.adapters.IngredientsAdapter
import com.apprajapati.foody.databinding.FragmentIngredientsBinding
import com.apprajapati.foody.models.Result
import com.apprajapati.foody.util.retrieveParcelable

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        val args = arguments
        val myBundle: Result? = args?.retrieveParcelable(RECIPE_BUNDLE_KEY)
        binding.ingredientsRecyclerView.adapter = mAdapter
        binding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        myBundle?.extendedIngredients?.let {
            mAdapter.setData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}