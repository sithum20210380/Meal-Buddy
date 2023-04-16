package com.example.cw2_w1867464

import android.os.AsyncTask
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey val id: Int,
    val name: String,
    val category: String,
    val area: String,
    val instructions: String,
    val tags: String,
    val youtubeLink: String,
    val measures: List<String>,
    @TypeConverters(ListStringConverter::class)
    val ingredients: List<String>,
    val mealThumb: String
)


class RetrieveMealsTask(private val ingredient: String, private val callback: (List<Meal>?) -> Unit) :
    AsyncTask<Void, Void, List<Meal>?>() {

    override fun doInBackground(vararg params: Void?): List<Meal>? {
        var meals: List<Meal>? = null
        try {
            val url = URL("https://www.themealdb.com/api/json/v1/1/filter.php?i=$ingredient")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
                val stringBuilder = StringBuilder()
                var line: String? = null
                while ({ line = bufferedReader.readLine(); line }() != null) {
                    stringBuilder.append(line)
                }
                bufferedReader.close()

                val jsonObject = JSONObject(stringBuilder.toString())
                val jsonArray = jsonObject.getJSONArray("meals")
                meals = mutableListOf()
                for (i in 0 until jsonArray.length()) {
                    val mealObject = jsonArray.getJSONObject(i)
                    val mealId = mealObject.getString("idMeal")
                    val mealDetailsResponse =
                        URL("https://www.themealdb.com/api/json/v1/1/lookup.php?i=$mealId").readText()
                    val mealDetails = JSONObject(mealDetailsResponse).getJSONArray("meals").getJSONObject(0)
                    val id = mealDetails.getString("idMeal").toInt()
                    val name = mealDetails.getString("strMeal")
                    val category = mealDetails.getString("strCategory")
                    val area = mealDetails.getString("strArea")
                    val instructions = mealDetails.getString("strInstructions")
                    val tags = mealDetails.optString("strTags", "")
                    val youtubeLink = mealDetails.optString("strYoutube", "")
                    val mealThumb = mealDetails.optString("strMealThumb", "")
                    val ingredients = mutableListOf<String>()
                    val measures = mutableListOf<String>()
                    for (j in 1..20) {
                        val ingredient = mealDetails.optString("strIngredient$j", "")
                        if (ingredient.isNotEmpty()) {
                            ingredients.add(ingredient)
                            measures.add(mealDetails.optString("strMeasure$j", ""))
                        }
                    }
                    val meal = Meal(
                        id=id,
                        name = name,
                        category = category,
                        area = area,
                        instructions = instructions,
                        tags = tags,
                        youtubeLink = youtubeLink,
                        measures = measures,
                        ingredients = ingredients,
                        mealThumb = mealThumb
                    )
                    meals.add(meal)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return meals
    }

    override fun onPostExecute(result: List<Meal>?) {
        callback(result)
    }
}

class ListStringConverter {

    @TypeConverter
    fun fromListString(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toListString(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }
}
