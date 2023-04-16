package com.example.cw2_w1867464

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import java.util.*


class SearchResultsActivity : AppCompatActivity() {
    private lateinit var mealDao: MealDao
    private lateinit var viewResults: RecyclerView
    private lateinit var searchResultsFromDB: TextInputEditText
    private lateinit var searchButton: Button
    private lateinit var adapter: MealAdapter
    private lateinit var emptyView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        //viewResults = findViewById(R.id.viewResultsFromDB)
        searchResultsFromDB = findViewById(R.id.searchMealFromDB)
        searchButton = findViewById(R.id.search_button)
        emptyView = findViewById(R.id.empty_view)

        val viewResults = findViewById<RecyclerView>(R.id.recyclerView)


        // Initialize MealDao
        val mealDatabase = MealDatabase.getInstance(this)
        mealDao = mealDatabase.mealDao()

        adapter = MealAdapter(MealAdapter.MealDiffCallback())

        viewResults.adapter = adapter
        viewResults.layoutManager = LinearLayoutManager(this)

        searchButton.setOnClickListener {
            // Get the search query entered by the user
            val query = searchResultsFromDB.text.toString().toLowerCase(Locale.getDefault())

            // Get all meals that contain the search query in their name or ingredients
            val viewModel = ViewModelProvider(this).get(MealViewModel::class.java)
            viewModel.searchMeals(query).observe(this, Observer<List<Meal>> { meals ->
                if (meals.isNotEmpty()) {
                    adapter.submitList(meals)
                    emptyView.isVisible = false
                    viewResults.isVisible = true
                } else {
                    emptyView.isVisible = true
                    viewResults.isVisible = false
                }
            })
        }
    }
}


