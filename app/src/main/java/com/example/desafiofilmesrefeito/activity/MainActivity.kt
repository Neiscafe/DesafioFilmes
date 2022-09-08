package com.example.desafiofilmes.activity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.desafiofilmes.adapter.ListaFilmesAdapter
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.RetrofitInicializador
import com.example.desafiofilmesrefeito.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val listaFilmes: ArrayList<Filme> = arrayListOf()
    private val retrofit by lazy {
        RetrofitInicializador().retrofit
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launchWhenStarted {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                try {
                    val response = retrofit.buscaTodas()
                    if (response.isSuccessful) {
                        listaFilmes.addAll(response.body()!!.results)
                    } else {
                        Log.d(TAG, "onCreate: Error")
                    }
                } catch (e: HttpException) {
                    Log.d(TAG, "onCreate: ${e.printStackTrace()}")
                } catch (e: IOException) {
                    Log.d(TAG, "onCreate: ${e.printStackTrace()}")
                }
                val adapter = ListaFilmesAdapter(listaFilmes, this@MainActivity)
                val recyclerView = binding.recyclerView
                recyclerView.adapter = adapter
            }
        }

    }
}