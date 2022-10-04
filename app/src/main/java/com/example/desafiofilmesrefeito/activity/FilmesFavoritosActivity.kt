package com.example.desafiofilmesrefeito.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.desafiofilmes.activity.ListaFilmesActivity
import com.example.desafiofilmesrefeito.R

class FilmesFavoritosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filmes_favoritos)

        val voltar = findViewById<ImageView>(R.id.ImageVFlecha)
        voltar.setOnClickListener{
            val intent = Intent(this, ListaFilmesActivity::class.java)
            startActivity(intent)
            finish()
        }

        val favoritos = findViewById<TextView>(R.id.TextVFavoritos)
        favoritos.setOnClickListener {
            val intent = Intent(this, FilmesFavoritosActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}