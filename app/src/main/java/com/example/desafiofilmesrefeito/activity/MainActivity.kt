package com.example.desafiofilmes.activity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafiofilmes.adapter.ListaFilmesAdapter
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.RetrofitInicializador
import com.example.desafiofilmesrefeito.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val listaFilmes: ArrayList<Filme> = arrayListOf()
    private lateinit var scrollListener:
    val retrofit by lazy {
        RetrofitInicializador().retrofit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {

                populaLista()

            }
        }
    }

    private suspend fun populaLista() {
        try {
            var pagina = 1
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
        val adapter = ListaFilmesAdapter(listaFilmes, this)
        val recyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Toast.makeText(applicationContext, "aqui é o fim", Toast.LENGTH_SHORT)
                        .show()
//                    recyclerView.removeOnScrollListener(scrollListener)
                }
            }
        })
    }
}
