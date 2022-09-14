package com.example.desafiofilmes.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafiofilmes.adapter.ListaFilmesAdapter
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.RetrofitInicializador
import com.example.desafiofilmesrefeito.activity.DescricaoFilme
import com.example.desafiofilmesrefeito.databinding.ActivityListaFilmesBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ListaFilmesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaFilmesBinding
    private val listaFilmes: ArrayList<Filme> = arrayListOf()
    val retrofit by lazy {
        RetrofitInicializador().retrofit
    }
    private var pagina = 1
    private lateinit var recyclerViewListener: RecyclerView.OnScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaFilmesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTitle("GuilhermeFlix")

        lifecycleScope.launchWhenStarted {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {

                populaLista()

            }
        }
    }

    override fun onResume() {
        super.onResume()
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

    override fun onPause() {
        super.onPause()
        binding.recyclerView.removeOnScrollListener(recyclerViewListener)
    }

    private suspend fun populaLista() {
        try {
            val response = retrofit.buscaTodas("9106a44c761c36bbb02f24c16958a56a", pagina)
            if (response.isSuccessful) {
                listaFilmes.addAll(response.body()!!.results)

                configuraScrollListener()
                pagina++

            } else {
                Log.d(TAG, "onCreate: Error")
            }
        } catch (e: HttpException) {
            Log.d(TAG, "onCreate: ${e.printStackTrace()}")
        } catch (e: IOException) {
            Log.d(TAG, "onCreate: ${e.printStackTrace()}")
        }
    }


    private fun configuraScrollListener() {
        if (::recyclerViewListener.isInitialized){
            binding.recyclerView.removeOnScrollListener(recyclerViewListener)
            binding.recyclerView.addOnScrollListener(recyclerViewListener)
        }
        val adapter = ListaFilmesAdapter()
        val recyclerView = binding.recyclerView
        val layoutManager = GridLayoutManager(this@ListaFilmesActivity, 2)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        adapter.setOnItemClickListener(object : ListaFilmesAdapter.onItemClickListener {
            override fun onItemClick(posicao: Int) {
                val filmeEnviado = listaFilmes.get(posicao)
                val intent = Intent(this@ListaFilmesActivity, DescricaoFilme::class.java)
                intent.putExtra("filmeEnviado", filmeEnviado)

                startActivity(intent)
            }
        })
        adapter.populaAdapter(listaFilmes)
    }
}