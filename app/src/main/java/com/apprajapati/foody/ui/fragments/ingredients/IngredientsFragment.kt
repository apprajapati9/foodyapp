package com.apprajapati.foody.ui.fragments.ingredients

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.apprajapati.foody.adapters.IngredientsAdapter
import com.apprajapati.foody.databinding.FragmentIngredientsBinding
import com.apprajapati.foody.models.Result
import com.apprajapati.foody.ui.BaseFragment
import com.apprajapati.foody.util.Constants.Companion.RECIPE_BUNDLE_KEY
import com.apprajapati.foody.util.retrieveParcelable

class IngredientsFragment :
    BaseFragment<FragmentIngredientsBinding>(FragmentIngredientsBinding::inflate) {

    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
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
}