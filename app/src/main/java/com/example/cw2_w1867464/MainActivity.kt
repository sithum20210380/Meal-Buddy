package com.example.cw2_w1867464

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
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

        // Buttons in the home screen
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
            val intent = Intent(this, SearchByIngredientActivity::class.java)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save any necessary data here
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Handle orientation change here if needed
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
            // Show a toast message on the main UI thread
            runOnUiThread {
                Toast.makeText(this@MainActivity, "Meals added to the database", Toast.LENGTH_SHORT).show()
            }
        }
    }


}


