package com.apprajapati.foody.ui.fragments.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.apprajapati.foody.R
import com.apprajapati.foody.bindingadapters.RecipesBinding
import com.apprajapati.foody.bindingadapters.RecipesRowBinding
import com.apprajapati.foody.databinding.FragmentOverviewBinding
import com.apprajapati.foody.models.Result
import com.apprajapati.foody.util.Constants.Companion.RECIPE_BUNDLE_KEY
import com.apprajapati.foody.util.retrieveParcelable
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOverviewBinding.inflate(inflater, container, false)



        populateData()


        return binding.root
    }

    private fun populateData() {
        val args = arguments
        val myBundle: Result? =
            args!!.retrieveParcelable(RECIPE_BUNDLE_KEY) as Result? //Todo: Deprecated. Replace/Remove

        if(myBundle != null){
            binding.imageViewRecipe.load(myBundle.image)
            binding.textViewTitle.text = myBundle.title
            binding.textViewLikes.text = myBundle.aggregateLikes.toString()
            binding.textViewTime.text = myBundle.readyInMinutes.toString()

            RecipesRowBinding.parseHtml(binding.textViewSummary, myBundle.summary)

            updateColors(myBundle.vegetarian, binding.textViewVegetarian, binding.imageViewVegetarian)
            updateColors(myBundle.vegan, binding.textViewVegan, binding.imageViewVegan)
            updateColors(myBundle.glutenFree, binding.textViewGlutenFree, binding.imageViewGlutenFree)
            updateColors(myBundle.dairyFree, binding.textViewDairyFree, binding.imageViewDairyFree)
            updateColors(myBundle.veryHealthy, binding.textViewHealthy, binding.imageViewHealthy)
            updateColors(myBundle.cheap, binding.textViewCheap, binding.imageViewCheap)
        }
    }

    private fun updateColors(state: Boolean, textView: TextView, imageView: ImageView) {
        if (state) {
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}