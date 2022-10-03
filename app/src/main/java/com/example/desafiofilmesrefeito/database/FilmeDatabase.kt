package com.example.desafiofilmesrefeito.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.desafiofilmesrefeito.database.dao.FilmeFavoritoDao
import com.example.desafiofilmesrefeito.model.FilmeFavorito

@Database(
    entities = [FilmeFavorito::class],
    version = 1,
    exportSchema = false
)
abstract class FilmeDatabase : RoomDatabase() {

    abstract fun getUsuarioDao(): FilmeFavoritoDao

    companion object factory{
        fun getInstance(context: Context): FilmeDatabase {
            return Room.databaseBuilder(context, FilmeDatabase::class.java, "InformacoesBD")
                .allowMainThreadQueries()
                .addMigrations()
                .build()
        }
    }
}