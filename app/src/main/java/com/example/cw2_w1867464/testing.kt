//package com.example.cw2_w1867464
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.lifecycle.Observer
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.flowOn
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import org.json.JSONObject
//import java.net.URL
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var mealDao: MealDao
//    private lateinit var addMealsButton: Button
//    private lateinit var searchForMeals: Button
//    private lateinit var searchMealsByIngredients: Button
//    private lateinit var searchResultsTextView: TextView
//    private lateinit var ingredientEditText: EditText
//
//    @SuppressLint("MissingInflatedId")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        addMealsButton = findViewById(R.id.addMealsToDB_Button)
//        searchForMeals = findViewById(R.id.SearchForMeals_button)
//        searchMealsByIngredients = findViewById(R.id.SearchMealsByIngredients_button)
//        searchResultsTextView = findViewById(R.id.searchResults_textView)
//        ingredientEditText = findViewById(R.id.ingredient_editText)
//
//        // Initialize MealDao
//        val mealDatabase = MealDatabase.getInstance(this)
//        mealDao = mealDatabase.mealDao()
//
//        // Set click listeners for buttons
//        addMealsButton.setOnClickListener {
//            //addMealsToDb()
//        }
//        searchForMeals.setOnClickListener {
//            searchMeals()
//        }
//        searchMealsByIngredients.setOnClickListener {
//            val intent = Intent(this, MealDetailsActivity::class.java)
//            intent.putExtra("ingredient", ingredientEditText.text.toString())
//            startActivity(intent)
//        }
//    }
//
////    private fun addMealsToDb() {
////        // Launch a coroutine on IO dispatcher
////        CoroutineScope(Dispatchers.IO).launch {
////            // Insert sample meals into the database
////            val meal1 = Meal(name = "Spaghetti Bolognese", ingredient = "beef", description = "Pasta")
////            val meal2 = Meal(name = "Chicken Curry", ingredient = "chicken", description = "Curry")
////            val meal3 = Meal(name = "Veggie Stir-fry", ingredient = "vegetables", description = "Stir-fry")
////
////            mealDao.insert(meal1)
////            mealDao.insert(meal2)
////            mealDao.insert(meal3)
////        }
////    }
////
////    private fun searchMealsByIngredient() {
////        // Launch a coroutine on IO dispatcher
////        CoroutineScope(Dispatchers.IO).launch {
////            // Observe query results using Flow
////            mealDao.searchMealsByIngredient("beef").collect { meals ->
////                // Update UI with search results
////                withContext(Dispatchers.Main) {
////                    Toast.makeText(
////                        this@MainActivity,
////                        "Meals with beef: ${meals.size}",
////                        Toast.LENGTH_SHORT
////                    ).show()
////                }
////            }
////        }
////    }
////
////
////    private fun searchMeals() {
////        // Launch a coroutine on IO dispatcher
////        CoroutineScope(Dispatchers.IO).launch {
////            // Search meals by query
////            val meals = mealDao.searchMeals("%curry%")
////                .collect { mealsList ->
////                    // Update UI with search results on the main thread
////                    withContext(Dispatchers.Main) {
////                        Toast.makeText(
////                            this@MainActivity,
////                            "Meals with curry: ${mealsList.size}",
////                            Toast.LENGTH_SHORT
////                        ).show()
////                    }
////                }
////        }
////    }
//
//    private fun searchMeals() {
//        // Launch a coroutine on IO dispatcher
//        CoroutineScope(Dispatchers.IO).launch {
//            // Retrieve meals from the web service
//            val ingredient = ingredientEditText.text.toString()
//            val meals = retrieveMealsFromWebService(ingredient)
//
//            // Update UI with search results on the main thread
//            withContext(Dispatchers.Main) {
//                searchResultsTextView.text = meals
//            }
//        }
//    }
//
//
////    private fun searchMealsByIngredient() {
////        // Launch a coroutine on IO dispatcher
////        CoroutineScope(Dispatchers.IO).launch {
////            // Search meals by ingredient in the database
////            val ingredient = ingredientEditText.text.toString()
////            val meals = mealDao.searchMealsByIngredient("%$ingredient%")
////                .collect { mealsList ->
////                    // Update UI with search results on the main thread
////                    withContext(Dispatchers.Main) {
////                        searchResultsTextView.text = "Meals with $ingredient: ${mealsList.size}"
////                    }
////                }
////        }
////    }
//
////    private suspend fun retrieveMealsFromWebService(ingredient: String): String {
////        // Make HTTP request to the web service
////        val url = "https://www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata"
////        val response = URL(url).readText()
////
////        // Parse JSON response
////        val meals = JSONObject(response).getJSONArray("meals")
////        val mealNames = mutableListOf<String>()
////        for (i in 0 until meals.length()) {
////            val meal = meals.getJSONObject(i)
////            val mealName = meal.getString("strMeal")
////            mealNames.add(mealName)
////        }
////
////        // Return meal names as a formatted string
////        return "Meals with $ingredient: ${mealNames.joinToString(", ")}"
////    }
//
//    private suspend fun retrieveMealsFromWebService(ingredient: String): String {
//        // Make HTTP request to the web service
//        val url = "https://www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata"
//        val response = URL(url).readText()
//
//        // Parse JSON response
//        val meals = JSONObject(response).getJSONArray("meals")
//        val mealDetails = mutableListOf<String>()
//        for (i in 0 until meals.length()) {
//            val mealId = meals.getJSONObject(i).getString("idMeal")
//            val mealDetailsResponse =
//                URL("https://www.themealdb.com/api/json/v1/1/lookup.php?i=$mealId").readText()
//            val meal = JSONObject(mealDetailsResponse).getJSONArray("meals").getJSONObject(0)
//            val mealName = meal.getString("strMeal")
//            val category = meal.getString("strCategory")
//            val area = meal.getString("strArea")
//            val instructions = meal.getString("strInstructions")
//            val tags = meal.optString("strTags", "")
//            val youtube = meal.optString("strYoutube", "")
//            val ingredients = mutableListOf<String>()
//            val measures = mutableListOf<String>()
//            for (j in 1..20) {
//                val ingredient = meal.optString("strIngredient$j", "")
//                if (ingredient.isNotEmpty()) {
//                    ingredients.add(ingredient)
//                    measures.add(meal.optString("strMeasure$j", ""))
//                }
//            }
//            val mealDetail = """
//                "Meal":"$mealName",
//                "Category":"$category",
//                "Area":"$area",
//                "Instructions":"$instructions",
//                "Tags":"$tags",
//                "Youtube":"$youtube",
//                "Ingredients":${ingredients.joinToString(",", "[", "]")},
//                "Measures":${measures.joinToString(",", "[", "]")}
//                 """
//            mealDetails.add(mealDetail)
//        }
//        return mealDetails.joinToString(",","[", "]")
//
//    }
//
//}
//
//




// MealDeatilsActivity
//        retrieveMealsButton.setOnClickListener {
//            val searchIngredient = ingredientEditText.text.toString()
//            CoroutineScope(Dispatchers.IO).launch {
//                val mealsJson = searchIngredient?.let { retrieveMealsFromWebService(it) }
//                val meals = JSONObject(mealsJson).getJSONArray("categories")
//                withContext(Dispatchers.Main) {
//                    val builder = AlertDialog.Builder(this@MealDetailsActivity)
//                    builder.setTitle("Meal Details")
//
//                    // Create a custom layout for the dialog box
//                    val customLayout = LayoutInflater.from(this@MealDetailsActivity).inflate(R.layout.dialog_meal_details, null)
//                    builder.setView(customLayout)
//
//                    // Set the thumb image in the custom layout
//                    val thumbImageView = customLayout.findViewById<ImageView>(R.id.thumbImageView)
//                    val thumbUrl = meals.getJSONObject(0).getString("Thumb")
//                    Glide.with(this@MealDetailsActivity).load(thumbUrl).into(thumbImageView)
//
//                    // Set the meal details in the custom layout
//                    val mealNameTextView = customLayout.findViewById<TextView>(R.id.mealNameTextView)
//                    val descriptionTextView = customLayout.findViewById<TextView>(R.id.descriptionTextView)
//                    val mealDetails = meals.getJSONObject(0)
//                    mealNameTextView.text = mealDetails.getString("Meal")
//                    descriptionTextView.text = mealDetails.getString("Description")
//
//                    // Add a button to close the dialog box
//                    builder.setPositiveButton("OK", null)
//
//                    // Show the dialog box
//                    val dialog = builder.create()
//                    dialog.show()
//                }
//            }
//        }