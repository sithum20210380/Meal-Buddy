package com.example.cw2_w1867464

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao // to mark the interface as a Data Access Object
interface MealDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(meals: List<Meal>)

    @Query("SELECT * FROM meals WHERE lower(name) LIKE '%' || :query || '%' OR lower(ingredients) LIKE '%' || :query || '%'")
    fun searchMeals(query: String): Flow<List<Meal>>

    @Query("SELECT * FROM meals WHERE ingredients LIKE :ingredient")
    fun searchMealsByIngredient(ingredient: String): Flow<List<Meal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(meals: List<Meal>)

}
