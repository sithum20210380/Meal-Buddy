package com.example.cw2_w1867464
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao // to mark the interface as a Data Access Object
interface MealDao{
    //@Insert
//    fun insert(meal: Meal)  // suspend modifier is used to call from a coroutine and it will not block the main thread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(meals: List<Meal>)

//    @Query("SELECT * FROM meals WHERE name LIKE :query OR category LIKE :query")
//    fun searchMeals(query: String): Flow<List<Meal>>

    @Query("SELECT * FROM meals WHERE lower(name) LIKE '%' || :query || '%' OR lower(ingredients) LIKE '%' || :query || '%'")
    fun searchMeals(query: String): Flow<List<Meal>>


    @Query("SELECT * FROM meals WHERE ingredients LIKE :ingredient")
    fun searchMealsByIngredient(ingredient: String): Flow<List<Meal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(meals: List<Meal>)

//    @Query("SELECT * FROM meals")
//    fun getAll(): List<Meal>
}
/*suspend modifier important for performing database operations asynchronously to avoid blocking the UI thread and providing smooth UX to the APP*/