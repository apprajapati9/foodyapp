package com.apprajapati.foody.ui.fragments.instructions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.apprajapati.foody.databinding.FragmentInstructionsBinding
import com.apprajapati.foody.models.Result
import com.apprajapati.foody.util.Constants.Companion.RECIPE_BUNDLE_KEY
import com.apprajapati.foody.util.retrieveParcelable

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)

        setupWebView()

        return binding.root
    }

    private fun setupWebView() {
        val args = arguments
        val myBundle: Result? = args?.retrieveParcelable(RECIPE_BUNDLE_KEY)
        binding.webViewInstructions.webViewClient = object : WebViewClient() {}
        binding.webViewInstructions.loadUrl(myBundle!!.sourceUrl)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}