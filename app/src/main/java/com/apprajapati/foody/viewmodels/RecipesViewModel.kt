package com.apprajapati.foody.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.apprajapati.foody.data.DataStoreRepository
import com.apprajapati.foody.util.Constants.Companion.API_KEY
import com.apprajapati.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.apprajapati.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.apprajapati.foody.util.Constants.Companion.DEFAULT_RECIPE_NUMBER
import com.apprajapati.foody.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.apprajapati.foody.util.Constants.Companion.QUERY_DIET
import com.apprajapati.foody.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.apprajapati.foody.util.Constants.Companion.QUERY_KEY
import com.apprajapati.foody.util.Constants.Companion.QUERY_NUMBER
import com.apprajapati.foody.util.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var networkStatus = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    fun saveMealAndDietType(meal : String, mealId : Int, diet : String, dietId: Int)  =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(meal, mealId, diet, dietId)
        }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect {
                value ->
                    mealType = value.selectedMealType
                    dietType = value.selectedDietType
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_RECIPE_NUMBER
        queries[QUERY_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        val q = queries.map{
            "${it.key}: ${it.value}"
        }
        Log.d("Queries", q.toString() );
        return queries
    }

    fun showNetworkStatus(){
        if(!networkStatus){
            Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
        }
    }
}