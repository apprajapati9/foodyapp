package com.apprajapati.foody.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apprajapati.foody.databinding.RecipeBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RecipeBottomSheet : BottomSheetDialogFragment() {

    private var _binding: RecipeBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = RecipeBottomSheetBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}