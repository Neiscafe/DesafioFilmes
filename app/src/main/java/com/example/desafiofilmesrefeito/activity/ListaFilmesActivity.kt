package com.example.desafiofilmes.activity

import android.content.ContentValues.TAG
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
    private var estado: Int = 0
    private lateinit var binding: ActivityListaFilmesBinding
    private var pagina = 1
    private lateinit var recyclerViewListener: RecyclerView.OnScrollListener
    private val listaFilmes: MutableList<Filme> = mutableListOf()

    private val viewModel by lazy {
        val repository =
            FilmeFavoritoRepository(FilmeDatabase.getInstance(this).getFilmeFavoritoDao())
        val factory = FilmesFavoritosViewModelFactory(repository)
        ViewModelProviders.of(this, factory).get(FilmesFavoritosViewModel::class.java)
    }
    val retrofit by lazy {
        RetrofitInicializador().retrofit
    }
    val mainAdapter by lazy {
        ListaFilmesAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaFilmesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pegaDadosApiAssincrono()
        configuraAppBar()

    }

    private fun configuraAppBar() {
        val flecha = binding.appbar.ImageVFlecha
        var iconeFavoritar = findViewById<ImageView>(R.id.ImageVIconeFavoritar)
        var favoritos = findViewById<TextView>(R.id.TextVFavoritos)
        flecha.isVisible = false

        if (estado > 0) {
            iconeFavoritar.isVisible = true
            favoritos.isVisible = false

            iconeFavoritar.setOnClickListener {
                for (filme in listaSelecionados) {
                    viewModel.salva(filme)
                    iconeFavoritar.isVisible = false
                    favoritos.isVisible = true
                    filme.selected = false
                    mainAdapter.notifyDataSetChanged()
                    estado = 0
                }

            }
        } else {
            favoritos.isVisible = true
            iconeFavoritar.isVisible = false

            favoritos.setOnClickListener {
                val intent = Intent(this, FilmesFavoritosActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
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

    private suspend fun populaLista() {
        try {
            val response = retrofit.buscaTodas("9106a44c761c36bbb02f24c16958a56a", pagina)
            if (response.isSuccessful) {
                listaFilmes.addAll(response.body()!!.results)
                mainAdapter.populaAdapter(listaFilmes)

                configuraAdapterListener()
                configuraLongListener()
                pagina++

            } else {
                Log.d("deu errado", "onCreate: Error")
            }
        } catch (e: HttpException) {
            Log.d("deu errado", "onCreate: ${e.printStackTrace()}")
        } catch (e: IOException) {
            Log.d("deu errado", "onCreate: ${e.printStackTrace()}")
        }
    }

    private fun configuraLongListener() {
        mainAdapter.setOnItemClickListener(object : ListaFilmesAdapter.onItemClickListener {
            override fun onItemClick(posicao: Int) {

                if (!listaFilmes[posicao].selected) {
                    val filmeSendoEnviado = listaFilmes[posicao]
                    val intent = Intent(this@ListaFilmesActivity, DescricaoFilme::class.java)
                    intent.putExtra("filmeEnviado", filmeSendoEnviado)
                    startActivity(intent)
                } else {
                    listaFilmes[posicao].selected = false
                    estado--
                    listaSelecionados.remove(listaFilmes[posicao])
                    mainAdapter.notifyDataSetChanged()
                    configuraAppBar()
                    estado = 0
                }
            }

            override fun onItemLongClick(posicao: Int) {
                if (listaFilmes[posicao].selected == false) {
                    listaFilmes[posicao].selected = true
                    estado++
                    listaSelecionados.add(listaFilmes[posicao])
                    mainAdapter.notifyDataSetChanged()
                    configuraAppBar()
                } else {
                    listaFilmes[posicao].selected = false
                    estado--
                    listaSelecionados.remove(listaFilmes[posicao])
                    mainAdapter.notifyDataSetChanged()
                    configuraAppBar()
                    estado = 0
                }
            }
        })
    }

    private fun configuraAdapterListener() {
        if (::recyclerViewListener.isInitialized) {
            binding.recyclerView.removeOnScrollListener(recyclerViewListener)
        }
        recyclerViewListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    println("scroll vertical é maior que zero")
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount = layoutManager.childCount
                    val totalItemVisible = visibleItemCount + pastVisibleItems
                    val totalItemCount = layoutManager.itemCount

                    if (totalItemVisible >= totalItemCount) {
                        println("total de itens visíveis é maior ou igual ao número de itens")
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

    override fun onResume() {
        super.onResume()
        configuraAdapterListener()
    }

    override fun onPause() {
        super.onPause()
        binding.recyclerView.removeOnScrollListener(recyclerViewListener)
    }
}
