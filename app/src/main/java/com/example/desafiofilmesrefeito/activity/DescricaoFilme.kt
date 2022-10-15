package com.example.desafiofilmesrefeito.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.desafiofilmes.activity.ListaFilmesActivity
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.R
import com.example.desafiofilmesrefeito.database.FilmeDatabase
import com.example.desafiofilmesrefeito.repository.FilmeFavoritoRepository
import com.example.desafiofilmesrefeito.util.DataUtil
import com.example.desafiofilmesrefeito.viewModel.FilmesFavoritosViewModel
import com.example.desafiofilmesrefeito.viewModel.factory.FilmesFavoritosViewModelFactory
import kotlinx.coroutines.launch

class DescricaoFilme : AppCompatActivity() {

    private lateinit var botaoFavoritar: ToggleButton
    private lateinit var filmeRecebido: Filme
    private val viewModel by lazy {
        val repository =
            FilmeFavoritoRepository(FilmeDatabase.getInstance(this).getFilmeFavoritoDao())
        val factory = FilmesFavoritosViewModelFactory(repository)
        ViewModelProviders.of(this, factory).get(FilmesFavoritosViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_descricao_filme)

        capturaIntent()

        checaSeFilmeExiste()

        configuraElementosDaTela()

    }

    private fun checaSeFilmeExiste() {
        botaoFavoritar = findViewById(R.id.ToggleBFavoritar)
        lifecycleScope.launch {
            botaoFavoritar.isChecked = viewModel.checaSeExiste(filmeRecebido.id)
        }
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
        configuraRatingBar()
    }

    private fun configuraRatingBar() {
        val ratingBar = findViewById<RatingBar>(R.id.RatingBNota)
        ratingBar.rating = filmeRecebido.vote_average/2

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
        dataLancamento.text = DataUtil.periodoEmTextoApenasAno(filmeRecebido.release_date)
    }

    private fun configuraDescricao() {
        val descricao = findViewById<TextView>(R.id.TextVDescricao)
        descricao.text = filmeRecebido.overview
    }

    private fun configuraBotaoFavoritar() {
        botaoFavoritar.setOnCheckedChangeListener { _, b ->
            if (b) {
                viewModel.salva(filmeRecebido)
            } else {
                viewModel.remove(filmeRecebido.id)
            }
        }
    }


    private fun configuraAppBar() {
        criaBotaoVoltarAppBar()
        criaBotaoFavoritosAppBar()
    }

    private fun criaBotaoVoltarAppBar() {
        val voltar = findViewById<ImageView>(R.id.ImageVFlecha)
        voltar.setOnClickListener {
            val intent = Intent(this, ListaFilmesActivity::class.java)
            startActivity(intent)
        }

    }

    private fun criaBotaoFavoritosAppBar() {
        val favoritos = findViewById<TextView>(R.id.TextVFavoritos)
        favoritos.setOnClickListener {
            val intent = Intent(this, FilmesFavoritosActivity::class.java)
            startActivity(intent)
        }
    }
}