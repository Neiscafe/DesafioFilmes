package com.example.desafiofilmes.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafiofilmes.adapter.ListaFilmesAdapter
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.R
import com.example.desafiofilmesrefeito.RetrofitInicializador
import com.example.desafiofilmesrefeito.activity.DescricaoFilme
import com.example.desafiofilmesrefeito.activity.FilmesFavoritosActivity
import com.example.desafiofilmesrefeito.database.FilmeDatabase
import com.example.desafiofilmesrefeito.databinding.ActivityListaFilmesBinding
import com.example.desafiofilmesrefeito.repository.FilmeFavoritoRepository
import com.example.desafiofilmesrefeito.viewModel.FilmesFavoritosViewModel
import com.example.desafiofilmesrefeito.viewModel.factory.FilmesFavoritosViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class ListaFilmesActivity : AppCompatActivity() {

    private var listaSelecionados = mutableListOf<Filme>()
    private var listaFilmes = mutableListOf<Filme>()
    private var numeroItensSelecionados: Int = 0
    private lateinit var binding: ActivityListaFilmesBinding
    private lateinit var recyclerViewListener: RecyclerView.OnScrollListener
    private lateinit var voltar: ImageView
    private lateinit var iconeFavoritar: ImageView
    private lateinit var favoritos: TextView

    private val viewModel by lazy {
        val repository =
            FilmeFavoritoRepository(FilmeDatabase.getInstance(this).getFilmeFavoritoDao())
        val factory = FilmesFavoritosViewModelFactory(repository)
        ViewModelProviders.of(this, factory).get(FilmesFavoritosViewModel::class.java)
    }

    val mainAdapter by lazy {
        ListaFilmesAdapter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaFilmesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configuraAppBar()
        pegaDadosApiAssincrono()
    }

    private fun pegaDadosApiAssincrono() {
        lifecycleScope.launchWhenStarted {
            configuraAdapter()
            populaLista()
        }
    }

    private fun configuraAdapter() {

        val recyclerView = binding.recyclerView
        val layoutManager = GridLayoutManager(this@ListaFilmesActivity, 4)

        recyclerView.adapter = mainAdapter
        recyclerView.layoutManager = layoutManager
    }

    private fun populaLista() {
        lifecycleScope.launch {
            val response = viewModel.populaLista()
            listaFilmes.addAll(response)
            mainAdapter.populaAdapter(listaFilmes)

            configuraAdapterListener()
            configuraClickListener()
        }
    }

    private fun configuraAdapterListener() {
        if (::recyclerViewListener.isInitialized) {
            binding.recyclerView.removeOnScrollListener(recyclerViewListener)
        }
        recyclerViewListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount = layoutManager.childCount
                    val totalItemVisible = visibleItemCount + pastVisibleItems
                    val totalItemCount = layoutManager.itemCount

                    if (totalItemVisible >= totalItemCount) {
                        binding.recyclerView.removeOnScrollListener(recyclerViewListener)
                        lifecycleScope.launch {
                            populaLista()
                        }
                    }
                }
            }
        }
        binding.recyclerView.addOnScrollListener(recyclerViewListener)
    }

    private fun configuraClickListener() {
        mainAdapter.setOnItemClickListener(object : ListaFilmesAdapter.onItemClickListener {
            override fun onItemClick(posicao: Int) {
                if (!listaFilmes[posicao].selected) {
                    val filmeSendoEnviado = listaFilmes[posicao]
                    val intent = Intent(this@ListaFilmesActivity, DescricaoFilme::class.java)
                    intent.putExtra("filmeEnviado", filmeSendoEnviado)
                    startActivity(intent)

                } else {
                    listaFilmes[posicao].selected = false
                    numeroItensSelecionados--
                    listaSelecionados.remove(listaFilmes[posicao])
                    atualizarListaSelecionados()
                }
            }

            override fun onItemLongClick(posicao: Int) {
                if (!listaFilmes[posicao].selected) {
                    listaFilmes[posicao].selected = true
                    numeroItensSelecionados++
                    listaSelecionados.add(listaFilmes[posicao])
                    atualizarListaSelecionados()
                } else {
                    listaFilmes[posicao].selected = false
                    numeroItensSelecionados--
                    listaSelecionados.remove(listaFilmes[posicao])
                    atualizarListaSelecionados()
                }
            }

            private fun atualizarListaSelecionados() {
                mainAdapter.notifyDataSetChanged()
                logicaFilmesSelecionados()
            }
        })
    }

    private fun configuraAppBar() {
        voltar = findViewById(R.id.ImageVFlecha)
        iconeFavoritar = findViewById(R.id.ImageVIconeFavoritar)
        favoritos = findViewById(R.id.TextVFavoritos)

        voltar.isVisible = false

        favoritos.setOnClickListener {
            val intent = Intent(this, FilmesFavoritosActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun logicaFilmesSelecionados() {

        if (numeroItensSelecionados > 0) {
            iconeFavoritar.isVisible = true
            favoritos.isVisible = false

            iconeFavoritar.setOnClickListener {
                for (filme in listaSelecionados) {
                    filme.selected = false
                    viewModel.salva(filme)

                }
                iconeFavoritar.isVisible = false
                favoritos.isVisible = true
            }
            numeroItensSelecionados = 0
        } else {
            iconeFavoritar.isVisible = true
            favoritos.isVisible = false
        }

    }

    override fun onResume() {
        super.onResume()
        configuraAdapterListener()
    }

    override fun onPause() {
        super.onPause()
        binding.recyclerView.removeOnScrollListener(recyclerViewListener)
    }
}
