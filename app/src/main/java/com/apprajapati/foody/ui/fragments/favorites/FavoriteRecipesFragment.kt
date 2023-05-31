package com.apprajapati.foody.ui.fragments.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apprajapati.foody.adapters.FavoriteRecipeAdapter
import com.apprajapati.foody.databinding.FragmentFavRecipeBinding
import com.apprajapati.foody.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {

    private var _binding: FragmentFavRecipeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter: FavoriteRecipeAdapter by lazy {
        FavoriteRecipeAdapter(
            requireActivity(),
            mainViewModel
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavRecipeBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this

        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter

        setupRecyclerView()
        return binding.root

    }

    private fun setupRecyclerView() {
        binding.favRecipeRecyclerView.adapter = mAdapter
        binding.favRecipeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mAdapter.clearContextualActionMode()
    }
}