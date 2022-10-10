package com.example.desafiofilmesrefeito.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.database.dao.FilmeFavoritoDao

@Database(
    entities = [Filme::class],
    version = 2,
    exportSchema = false
)
abstract class FilmeDatabase : RoomDatabase() {

    abstract fun getFilmeFavoritoDao(): FilmeFavoritoDao

    companion object factory {

        private lateinit var db: FilmeDatabase

        fun getInstance(context: Context): FilmeDatabase {

            if (::db.isInitialized) return db
            db = databaseBuilder(context, FilmeDatabase::class.java, "InformacoesBD.bd")
//                .addMigrations(Migration(1, 2) {
//                    it.execSQL("DROP TABLE IF EXISTS 'Filme'")
//                    it.execSQL(
//                        "CREATE TABLE IF NOT EXISTS `Filme` (" +
//                                "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
//                                "`title` TEXT NOT NULL, " +
//                                "`poster_path` TEXT NOT NULL, " +
//                                "`vote_average` FLOAT NOT NULL, " +
//                                "`overview` TEXT NOT NULL, " +
//                                "`release_date` TEXT NOT NULL, " +
//                                "`vote_count` TEXT NOT NULL, " +
//                                "`backdrop_path` TEXT NOT NULL)"
//                    );
//                })
                .build()
            return db
        }
    }
}
