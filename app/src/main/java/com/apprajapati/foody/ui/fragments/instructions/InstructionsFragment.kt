package com.apprajapati.foody.ui.fragments.instructions

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.apprajapati.foody.databinding.FragmentInstructionsBinding
import com.apprajapati.foody.models.Result
import com.apprajapati.foody.ui.BaseFragment
import com.apprajapati.foody.util.Constants.Companion.RECIPE_BUNDLE_KEY
import com.apprajapati.foody.util.retrieveParcelable

class InstructionsFragment :
    BaseFragment<FragmentInstructionsBinding>(FragmentInstructionsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebView()
    }

    private fun setupWebView() {
        val args = arguments
        val myBundle: Result? = args?.retrieveParcelable(RECIPE_BUNDLE_KEY)
        binding.webViewInstructions.webViewClient = object : WebViewClient() {}
        binding.webViewInstructions.loadUrl(myBundle!!.sourceUrl)
    }
}