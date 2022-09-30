package com.example.desafiofilmesrefeito.activity

import android.content.ContentValues.TAG
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
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
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)




        val extra = intent
        filmeRecebido = extra.getSerializableExtra("filmeEnviado") as Filme
        Log.d("achar", "onCreate: filme recebido com sucesso  $filmeRecebido")
        Log.d("achar fundo", "onCreate: filme recebido com sucesso  ${filmeRecebido.concatFundo()}")

        val imagemFundo = findViewById<ImageView>(R.id.ImageVImagemFundo)
        Glide.with(this).load(filmeRecebido.concatFundo()).into(imagemFundo)

        val imagemIcone = findViewById<ImageView>(R.id.ImageVImagemIcone)
        Glide.with(this).load(filmeRecebido.concatPoster()).into(imagemIcone)

        val titulo = findViewById<TextView>(R.id.TextVTitulo)
        titulo.text = filmeRecebido.title

        val nota = findViewById<TextView>(R.id.TextVNota)
        nota.text = "Nota: "+filmeRecebido.vote_average

        val numeroAvaliacoes = findViewById<TextView>(R.id.TextVNumeroAvaliacoes)
        numeroAvaliacoes.text = "Votos: "+filmeRecebido.vote_count

        val dataLancamento = findViewById<TextView>(R.id.TextVDataLancamento)
        dataLancamento.text = "Lançamento: "+DataUtil.periodoEmTexto(filmeRecebido.release_date)

        val descricao = findViewById<TextView>(R.id.TextVDescricao)
        descricao.text = filmeRecebido.overview


    }
}