package com.apprajapati.foody

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.apprajapati.foody.databinding.FoodJokeFragmentBinding

class FoodJokeFragment : Fragment() {

    private var _binding: FoodJokeFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FoodJokeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}