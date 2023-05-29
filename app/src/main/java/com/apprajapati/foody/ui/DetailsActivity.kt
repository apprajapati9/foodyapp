package com.apprajapati.foody.ui

import android.icu.text.CaseMap.Title
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.apprajapati.foody.R
import com.apprajapati.foody.adapters.PagerAdapter
import com.apprajapati.foody.databinding.ActivityDetailsBinding
import com.apprajapati.foody.ui.fragments.ingredients.IngredientsFragment
import com.apprajapati.foody.ui.fragments.instructions.InstructionsFragment
import com.apprajapati.foody.ui.fragments.overview.OverviewFragment

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private val args by navArgs<DetailsActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.detailsToolbar)
        binding.detailsToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupTabs()
    }

    private fun setupTabs() {
        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable("recipeBundle", args.recipeResult)

        val adapter = PagerAdapter(resultBundle, fragments, titles, supportFragmentManager)
        binding.detailsViewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.detailsViewPager)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}