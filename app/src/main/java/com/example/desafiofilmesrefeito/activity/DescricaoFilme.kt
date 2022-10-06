package com.example.desafiofilmesrefeito.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.desafiofilmes.activity.ListaFilmesActivity
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.R
import com.example.desafiofilmesrefeito.util.DataUtil
import com.example.desafiofilmesrefeito.viewModel.FilmesFavoritosActivityViewModel

class DescricaoFilme : AppCompatActivity() {

    private lateinit var filmeRecebido: Filme
    private lateinit var viewModel: FilmesFavoritosActivityViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_descricao_filme)


        setTitle("GuilhermeFlix")

        capturaIntent()

        configuraElementosDaTela()

    }

    private fun capturaIntent() {
        val extra = intent
        filmeRecebido = extra.getSerializableExtra("filmeEnviado") as Filme
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configuraElementosDaTela() {
        configuraImagemDeFundo()
        configuraImagemDePoster()
        configuraTitulo()
        configuraDataLancamento()
        configuraDescricao()
        configuraBotaoFavoritar()
        configuraAppBar()
    }

    private fun configuraImagemDeFundo() {
        val imagemFundo = findViewById<ImageView>(R.id.ImageVImagemFundo)
        Glide.with(this).load(filmeRecebido.concatFundo()).into(imagemFundo)
    }

    private fun configuraImagemDePoster() {
        val imagemIcone = findViewById<ImageView>(R.id.ImageVImagemIcone)
        Glide.with(this).load(filmeRecebido.concatPoster()).into(imagemIcone)
    }

    private fun configuraTitulo() {
        val titulo = findViewById<TextView>(R.id.TextVTitulo)
        titulo.text = filmeRecebido.title.uppercase()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configuraDataLancamento() {
        val dataLancamento = findViewById<TextView>(R.id.TextVDataLancamento)
        dataLancamento.text = "Lançamento: " + DataUtil.periodoEmTexto(filmeRecebido.release_date)
    }

    private fun configuraDescricao() {
        val descricao = findViewById<TextView>(R.id.TextVDescricao)
        descricao.text = filmeRecebido.overview
    }

    private fun configuraBotaoFavoritar() {
        val botaoFavoritar = findViewById<ImageView>(R.id.ImgVFavoritar)
        botaoFavoritar.setOnClickListener {
            viewModel.salva(filmeRecebido)
        }
    }

    private fun configuraAppBar() {
        val voltar = findViewById<ImageView>(R.id.ImageVFlecha)
        voltar.setOnClickListener {
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