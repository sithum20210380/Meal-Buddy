package com.example.cw2_w1867464

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData

class MealViewModel(application: Application) : AndroidViewModel(application) {

    private val mealDao: MealDao = MealDatabase.getInstance(application).mealDao()

    fun searchMealsByIngredient(ingredient: String): LiveData<List<Meal>> {
        return mealDao.searchMealsByIngredient(ingredient).asLiveData()
    }
    fun searchMeals(query: String): LiveData<List<Meal>> {
        return mealDao.searchMeals(query).asLiveData()
    }
}
