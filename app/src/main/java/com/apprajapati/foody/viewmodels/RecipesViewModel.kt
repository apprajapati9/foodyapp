package com.apprajapati.foody.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.apprajapati.foody.data.DataStoreRepository
import com.apprajapati.foody.data.MealAndDietType
import com.apprajapati.foody.util.Constants.Companion.API_KEY
import com.apprajapati.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.apprajapati.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.apprajapati.foody.util.Constants.Companion.DEFAULT_RECIPE_NUMBER
import com.apprajapati.foody.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.apprajapati.foody.util.Constants.Companion.QUERY_DIET
import com.apprajapati.foody.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.apprajapati.foody.util.Constants.Companion.QUERY_KEY
import com.apprajapati.foody.util.Constants.Companion.QUERY_NUMBER
import com.apprajapati.foody.util.Constants.Companion.QUERY_SEARCH
import com.apprajapati.foody.util.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private lateinit var mealAndDiet: MealAndDietType
    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    val readMealAndDietType = dataStoreRepository.readMealAndDietType


    var networkStatus = false
    var backOnline = false

    var backFromBottomSheet: Boolean = false

    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()


    fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) { dataStoreRepository.saveBackOnline(backOnline) }

    fun saveMealAndDietType() =
        viewModelScope.launch(Dispatchers.IO) {
            if(this@RecipesViewModel::mealAndDiet.isInitialized) {
                dataStoreRepository.saveMealAndDietType(
                    mealAndDiet.selectedMealType,
                    mealAndDiet.selectedMealTypeId,
                    mealAndDiet.selectedDietType,
                    mealAndDiet.selectedDietTypeId
                )
            }

        }

    fun saveMealAndDietTypeTemporary(meal: String, mealId: Int, diet: String, dietId: Int) {
        mealAndDiet = MealAndDietType(meal, mealId, diet, dietId)
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()


        queries[QUERY_NUMBER] = DEFAULT_RECIPE_NUMBER
        queries[QUERY_KEY] = API_KEY
        if(this@RecipesViewModel::mealAndDiet.isInitialized){
            queries[QUERY_TYPE] = mealAndDiet.selectedMealType
            queries[QUERY_DIET] = mealAndDiet.selectedDietType
        }else{
            queries[QUERY_TYPE] = mealType
            queries[QUERY_DIET] = dietType
        }

        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        val q = queries.map {
            "${it.key}: ${it.value}"
        }
        Log.d("Queries", q.toString())
        return queries
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = DEFAULT_RECIPE_NUMBER
        queries[QUERY_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        val q = queries.map {
            "${it.key}: ${it.value}"
        }
        Log.d("Queries", q.toString())

        return queries
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online.", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}