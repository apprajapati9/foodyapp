package com.apprajapati.foody.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.apprajapati.foody.R
import com.apprajapati.foody.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Foody)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.setGraph(R.navigation.nav_graph)
        // appBarConfiguration = AppBarConfiguration(navController.graph)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.recipeFragment,
                R.id.favRecipeFragment,
                R.id.foodJokeFragment
            )
        )

        val bottomNavView: BottomNavigationView =
            binding.root.findViewById(R.id.bottomNavigationView)
        bottomNavView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }
}