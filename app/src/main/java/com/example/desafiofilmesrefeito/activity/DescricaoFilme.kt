package com.example.desafiofilmesrefeito.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.desafiofilmes.activity.ListaFilmesActivity
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.R
import com.example.desafiofilmesrefeito.databinding.ActivityDescricaoFilmeBinding
import com.example.desafiofilmesrefeito.util.DataUtil

class DescricaoFilme : AppCompatActivity() {

    private lateinit var filmeRecebido: Filme

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_descricao_filme)


        setTitle("GuilhermeFlix")

        val extra = intent
        filmeRecebido = extra.getSerializableExtra("filmeEnviado") as Filme
        Log.d("achar", "onCreate: filme recebido com sucesso  $filmeRecebido")
        Log.d("achar fundo", "onCreate: filme recebido com sucesso  ${filmeRecebido.concatFundo()}")

//        val rb_ratingBar = findViewById<RatingBar>(R.id.RatingBNota)

//        rb_ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
//            rb_ratingBar.numStars = 5
//            rb_ratingBar.max = 10
//            rb_ratingBar.stepSize= 0.5f
//        }

        val imagemFundo = findViewById<ImageView>(R.id.ImageVImagemFundo)
        Glide.with(this).load(filmeRecebido.concatFundo()).into(imagemFundo)

        val imagemIcone = findViewById<ImageView>(R.id.ImageVImagemIcone)
        Glide.with(this).load(filmeRecebido.concatPoster()).into(imagemIcone)

        val titulo = findViewById<TextView>(R.id.TextVTitulo)
        titulo.text = filmeRecebido.title.uppercase()

        val dataLancamento = findViewById<TextView>(R.id.TextVDataLancamento)
        dataLancamento.text = "Lançamento: " + DataUtil.periodoEmTexto(filmeRecebido.release_date)

        val descricao = findViewById<TextView>(R.id.TextVDescricao)
        descricao.text = filmeRecebido.overview

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