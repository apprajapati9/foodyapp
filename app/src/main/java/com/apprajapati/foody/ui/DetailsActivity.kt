package com.apprajapati.foody.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.apprajapati.foody.R
import com.apprajapati.foody.adapters.PagerAdapter
import com.apprajapati.foody.data.database.entities.FavoritesEntity
import com.apprajapati.foody.databinding.ActivityDetailsBinding
import com.apprajapati.foody.ui.fragments.ingredients.IngredientsFragment
import com.apprajapati.foody.ui.fragments.instructions.InstructionsFragment
import com.apprajapati.foody.ui.fragments.overview.OverviewFragment
import com.apprajapati.foody.util.Constants.Companion.RECIPE_BUNDLE_KEY
import com.apprajapati.foody.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private val args by navArgs<DetailsActivityArgs>()

    private val mainViewModel: MainViewModel by viewModels()

    private var recipeSaved = false
    private var savedRecipeId = 0

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
        resultBundle.putParcelable(RECIPE_BUNDLE_KEY, args.recipeResult)

        val viewPagerAdapter = PagerAdapter(resultBundle, fragments, this)
        binding.detailsViewPager.apply {
            adapter = viewPagerAdapter
        }
        //Updated way of using viewpager
        TabLayoutMediator(binding.tabLayout, binding.detailsViewPager) {
            tab, position->
            tab.text = titles[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_favorite_menu, menu)
        val menuItem = menu?.findItem(R.id.save_favorite_menu)
        checkSavedRecipes(menuItem!!)
        return true
    }

    private fun checkSavedRecipes(item: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this) { favorite ->
            try {
                for (savedRecipe in favorite) {
                    if (savedRecipe.result.id == args.recipeResult.id) {
                        changeMenuItemColor(item, R.color.yellow)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                    } else
                        changeMenuItemColor(item, R.color.white)
                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_favorite_menu && !recipeSaved) {
            saveToFavorites(item)
        } else if (item.itemId == R.id.save_favorite_menu && recipeSaved) {
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(0, args.recipeResult)
        mainViewModel.insertFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        Snackbar.make(binding.root, "Recipe Saved.", Snackbar.LENGTH_SHORT).setAction("Okay") {}
            .show()
        recipeSaved = true
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(savedRecipeId, args.recipeResult)
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.white)
        Snackbar.make(binding.root, "Recipe removed from Favorites.", Snackbar.LENGTH_SHORT)
            .setAction("Okay") {}.show()
        recipeSaved = false
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon?.setTint(ContextCompat.getColor(this, color))
    }
}