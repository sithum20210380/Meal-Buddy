package com.example.cw2_w1867464

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mealDao: MealDao
    private lateinit var addMealsButton: Button
    private lateinit var searchForMeals: Button
    private lateinit var searchMealsByIngredients: Button
    private lateinit var searchByname: Button
    private lateinit var addMealsToDB: Button



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProvider(this).get(MealViewModel::class.java)


        searchForMeals = findViewById(R.id.SearchForMeals_button)
        searchMealsByIngredients = findViewById(R.id.SearchMealsByIngredients_button)
        searchByname = findViewById(R.id.SearchByName_Button)
        addMealsToDB = findViewById(R.id.addMealToDB_button)




        // Initialize MealDao
        val mealDatabase = MealDatabase.getInstance(this)
        mealDao = mealDatabase.mealDao()

        // Set click listeners for buttons
        searchForMeals.setOnClickListener {
            val intent = Intent(this, SearchResultsActivity::class.java)
            startActivity(intent)
        }

        searchMealsByIngredients.setOnClickListener {
            val intent = Intent(this, MealDetailsActivity::class.java)
            startActivity(intent)
        }
        searchByname.setOnClickListener {
            val intent = Intent(this, SearchByNameActivity::class.java)
            startActivity(intent)
        }

        addMealsToDB.setOnClickListener {
            addMealsToDb()
        }
    }

    private fun addMealsToDb() {
        // Launch a coroutine on IO dispatcher
        CoroutineScope(Dispatchers.IO).launch {
            // Insert sample meals into the database
            val meals = listOf(
                Meal(
                    id = 1,
                    name = "Spaghetti Bolognese",
                    category = "Pasta",
                    area = "French",
                    instructions = "Preparation\r\n1. Crack the egg into a bowl. Separate the egg white and yolk.\r\n\r\nSweet and Sour Pork\r\n2. Slice the pork tenderloin into ips.\r\n\r\n3. Prepare the marinade using a pinch of salt, one teaspoon of starch, two teaspoons of light soy sauce, and an egg white.\r\n\r\n4. Marinade the pork ips for about 20 minutes.\r\n\r\n5. Put the remaining starch in a bowl. Add some water and vinegar to make a starchy sauce.\r\n\r\nSweet and Sour Pork\r\nCooking Inuctions\r\n1. Pour the cooking oil into a wok and heat to 190\u00b0C (375\u00b0F). Add the marinated pork ips and fry them until they turn brown. Remove the cooked pork from the wok and place on a plate.\r\n\r\n2. Leave some oil in the wok. Put the tomato sauce and white sugar into the wok, and heat until the oil and sauce are fully combined.\r\n\r\n3. Add some water to the wok and thoroughly heat the sweet and sour sauce before adding the pork ips to it.\r\n\r\n4. Pour in the starchy sauce. Stir-fry all the ingredients until the pork and sauce are thoroughly mixed " +
                            "together.\r\n\r\n5. Serve on a plate and add some coriander for decoration.",
                    tags = "Sweet",
                    youtubeLink = "https:www.youtube.comwatch?v=mdaBIhgEAMo",
                    measures = listOf("1 cup", "2 teaspoons", "1/4 teaspoon"),
                    ingredients = listOf("beef"),
                    mealThumb = "https:www.themealdb.comimagesmediamealsadxcbq1619787919.jpg"
                ),
                Meal(
                    id = 2,
                    name = "Leblebi Soup",
                    category = "Chicken",
                    area = "Chinese",
                    instructions = "Preparation\r\n1. Crack the egg into a bowl. Separate the egg white and yolk.\r\n\r\nSweet and Sour Pork\r\n2. Slice the pork tenderloin into ips.\r\n\r\n3. Prepare the marinade using a pinch of salt, one teaspoon of starch, two teaspoons of light soy sauce, and an egg white.\r\n\r\n4. Marinade the pork ips for about 20 minutes.\r\n\r\n5. Put the remaining starch in a bowl. Add some water and vinegar to make a starchy sauce.\r\n\r\nSweet and Sour Pork\r\nCooking Inuctions\r\n1. Pour the cooking oil into a wok and heat to 190\u00b0C (375\u00b0F). Add the marinated pork ips and fry them until they turn brown. Remove the cooked pork from the wok and place on a plate.\r\n\r\n2. Leave some oil in the wok. Put the tomato sauce and white sugar into the wok, and heat until the oil and sauce are fully combined.\r\n\r\n3. Add some water to the wok and thoroughly heat the sweet and sour sauce before adding the pork ips to it.\r\n\r\n4. Pour in the starchy sauce. Stir-fry all the ingredients until the pork and sauce are thoroughly mixed " +
                            "together.\r\n\r\n5. Serve on a plate and add some coriander for decoration.",
                    tags = "Sweet",
                    youtubeLink = "https:www.youtube.comwatch?v=mdaBIhgEAMo",
                    measures = listOf("1 cup", "2 teaspoons", "1/4 teaspoon"),
                    ingredients = listOf("chicken"),
                    mealThumb = "https:www.themealdb.comimagesmediamealsadxcbq1619787919.jpg"
                ),
                Meal(
                    id = 3,
                    name = "Chicken Marengo",
                    category = "Vegetarian",
                    area = "Vietnamese",
                    instructions = "Preparation\r\n1. Crack the egg into a bowl. Separate the egg white and yolk.\r\n\r\nSweet and Sour Pork\r\n2. Slice the pork tenderloin into ips.\r\n\r\n3. Prepare the marinade using a pinch of salt, one teaspoon of starch, two teaspoons of light soy sauce, and an egg white.\r\n\r\n4. Marinade the pork ips for about 20 minutes.\r\n\r\n5. Put the remaining starch in a bowl. Add some water and vinegar to make a starchy sauce.\r\n\r\nSweet and Sour Pork\r\nCooking Inuctions\r\n1. Pour the cooking oil into a wok and heat to 190\u00b0C (375\u00b0F). Add the marinated pork ips and fry them until they turn brown. Remove the cooked pork from the wok and place on a plate.\r\n\r\n2. Leave some oil in the wok. Put the tomato sauce and white sugar into the wok, and heat until the oil and sauce are fully combined.\r\n\r\n3. Add some water to the wok and thoroughly heat the sweet and sour sauce before adding the pork ips to it.\r\n\r\n4. Pour in the starchy sauce. Stir-fry all the ingredients until the pork and sauce are thoroughly mixed " +
                            "together.\r\n\r\n5. Serve on a plate and add some coriander for decoration.",
                    tags = "Sweet",
                    youtubeLink = "https:www.youtube.comwatch?v=mdaBIhgEAMo",
                    measures = listOf("1 cup", "2 teaspoons", "1/4 teaspoon"),
                    ingredients = listOf("sugar"),
                    mealThumb = "https:www.themealdb.comimagesmediamealsadxcbq1619787919.jpg"
                )
            )

            mealDao.insert(meals)
        }
    }

//    id=id,
//    name = name,
//    category = category,
//    area = area,
//    instructions = instructions,
//    tags = tags,
//    youtubeLink = youtubeLink,
//    measures = measures,
//    ingredients = ingredients,
//    mealThumb = mealThumb


//    private suspend fun retrieveMealsFromWebService(ingredient: String): String {
//        // Make HTTP request to the web service
//        val url = "https://www.themealdb.com/api/json/v1/1/filter.php?i=$ingredient"
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
//        return mealDetails.joinToString(separator = "\n")
//
//    }

}


