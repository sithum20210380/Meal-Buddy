package com.example.cw2_w1867464

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE meals_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, category TEXT NOT NULL, area TEXT NOT NULL, instructions TEXT NOT NULL, tags TEXT NOT NULL, youtubeLink TEXT NOT NULL, measures TEXT NOT NULL, ingredients TEXT NOT NULL, mealThumb TEXT NOT NULL)")
//        database.execSQL("ALTER TABLE meals ADD COLUMN area TEXT NOT NULL DEFAULT ''")
//        database.execSQL("ALTER TABLE meals ADD COLUMN instructions TEXT NOT NULL DEFAULT ''")
//        database.execSQL("ALTER TABLE meals ADD COLUMN tags TEXT NOT NULL DEFAULT ''")
//        database.execSQL("ALTER TABLE meals ADD COLUMN youtubeLink NOT NULL DEFAULT ''")
//        database.execSQL("ALTER TABLE meals ADD COLUMN measures NOT NULL DEFAULT ''")
//        database.execSQL("ALTER TABLE meals ADD COLUMN ingredients NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE meals ADD COLUMN mealThumb NOT NULL DEFAULT ''")
        database.execSQL("INSERT INTO meals_new (id, name, category, area, instructions, tags, youtubeLink, measures, ingredients,mealThumb) SELECT id, name, category, area, instructions, tags, youtubeLink, measures, ingredients,'' FROM meals")
        database.execSQL("DROP TABLE meals")
        database.execSQL("ALTER TABLE meals_new RENAME TO meals")
    }
}





@Database(entities = [Meal::class], version = 3)  // define the Room database
@TypeConverters(ListStringConverter::class) // Register the TypeConverter here
abstract class MealDatabase : RoomDatabase(){  // to use this class as the main entry point for interacting with database
    abstract fun mealDao() : MealDao  // to return an instance of the MealDao interface

    companion object {
        @Volatile
        private var INSTANCE: MealDatabase? = null  // holds the singleton instance of the database (here it uses singleton pattern)

        fun getInstance(context: Context): MealDatabase{
            return INSTANCE ?: synchronized(this){  // to ensure thread-safety / check whether it is null or to create a new instance
                val instance = Room.databaseBuilder(  // builder pattern for creating Room database
                    context.applicationContext,
                    MealDatabase::class.java,
                    "meal_db"
                ).addMigrations(migration_2_3)
                    .build()
                INSTANCE = instance
                instance  // line 23-24 to reuse in the future calls to getInstance() function
            }
        }
    }
}