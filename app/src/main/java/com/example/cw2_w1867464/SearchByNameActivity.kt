package com.example.cw2_w1867464

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.util.*

class SearchByNameActivity : AppCompatActivity() {
    private lateinit var mealsTextView: TextView
    private lateinit var ingredientEditText: TextView
    private lateinit var retrieveMealsButton: Button
    private lateinit var saveMealsButton : Button

    private lateinit var mealDao: MealDao
    private lateinit var mealDatabase: MealDatabase

    private var meals: List<Meal>? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchmeal_details)

        mealsTextView = findViewById(R.id.meal_details)
        ingredientEditText = findViewById(R.id.ingredientEditText)
        retrieveMealsButton = findViewById(R.id.retrieve_meals_button)

        // Initialize the database and dao
        mealDatabase = MealDatabase.getInstance(applicationContext)
        mealDao = mealDatabase.mealDao()


        // Get ingredient from intent
        val ingredient = intent.getStringExtra("ingredient")

        // Launch a coroutine on IO dispatcher
        CoroutineScope(Dispatchers.IO).launch {
            // Retrieve meals from the web service
            val meals = ingredient?.let { retrieveMealsFromWebService(it) }

            // Update UI with search results on the main thread
            withContext(Dispatchers.Main) {
                // Display meals in TextView
                //mealsTextView.text = meals
            }
        }

        // Add click listener to Retrieve Meals button
        retrieveMealsButton.setOnClickListener {
            val searchIngredient = ingredientEditText.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val meals = retrieveMealsFromWebService(searchIngredient)
                withContext(Dispatchers.Main) {
                    if (meals.isEmpty()) {
                        // Display an error message if there are no meals for the given ingredient
                        Toast.makeText(
                            this@SearchByNameActivity,
                            "No meals found for $searchIngredient",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val builder = AlertDialog.Builder(this@SearchByNameActivity)
                        builder.setTitle("Meal Details")

                        // Filter meals based on substring entered by user
                        val filteredMeals = meals.filter { meal ->
                            meal.name.toLowerCase(Locale.getDefault()).contains(searchIngredient.toLowerCase(Locale.getDefault()))
                        }

                        if (filteredMeals.isEmpty()) {
                            // Display an error message if there are no meals containing the substring
                            Toast.makeText(
                                this@SearchByNameActivity,
                                "No meals found containing $searchIngredient",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Set the message of the dialog box to be the meal details
                            builder.setMessage(
                                filteredMeals.joinToString(
                                    separator = "\n\n"
                                ) { mealDetailsString(it) }
                            )

                            // Add a button to close the dialog box
                            builder.setPositiveButton("OK", null)

                            // Show the dialog box
                            val dialog = builder.create()
                            dialog.show()

                            // Store the retrieved meals in the activity
                            this@SearchByNameActivity.meals = filteredMeals
                        }
                    }
                }
            }
        }


    }


    private suspend fun retrieveMealsFromWebService(substring: String): List<Meal> {
        // Make HTTP request to the web service
        val url = "https://www.themealdb.com/api/json/v1/1/search.php?s=$substring"
        val response = URL(url).readText()

        // Parse JSON response
        val mealsJson = JSONObject(response).optJSONArray("meals")
        if (mealsJson == null) {
            // Handle error here, e.g. throw an exception or return an empty list
            return emptyList()
        }

        val mealList = mutableListOf<Meal>()
        for (i in 0 until mealsJson.length()) {
//            val mealId = mealsJson.getJSONObject(i).getString("idMeal")
//            val mealDetailsResponse =
//                URL("https://www.themealdb.com/api/json/v1/1/lookup.php?i=$mealId").readText()
//            val mealDetailsJson = JSONObject(mealDetailsResponse).optJSONArray("meals")
//            if (mealDetailsJson == null || mealDetailsJson.length() == 0) {
//                // Handle error here, e.g. log a message or skip this meal
//                continue
//            }
            val meal = mealsJson.getJSONObject(i)
            val id = meal.getString("idMeal").toInt()
            val mealName = meal.getString("strMeal")
            val category = meal.getString("strCategory")
            val area = meal.getString("strArea")
            val instructions = meal.getString("strInstructions")
            val tags = meal.optString("strTags", "")
            val youtube = meal.optString("strYoutube", "")
            val mealThumb = meal.optString("strMealThumb", "")
            val ingredients = mutableListOf<String>()
            val measures = mutableListOf<String>()
            for (j in 1..20) {
                val ingredient = meal.optString("strIngredient$j", "")
                if (ingredient.isNotEmpty()) {
                    ingredients.add(ingredient)
                    measures.add(meal.optString("strMeasure$j", ""))
                }
            }
            val mealDetail = Meal(
                id=id,
                name = mealName,
                category = category,
                area = area,
                instructions = instructions,
                tags = tags,
                youtubeLink = youtube,
                ingredients = ingredients,
                measures = measures,
                mealThumb = mealThumb
            )
            mealList.add(mealDetail)
        }
        return mealList
    }



    fun mealDetailsString(meal: Meal): String {
        val sb = StringBuilder()
        sb.append("Meal: ${meal.name}\n")
        //sb.append("DrinkAlternate: ${meal.drinkAlternate}\n")
        sb.append("Category: ${meal.category}\n")
        sb.append("Area: ${meal.area}\n")
        sb.append("Instructions: ${meal.instructions}\n")
        sb.append("Tags: ${meal.tags}\n")
        sb.append("Youtube: ${meal.youtubeLink}\n")
        sb.append("Thumbnail URL: ${meal.mealThumb}\n")

        sb.append("Ingredients:\n")
        for (i in meal.ingredients.indices) {
            sb.append("Ingredient${i + 1}: ${meal.ingredients[i]}\n")
        }
        sb.append("\nMeasures:\n")
        for (i in meal.measures.indices) {
            sb.append("Measure${i + 1}: ${meal.measures[i]}\n")
        }

        return sb.toString()
    }

}