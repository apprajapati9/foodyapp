package com.apprajapati.foody.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.apprajapati.foody.databinding.RecipeBottomSheetBinding
import com.apprajapati.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.apprajapati.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.apprajapati.foody.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.lang.Exception

class RecipeBottomSheet : BottomSheetDialogFragment() {

    private var _binding: RecipeBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipesViewModel: RecipesViewModel

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = RecipeBottomSheetBinding.inflate(inflater, container, false)

        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) {
            mealTypeChip = it.selectedMealType
            dietTypeChip = it.selectedDietType

            updateChipValues(it.selectedMealTypeId, binding.chipGroupMealType)
            updateChipValues(it.selectedDietTypeId, binding.chipGroupDietType)
        }

        binding.chipGroupMealType.setOnCheckedStateChangeListener { group, selectedChipIds ->

            val chip = group.findViewById<Chip>(selectedChipIds.first())
            val selectedMealType = chip.text.toString().lowercase()
            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipIds.first()
        }

        binding.chipGroupDietType.setOnCheckedStateChangeListener { group, selectedIds ->

            val chip = group.findViewById<Chip>(selectedIds.first())
            val selectedDietType = chip.text.toString().lowercase()
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedIds.first()
        }

        binding.buttonApply.setOnClickListener {
            recipesViewModel.saveMealAndDietTypeTemporary(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )
            val action = RecipeBottomSheetDirections.actionRecipeBottomSheetToRecipeFragment(true)
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun updateChipValues(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                val selectedChip = chipGroup.findViewById<Chip>(chipId)
                selectedChip.isChecked = true
                chipGroup.requestChildFocus(selectedChip, selectedChip)
            } catch (e: Exception) {
                Log.d("RecipesBottomSheet", e.message.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}